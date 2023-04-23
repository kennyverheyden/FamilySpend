package be.kennyverheyden.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.GroupedCategory;
import be.kennyverheyden.services.BookService;
import be.kennyverheyden.services.CategoryService;
import be.kennyverheyden.services.GroupService;
import be.kennyverheyden.services.GroupedCategoryService;
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
	private GroupedCategoryService groupedCategoryService;

	public CategoryController() {}

	@GetMapping("/category")
	public String categorieGet(Model model)
	{

		String userEmail = userService.getUserEmail();
		// When user is not logged on, the String is null

		if(userEmail==null)
		{
			model.addAttribute("content", "login");
			return "redirect:/";
		}
		categoryService.loadCategories(userService.findUserByeMail(userService.getUserEmail())); // Collect and load categories from specific user
		groupService.loadGroups(userService.findUserByeMail(userService.getUserEmail())); // Collect and load categories from specific user
		model.addAttribute("categories",categoryService.findCategoryByUserUserID(userService.findUserByeMail(userEmail).getUserID()));
		model.addAttribute("groups",groupService.getGroups()); // Bind groups to Select options
		model.addAttribute("content", "category");
		return "index";
	}
	
	@GetMapping("/categorytotals")
	public String categorieTotalsGet(Model model)
	{
		String userEmail = userService.getUserEmail();
		// When user is not logged on, the String is null

		if(userEmail==null)
		{
			model.addAttribute("content", "login");
			return "redirect:/";
		}
		List<GroupedCategory> groupedCategories = groupedCategoryService.groupedCategories();
		model.addAttribute("groupedCategories",groupedCategories);
		model.addAttribute("content", "categorytotals");
		return "index";
	}

	@PostMapping("/category/update") 
	public String updateCategoryPost(@RequestParam Long categoryID, @RequestParam String categoryName, String groupName, Boolean delete, Model model, RedirectAttributes rm)
	{
		if(delete==null) // avoid error Cannot invoke "java.lang.Boolean.booleanValue()" because "delete" is null
		{
			delete=false;
		}

		if(delete)
		{
			if(!bookService.bookingHasCategory(categoryID, userService.getUserEmail()))
			{
				categoryService.deleteCategory(categoryService.findCategoryByCategoryID(categoryID));
				model.addAttribute("content", "category");
				rm.addFlashAttribute("message","Category succesfully deleted");
				return "redirect:/category";
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
				if(!categoryName.equalsIgnoreCase(checkDuplicat) || !categoryService.groupInCategoryIsEqual(categoryName, userService.findUserByeMail(userService.getUserEmail()), groupName))
				{
					categoryService.updateCategory(categoryID,categoryName,groupName,userService.findUserByeMail(userService.getUserEmail()));
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
	public String addCategoryPost(@RequestParam String categoryName, String groupName, Model model, RedirectAttributes rm)
	{
		if(categoryService.getCategories().size()<50) // Allow 50 per user
		{
			if(categoryName!="")
			{
				if(groupName!="")
				{
					String checkDuplicat = giveDuplicateIfExist(categoryName);
					if(!categoryName.equalsIgnoreCase(checkDuplicat) || !categoryService.groupInCategoryIsEqual(categoryName, userService.findUserByeMail(userService.getUserEmail()), groupName))
					{
						categoryService.addCategory(categoryName,groupName,userService.findUserByeMail(userService.getUserEmail()));
						model.addAttribute("content", "category");
						rm.addFlashAttribute("message","Category succesfully added");
						return "redirect:/category";
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
		if(categoryService.findCategoryByCategoryName(categoryName, userService.findUserByeMail(userService.getUserEmail()))!=null)
		{
			checkDuplicat = categoryService.findCategoryByCategoryName(categoryName, userService.findUserByeMail(userService.getUserEmail())).getCategoryName().toString();
		}
		return checkDuplicat;
	}

}
