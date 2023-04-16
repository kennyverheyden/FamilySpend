package be.kennyverheyden.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.Group;
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

	@PostMapping("/group/update") 
	public String updateGroupPost(@RequestParam Long groupID, @RequestParam String groupName, Model model, RedirectAttributes rm)
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

	@PostMapping("/group/delete") 
	public String deleteGroupPost(@RequestParam Long groupID, Model model, RedirectAttributes rm)
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
