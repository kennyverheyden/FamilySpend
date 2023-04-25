package be.kennyverheyden.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import be.kennyverheyden.models.Group;
import be.kennyverheyden.models.GroupedGroup;
import be.kennyverheyden.models.Month;
import be.kennyverheyden.services.BookService;
import be.kennyverheyden.services.CategoryService;
import be.kennyverheyden.services.GroupService;
import be.kennyverheyden.services.UserService;


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

	Date date = new Date();
	String monthDateFormat = "MM";
	String monthDateFormat_long = "MMMM";
	SimpleDateFormat mdf = new SimpleDateFormat(monthDateFormat);
	String month = mdf.format(date);
	SimpleDateFormat mdf_long = new SimpleDateFormat(monthDateFormat_long);
	String month_long = StringUtils.capitalize(mdf_long.format(date));
	
	public GroupController() {}

	@GetMapping("/group")
	public String groupGet(Model model)
	{

		String userEmail = userService.getUserEmail();
		//		// When user is not logged on, the String is null

		if(userEmail==null)
		{
			model.addAttribute("content", "login");
			return "redirect:/";
		}
		groupService.loadGroups(userService.findUserByeMail(userService.getUserEmail())); // Collect and load groups from specific user
		model.addAttribute("groups",groupService.findGroupByUserUserID(userService.findUserByeMail(userEmail).getUserID()));
		model.addAttribute("content", "group");
		return "index";
	}

	@GetMapping("/grouptotals")
	public String groupTotalsGet(Model model)
	{
		String userEmail = userService.getUserEmail();
		// When user is not logged on, the String is null

		if(userEmail==null)
		{
			model.addAttribute("content", "login");
			return "redirect:/";
		}

		// Get group by groups and totals
		Long userID=userService.findUserByeMail(userService.getUserEmail()).getUserID();
		List<GroupedGroup> groupedGroups = bookService.bookGroupByGroupMonth(userID,month);
		model.addAttribute("groupedGroups", groupedGroups);
		model.addAttribute("month_long",month_long);
		model.addAttribute("month", month);
		model.addAttribute("content", "grouptotals");
		return "index";
	}

	@PostMapping("/grouptotals/totals") 
	public String groupTotalsPost(@RequestParam String month, Model model, RedirectAttributes rm)
	{
		Long userID=userService.findUserByeMail(userService.getUserEmail()).getUserID();
		List<GroupedGroup> groupedGroups = bookService.bookGroupByGroupMonth(userID,month);
		model.addAttribute("content", "grouptotals");
		model.addAttribute("month_long",Month.getMonthByStringNumber(month));
		model.addAttribute("month",month);
		model.addAttribute("groupedGroups", groupedGroups);
		return "index";
	}

	@PostMapping("/group/update") 
	public String updateGroupPost(@RequestParam Long groupID, @RequestParam String groupName, Boolean delete, Model model, RedirectAttributes rm)
	{
		if(delete==null) // avoid error Cannot invoke "java.lang.Boolean.booleanValue()" because "delete" is null
		{
			delete=false;
		}

		if(delete)
		{
			if(!categoryService.categoryHasGroup(groupID, userService.getUserEmail()))
			{
				groupService.deleteGroup(groupService.findGroupByGroupID(groupID));
				model.addAttribute("content", "group");
				rm.addFlashAttribute("message","Group succesfully deleted");
				return "redirect:/group";
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
					groupService.updateGroup(groupID,groupName,userService.findUserByeMail(userService.getUserEmail()));
					model.addAttribute("content", "group");
					rm.addFlashAttribute("message","Information succesfully updated");
					return "redirect:/group";
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
		if(groupService.getGroups().size()<50) // Max amount groups per user
		{
			if(groupName!="")
			{
				String checkDuplicat = giveDuplicateIfExist(groupName);
				if(!groupName.equalsIgnoreCase(checkDuplicat))
				{
					groupService.addGroup(groupName,userService.findUserByeMail(userService.getUserEmail()));
					model.addAttribute("content", "group");
					rm.addFlashAttribute("message","Group succesfully added");
					return "redirect:/group";
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
		// Check if groupName already exist
		String checkDuplicat = null;
		if(groupService.findGroupByGroupName(groupName, userService.findUserByeMail(userService.getUserEmail()))!=null)
		{
			checkDuplicat = groupService.findGroupByGroupName(groupName, userService.findUserByeMail(userService.getUserEmail())).getGroupName().toString();
		}
		return checkDuplicat;
	}

}
