package be.kennyverheyden.controller;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.Category;
import be.kennyverheyden.models.Month;
import be.kennyverheyden.models.User;
import be.kennyverheyden.processors.UserDetailsImpl;
import be.kennyverheyden.services.BookService;
import be.kennyverheyden.services.CategoryService;
import be.kennyverheyden.services.UserService;


@Controller
public class BookController {

	@Autowired
	private BookService bookService;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserDetailsImpl userDetails;

	// Get current month and year
	Date date = new Date();
	String monthDateFormat = "MM";
	String monthDateFormat_long = "MMMM";
	SimpleDateFormat mdf = new SimpleDateFormat(monthDateFormat);
	String month = mdf.format(date);
	SimpleDateFormat mdf_long = new SimpleDateFormat(monthDateFormat_long);
	String month_long = StringUtils.capitalize(mdf_long.format(date));
	String year = Integer.toString(LocalDate.now().getYear());

	// Get current date
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
	LocalDateTime now = LocalDateTime.now();
	String currentDate=dtf.format(now);

	private String selectedMonth = null; // Keep the last month selection (user choice)
	private String selectedYear = null; // Keep the last year selection (user choice)

	public BookController() {}

	@GetMapping("/book")
	public String bookGet(Model model)
	{
		String userEmail = userDetails.getUsername();
		//	When user is not logged on, the String is null

		if(selectedMonth!=null) 
		{
			month=selectedMonth; // Set back the last user choice
		}
		if(selectedYear!=null)
		{
			year=selectedYear; // Set back the last user choice
		}

		User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
		List bookings = bookService.findBookByUserUserIDperMonth(user.getUserID(),month,year); // Get filtered book lines from user per month
		Collections.reverse(bookings); // Show newest first
		bookService.loadBooks(user);
		model.addAttribute("books",bookings); // Read bookings to html
		model.addAttribute("categories",categoryService.findCategoryByUserUserID(user.getUserID())); // Read categories for select option in html
		model.addAttribute("month_long",Month.getMonthByStringNumber(month)); // Print month title
		model.addAttribute("month", month); // Set the month
		model.addAttribute("income",bookService.monthResultIncome(user, month, year));
		model.addAttribute("spending",bookService.monthResultSpending(user, month, year));
		model.addAttribute("result",bookService.monthResult(user, month, year));
		model.addAttribute("currency",user.getCurrency().getCurrencySymbol()); // Currency
		model.addAttribute("years",bookService.getYears(user.getUserID())); // Dropdown filter, get years used by user
		model.addAttribute("year",year); // Dropdown selected option (month filter)
		model.addAttribute("currentDate",currentDate); // Add booking date field
		model.addAttribute("content", "book");
		return "index";
	}

	@PostMapping("/book/filter")
	public String bookFilterPost(@RequestParam (required = false) String month, String year, Model model, RedirectAttributes rm)
	{
		selectedMonth=month; // Keep the last user choice
		if(year!=null)
		{
			selectedYear=year; // Keeop the last user choice
		}
		else
		{
			year = Integer.toString(LocalDate.now().getYear());
		}
		User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
		List bookings = bookService.findBookByUserUserIDperMonth(user.getUserID(),month,year); // Get filtered book lines from user per month
		Collections.reverse(bookings); // Show newest first
		bookService.loadBooks(user);
		model.addAttribute("books",bookings); // Read bookings to html
		model.addAttribute("categories",categoryService.findCategoryByUserUserID(user.getUserID())); // Read categories for select option in html
		model.addAttribute("month_long",Month.getMonthByStringNumber(month));
		model.addAttribute("month", month);
		model.addAttribute("income",bookService.monthResultIncome(user, month, year));
		model.addAttribute("spending",bookService.monthResultSpending(user, month, year));
		model.addAttribute("result",bookService.monthResult(user, month, year));
		model.addAttribute("currency",user.getCurrency().getCurrencySymbol());
		model.addAttribute("years",bookService.getYears(user.getUserID())); // Dropdown filter
		model.addAttribute("year",year); // Dropdown selected option
		model.addAttribute("currentDate",currentDate); // Add booking date field
		model.addAttribute("content", "book");
		return "index";
	}

	@PostMapping("/book/update")
	public String bookUpdatePost(@RequestParam (required = false) Long bookID, @RequestParam (required = false) String date, @RequestParam (required = false) String stramount, @RequestParam (required = false) String description, Boolean delete, @RequestParam (required = false) String categoryName, Model model, RedirectAttributes rm) throws ParseException
	{
		if(delete==null) // Avoid error Cannot invoke "java.lang.Boolean.booleanValue()" because "delete" is null
		{
			delete=false;
		}
		if(delete)
		{
			bookService.deleteBook(bookService.findBookBybookID(bookID));
			model.addAttribute("content", "book");
			rm.addFlashAttribute("message","Booking succesfully deleted");
			return "redirect:/book";
		}
		else
		{	
			NumberFormat format =  NumberFormat.getInstance(Locale.getDefault());
			Number number = format.parse(stramount);
			double amount = number.doubleValue();
			if(date!="" || amount!=0 || description!="" || categoryName!="")
			{
				if(this.dateValidator(date))
				{
					try
					{
						User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
						bookService.updateBook(bookID, date, amount, description, categoryName, user);
						model.addAttribute("content", "book");
						rm.addFlashAttribute("message","Booking succesfully updated");
						return "redirect:/book";
					}
					catch (Exception e) {
						model.addAttribute("error", e.getMessage());
						return "redirect:/book";
					}
				}
				else
				{
					model.addAttribute("content", "book");
					rm.addFlashAttribute("message","Enter a valid date");
					return "redirect:/book";
				}
			}
			else
			{
				model.addAttribute("content", "book");
				rm.addFlashAttribute("message","Fill in all fields");
				return "redirect:/book";
			}
		}
	}

	@PostMapping("/book/add")
	public String bookAddPost(@RequestParam (required = false) Float amount, @RequestParam (required = false) String addDate, @RequestParam (required = false) Long categoryID, @RequestParam (required = false) String description, Model model, RedirectAttributes rm)
	{
		if(amount!=null && categoryID!=null && addDate!=null)
		{
			// If booking is not income, make number negative
			// Determined by category
			Category category=categoryService.findCategoryByCategoryID(categoryID); 
			if(category.getInOut()!=0) // 0=income
			{
				amount=amount-(amount*2); // Make number negative
			}

			if(dateValidator(addDate))
			{
				try
				{
					User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
					bookService.addBook(amount, addDate, description, category.getCategoryName(), user);
					model.addAttribute("content", "book");
					rm.addFlashAttribute("message","Booking succesfully updated");
					return "redirect:/book";
				}
				catch (Exception e) {
					model.addAttribute("error", e.getMessage());
					return "redirect:/book";
				}
			}
			else
			{
				model.addAttribute("content", "book");
				rm.addFlashAttribute("message","Enter a valid date");
				return "redirect:/book";
			}
		}
		else
		{
			model.addAttribute("content", "book");
			rm.addFlashAttribute("message","Fill in all fields");
			return "redirect:/book";
		}
	}

	@PostMapping("/book/deletebook")
	public String bookFilterPost(Model model, RedirectAttributes rm)
	{
		if(selectedMonth!=null) 
		{
			month=selectedMonth; // Set back the last user choice
		}
		if(selectedYear!=null)
		{
			year=selectedYear; // Set back the last user choice
		}
		try
		{
			bookService.deleteMonthBook(userService.findUserByeMail(userDetails.getUsername()).getUserID(), month, year);
			model.addAttribute("content", "book");
			rm.addFlashAttribute("message","Month bookings deleted");
			return "redirect:/book";
		}
		catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "redirect:/book";
		}
	}

	// Book page - Update date input
	private boolean dateValidator(String date)
	{
		int format = LocalDate.now().getYear();

		if(date!=null && date.length()==10)
		{
			String dayStr=date.substring(0,2);
			String monthStr=date.substring(3,5);
			String yearStr=date.substring(6,10);


			int day= Integer.parseInt(dayStr);
			int month =  Integer.parseInt(monthStr);
			int year= Integer.parseInt(yearStr);

			if(date.substring(2,3).equals("/") && date.substring(5,6).equals("/"))
			{
				if(format-year<=5 && year-format<=1)
				{
					switch(month){
					case 1:
						if(day<=31 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}
					case 2:
						if(year%4==0)
						{
							if(day<=29 && day>0)
							{
								return true;
							}
							else
							{
								return false;
							}
						}
						else
						{
							if(day<=28 && day>0)
							{
								return true;
							}
							else
							{
								return false;
							}
						}
					case 3:
						if(day<=31 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}
					case 4:
						if(day<=30 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}

					case 5:
						if(day<=31 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}
					case 6:
						if(day<30 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}
					case 7:
						if(day<=31 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}
					case 8:
						if(day<=31 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}
					case 9:
						if(day<=30 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}
					case 10:
						if(day<=31 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}
					case 11:
						if(day<=30 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}
					case 12:
						if(day<=31 && day>0)
						{
							return true;
						}
						else
						{
							return false;
						}
					default:
						return false;
					} 
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

}
