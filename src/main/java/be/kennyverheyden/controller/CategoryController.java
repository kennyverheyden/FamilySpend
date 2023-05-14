package be.kennyverheyden.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.Category;
import be.kennyverheyden.models.GroupedCategory;
import be.kennyverheyden.models.Month;
import be.kennyverheyden.processors.UserDetailsImpl;
import be.kennyverheyden.services.BookService;
import be.kennyverheyden.services.CategoryService;
import be.kennyverheyden.services.GroupService;
import be.kennyverheyden.services.UserService;


@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;
	@Autowired
	private UserDetailsImpl userDetails;

	Date date = new Date();
	String monthDateFormat = "MM";
	String monthDateFormat_long = "MMMM";
	SimpleDateFormat mdf = new SimpleDateFormat(monthDateFormat);
	String month = mdf.format(date);
	SimpleDateFormat mdf_long = new SimpleDateFormat(monthDateFormat_long);
	String month_long = StringUtils.capitalize(mdf_long.format(date));
	String year = Integer.toString(LocalDate.now().getYear());

	public CategoryController() {}

	@GetMapping("/category")
	public String categorieGet(Model model)
	{

		String userEmail = userDetails.getUsername();
		// When user is not logged on, the String is null

		categoryService.loadCategories(userService.findUserByeMail(userDetails.getUsername())); // Collect and load categories from specific user
		groupService.loadGroups(userService.findUserByeMail(userDetails.getUsername())); // Collect and load categories from specific user
		model.addAttribute("categories",categoryService.findCategoryByUserUserID(userService.findUserByeMail(userEmail).getUserID()));
		model.addAttribute("groups",groupService.getGroups()); // Bind groups to Select options
		model.addAttribute("content", "category");
		return "index";
	}

	@GetMapping("/categorytotals")
	public String categorieTotalsGet(Model model)
	{
		String userEmail = userDetails.getUsername();

		// Get Category by group by and totals
		Long userID=userService.findUserByeMail(userDetails.getUsername()).getUserID();
		List<GroupedCategory> groupedCategories = bookService.bookGroupByCategoryMonth(userID,month,year);
		model.addAttribute("groupedCategories",groupedCategories);
		model.addAttribute("month_long",Month.getMonthByStringNumber(month));
		model.addAttribute("month", month);
		model.addAttribute("currency",userService.findUserByeMail(userDetails.getUsername()).getCurrency().getCurrencySymbol());
		model.addAttribute("years",bookService.getYears(userService.findUserByeMail(userDetails.getUsername()).getUserID())); // Dropdown filter
		model.addAttribute("year",year); // Dropdown selected option
		model.addAttribute("content", "categorytotals");
		return "index";
	}

	@PostMapping("/categorytotals/totals") 
	public String categoryTotalsPost(@RequestParam String month, String year, Model model, RedirectAttributes rm)
	{
		Long userID=userService.findUserByeMail(userDetails.getUsername()).getUserID();
		List<GroupedCategory> groupedCategories = bookService.bookGroupByCategoryMonth(userID,month,year);

		model.addAttribute("content", "categorytotals");
		model.addAttribute("month_long",Month.getMonthByStringNumber(month));
		model.addAttribute("month",month);
		model.addAttribute("currency",userService.findUserByeMail(userDetails.getUsername()).getCurrency().getCurrencySymbol());
		model.addAttribute("years",bookService.getYears(userService.findUserByeMail(userDetails.getUsername()).getUserID())); // Dropdown filter
		model.addAttribute("year",year); // Dropdown selected option
		model.addAttribute("groupedCategories", groupedCategories);
		return "index";
	}

	@PostMapping("/category/update") 
	public String updateCategoryPost(@RequestParam (required = false) Long categoryID, @RequestParam (required = false) String categoryName, @RequestParam (required = false) String groupName, @RequestParam (required = false) Integer inout, @RequestParam (required = false) Boolean delete, Model model, RedirectAttributes rm)
	{
		Category category = categoryService.findCategoryByCategoryID(categoryID);
		if(delete==null) // avoid error Cannot invoke "java.lang.Boolean.booleanValue()" because "delete" is null
		{
			delete=false;
		}

		if(delete)
		{
			if(!bookService.bookingHasCategory(categoryID, userDetails.getUsername()))
			{
				try
				{
					categoryService.deleteCategory(categoryService.findCategoryByCategoryID(categoryID));
					model.addAttribute("content", "category");
					rm.addFlashAttribute("message","Category succesfully deleted");
					return "redirect:/category";
				}
				catch (Exception e) {
					model.addAttribute("error", e.getMessage());
					return "redirect:/category";
				}
			}
			else
			{
				model.addAttribute("content", "category");
				rm.addFlashAttribute("message","Not deleted because category has one or more bookings");
				return "redirect:/category";
			}
		}
		else
		{
			if(categoryName!="")
			{
				String checkDuplicat = giveDuplicateIfExist(categoryName);
				if(!categoryName.equalsIgnoreCase(checkDuplicat) || !categoryService.groupInCategoryIsEqual(categoryName, userService.findUserByeMail(userDetails.getUsername()), groupName) || inout!=category.getInOut())
				{
					categoryService.updateCategory(categoryID,categoryName,groupName,inout,userService.findUserByeMail(userDetails.getUsername()));
					model.addAttribute("content", "category");
					rm.addFlashAttribute("message","Information succesfully updated");
					return "redirect:/category";
				}
				else
				{
					model.addAttribute("content", "category");
					rm.addFlashAttribute("message","Category with same group already exist");
					return "redirect:/category";
				}

			}
			else
			{
				model.addAttribute("content", "category");
				rm.addFlashAttribute("message","Fill in a category name");
				return "redirect:/category";
			}
		}
	}

	@PostMapping("/category/add") 
	public String addCategoryPost(@RequestParam (required = false) String categoryName,@RequestParam (required = false) String groupName,@RequestParam (required = false) Integer inout, Model model, RedirectAttributes rm)
	{
		if(categoryService.getCategories().size()<50) // Allow 50 per user
		{
			if(categoryName!="")
			{
				if(groupName!="")
				{
					if(inout!=null)
					{
						String checkDuplicat = giveDuplicateIfExist(categoryName);
						if(!categoryName.equalsIgnoreCase(checkDuplicat) || !categoryService.groupInCategoryIsEqual(categoryName, userService.findUserByeMail(userDetails.getUsername()), groupName))
						{
							try
							{
								categoryService.addCategory(categoryName,groupName,inout,userService.findUserByeMail(userDetails.getUsername()));
								model.addAttribute("content", "category");
								rm.addFlashAttribute("message","Category succesfully added");
								return "redirect:/category";
							}
							catch (Exception e) {
								model.addAttribute("error", e.getMessage());
								return "redirect:/category";
							}
						}
						else
						{
							model.addAttribute("content", "category");
							rm.addFlashAttribute("message","Category already exist");
							return "redirect:/category";
						}
					}
					else
					{
						model.addAttribute("content", "category");
						rm.addFlashAttribute("message","Select IN or OUT");
						return "redirect:/category";
					}
				}
				else
				{
					model.addAttribute("content", "category");
					rm.addFlashAttribute("message","Select a group name");
					return "redirect:/category";
				}
			}
			else
			{
				model.addAttribute("content", "category");
				rm.addFlashAttribute("message","Fill in a category name");
				return "redirect:/category";
			}
		}
		else
		{
			model.addAttribute("content", "category");
			rm.addFlashAttribute("message","Max amount categories reached");
			return "redirect:/category";
		}
	}

	public String giveDuplicateIfExist(String categoryName)
	{
		// Check if categoryName already exist
		String checkDuplicat = null;
		if(categoryService.findCategoryByCategoryName(categoryName, userService.findUserByeMail(userDetails.getUsername()))!=null)
		{
			checkDuplicat = categoryService.findCategoryByCategoryName(categoryName, userService.findUserByeMail(userDetails.getUsername())).getCategoryName().toString();
		}
		return checkDuplicat;
	}

}
