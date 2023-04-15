package be.kennyverheyden.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.services.GroupService;
import be.kennyverheyden.services.UserService;


@Controller
public class GroupController {

	@Autowired
	private GroupService groupService;
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
		model.addAttribute("groups",groupService.findGroupByUserUserID(userService.findUserByeMail(userEmail).getUserID()));
		model.addAttribute("content", "group");
		return "index";
	}
	
	@PostMapping("/group/update") 
	public String updateGroupPost(@RequestParam Long groupID, @RequestParam String groupName, Model model, RedirectAttributes rm)
	{
		groupService.updateGroup(groupID,groupName,userService.findUserByeMail(userService.getUserEmail()).getUserID());
		model.addAttribute("content", "group");
		rm.addFlashAttribute("message","Information succesfully updated");
		return "redirect:/group";
	}

}
