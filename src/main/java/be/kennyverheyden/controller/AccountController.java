package be.kennyverheyden.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.User;
import be.kennyverheyden.services.UserService;

@Controller
public class AccountController {

	@Autowired
	private UserService userService;

	public AccountController() {}


	@GetMapping("/account") // get request
	public String selectGet(Model model) {


		if(userService.getUserEmail()==null)
		{
			model.addAttribute("content", "login");
			return "index";
		}

		User user = userService.findUserByeMail(userService.getUserEmail());

		model.addAttribute("content", "account");
		model.addAttribute("email",user.geteMail());  // map content to html elements
		model.addAttribute("firstName",user.getFirstName());  
		model.addAttribute("name",user.getName());
		return "index";
	}

	@PostMapping("/account") 
	public String updateAccount(@RequestParam (required = false) String email, @RequestParam (required = false) String name, @RequestParam (required = false) String firstName, Model model, RedirectAttributes rm){

		if(!name.equals("") && !firstName.equals(""))
		{
			// Update user
			userService.updateAccount(email, name, firstName);
			model.addAttribute("content", "account");
			rm.addFlashAttribute("message","Information succesfully updated");
			return "redirect:account";
		}
		else
		{
			// Check if all fields are filled in
			model.addAttribute("content", "account");
			rm.addFlashAttribute("message","Fill in all fields");
			return "redirect:account";
		}
	}

}