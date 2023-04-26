package be.kennyverheyden.controller;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.Book;
import be.kennyverheyden.models.Month;
import be.kennyverheyden.models.User;
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
	
	// Get current month
	Date date = new Date();
	String monthDateFormat = "MM";
	String monthDateFormat_long = "MMMM";
	SimpleDateFormat mdf = new SimpleDateFormat(monthDateFormat);
	String month = mdf.format(date);
	SimpleDateFormat mdf_long = new SimpleDateFormat(monthDateFormat_long);
	String month_long = StringUtils.capitalize(mdf_long.format(date));

	public BookController() {}

	@GetMapping("/book")
	public String bookGet(Model model)
	{
		String userEmail = userService.getUserEmail();
		//	When user is not logged on, the String is null

		if(userEmail==null)
		{
			model.addAttribute("content", "home");
			return "redirect:/";
		}
		
		User user=userService.findUserByeMail(userService.getUserEmail()); // Get user information
		//List bookings = bookService.findBookByUserUserID(user.getUserID());
		List bookings = bookService.findBookByUserUserIDperMonth(user.getUserID(),month); // Get filtered book lines from user per month
		Collections.reverse(bookings); // Show newest first
		bookService.loadBooks(user);
		model.addAttribute("books",bookings); // Read bookings to html
		model.addAttribute("categories",categoryService.findCategoryByUserUserID(user.getUserID())); // Read categories for select option in html
		model.addAttribute("month_long",month_long);
		model.addAttribute("month", month);
		model.addAttribute("result", bookService.monthResult(user.getUserID(), month));
		model.addAttribute("content", "book");
		return "index";
	}
	
	@PostMapping("/book/filter")
	public String bookFilterPost(@RequestParam (required = false) String month, Model model, RedirectAttributes rm)
	{
		User user=userService.findUserByeMail(userService.getUserEmail()); // Get user information
		List bookings = bookService.findBookByUserUserIDperMonth(user.getUserID(),month); // Get filtered book lines from user per month
		Collections.reverse(bookings); // Show newest first
		bookService.loadBooks(user);
		model.addAttribute("books",bookings); // Read bookings to html
		model.addAttribute("categories",categoryService.findCategoryByUserUserID(user.getUserID())); // Read categories for select option in html
		model.addAttribute("month_long",Month.getMonthByStringNumber(month));
		model.addAttribute("month",month);
		model.addAttribute("result", bookService.monthResult(user.getUserID(), month));
		model.addAttribute("content", "book");
		return "index";
	}

	@PostMapping("/book/update")
	public String bookUpdatePost(@RequestParam (required = false) Long bookID, @RequestParam (required = false) String date, @RequestParam (required = false) float amount, @RequestParam (required = false) String description, Boolean delete, @RequestParam (required = false) String categoryName, Model model, RedirectAttributes rm)
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
			if(date!="" || amount!=0 || description!="" || categoryName!="")
			{
				User user=userService.findUserByeMail(userService.getUserEmail()); // Get user information
				bookService.updateBook(bookID, date, amount, description, categoryName, user);
				model.addAttribute("content", "book");
				rm.addFlashAttribute("message","Booking succesfully updated");
				return "redirect:/book";
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
	public String bookAddPost(@RequestParam (required = false) Float amount, @RequestParam (required = false) String inout, @RequestParam (required = false) String categoryName, @RequestParam (required = false) String description, Model model, RedirectAttributes rm)
	{
		//	System.out.println(amount + " " + description);
		if(amount!=null && inout!="" && categoryName!="")
		{
			// make value negative

			if(inout.equals("out") && amount>0)
			{
				amount=amount-(amount*2);
			}

			User user=userService.findUserByeMail(userService.getUserEmail()); // Get user information
			bookService.addBook(amount, description, categoryName, user);
			model.addAttribute("content", "book");
			rm.addFlashAttribute("message","Booking succesfully updated");
			return "redirect:/book";
		}
		else
		{
			model.addAttribute("content", "book");
			rm.addFlashAttribute("message","Fill in all fields");
			return "redirect:/book";
		}
	}

}
