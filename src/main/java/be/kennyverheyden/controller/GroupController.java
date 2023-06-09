package be.kennyverheyden.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.Group;
import be.kennyverheyden.models.GroupedGroup;
import be.kennyverheyden.models.Month;
import be.kennyverheyden.models.User;
import be.kennyverheyden.processors.UserDetailsImpl;
import be.kennyverheyden.services.BookService;
import be.kennyverheyden.services.CategoryService;
import be.kennyverheyden.services.GroupService;
import be.kennyverheyden.services.UserService;

@SessionScope
@Controller
public class GroupController {

	@Autowired
	private GroupService groupService;
	@Autowired
	CategoryService categoryService;
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

	public GroupController() {}

	@GetMapping("/group")
	public String groupGet(Model model)
	{
		User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
		groupService.loadGroups(user); // Collect and load groups from specific user
		List<Group> groups = groupService.findGroupByUserUserID(user.getUserID());
		Collections.sort(groups); 
		model.addAttribute("groups",groups);
		model.addAttribute("content", "group");
		return "index";
	}

	@GetMapping("/grouptotals")
	public String groupTotalsGet(Model model)
	{
		// Get group by groups and totals
		User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
		List<GroupedGroup> groupedGroups = bookService.bookGroupByGroupMonth(user.getUserID(),month,year);
		Collections.sort(groupedGroups);
		model.addAttribute("groupedGroups", groupedGroups);
		model.addAttribute("month_long",Month.getMonthByStringNumber(month));
		model.addAttribute("month",month);
		model.addAttribute("currency",user.getCurrency().getCurrencySymbol());
		model.addAttribute("years",bookService.getYears(user.getUserID())); // Dropdown filter
		model.addAttribute("year",year); // Dropdown selected option
		model.addAttribute("content", "grouptotals");
		return "index";
	}

	@PostMapping("/grouptotals/totals") 
	public String groupTotalsPost(@RequestParam String month, String year, Model model, RedirectAttributes rm)
	{
		User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
		List<GroupedGroup> groupedGroups = bookService.bookGroupByGroupMonth(user.getUserID(),month,year);
		Collections.sort(groupedGroups);
		model.addAttribute("content", "grouptotals");
		model.addAttribute("month_long",Month.getMonthByStringNumber(month));
		model.addAttribute("month",month);
		model.addAttribute("currency",user.getCurrency().getCurrencySymbol());
		model.addAttribute("years",bookService.getYears(user.getUserID())); // Dropdown filter
		model.addAttribute("year",year); // Dropdown selected option
		model.addAttribute("groupedGroups", groupedGroups);
		return "index";
	}

	@PostMapping("/group/update") 
	public String updateGroupPost(@RequestParam Long groupID, @RequestParam String groupName, Boolean delete, Model model, RedirectAttributes rm)
	{
		User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
		if(delete==null) // avoid error Cannot invoke "java.lang.Boolean.booleanValue()" because "delete" is null
		{
			delete=false;
		}

		if(delete)
		{
			if(!categoryService.categoryHasGroup(groupID, user.geteMail()))
			{
				try
				{
					groupService.deleteGroup(groupService.findGroupByGroupID(groupID));
					model.addAttribute("content", "group");
					rm.addFlashAttribute("message","Group succesfully deleted");
					return "redirect:/group";
				}
				catch (Exception e) {
					model.addAttribute("error", e.getMessage());
					return "redirect:/book";
				}
			}
			else
			{
				model.addAttribute("content", "group");
				rm.addFlashAttribute("message","Not deleted because group has one or more categories");
				return "redirect:/group";
			}
		}
		else
		{
			if(groupName!="")
			{
				String checkDuplicat = giveDuplicateIfExist(groupName);
				if(!groupName.equalsIgnoreCase(checkDuplicat))
				{
					try
					{
						groupService.updateGroup(groupID,groupName,user);
						model.addAttribute("content", "group");
						rm.addFlashAttribute("message","Information succesfully updated");
						return "redirect:/group";
					}
					catch (Exception e) {
						model.addAttribute("error", e.getMessage());
						return "redirect:/group";
					}
				}
				else
				{
					model.addAttribute("content", "group");
					return "redirect:/group";
				}
			}
			else
			{
				model.addAttribute("content", "group");
				rm.addFlashAttribute("message","Fill in a group name");
				return "redirect:/group";
			}	
		}
	}

	@PostMapping("/group/add") 
	public String addGroupPost(@RequestParam String groupName, Model model, RedirectAttributes rm)
	{
		User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
		if(groupService.getGroups().size()<50) // Max amount groups per user
		{
			if(groupName!="")
			{
				String checkDuplicat = giveDuplicateIfExist(groupName);
				if(!groupName.equalsIgnoreCase(checkDuplicat))
				{
					try
					{
						groupService.addGroup(groupName,user);
						model.addAttribute("content", "group");
						rm.addFlashAttribute("message","Group succesfully added");
						return "redirect:/group";
					}
					catch (Exception e) {
						model.addAttribute("error", e.getMessage());
						return "redirect:/group";
					}
				}
				else
				{
					model.addAttribute("content", "group");
					rm.addFlashAttribute("message","Group already exist");
					return "redirect:/group";
				}
			}
			else
			{
				model.addAttribute("content", "group");
				rm.addFlashAttribute("message","Fill in a group name");
				return "redirect:/group";
			}
		}
		else
		{
			model.addAttribute("content", "group");
			rm.addFlashAttribute("message","Max amount groups reached");
			return "redirect:/group";
		}
	}

	public String giveDuplicateIfExist(String groupName)
	{
		User user=userService.findUserByeMail(userDetails.getUsername()); // Get user information
		// Check if groupName already exist
		String checkDuplicat = null;
		if(groupService.findGroupByGroupName(groupName,user)!=null)
		{
			checkDuplicat = groupService.findGroupByGroupName(groupName, user).getGroupName().toString();
		}
		return checkDuplicat;
	}

}
