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
public class PasswordResetController {

	@Autowired
	private UserService userService;

	public PasswordResetController() {}

	@GetMapping("/passreset") // get request
	public String passResetGet(Model model) {

		// When user is not logged on, the String is null
		if(userService.getUserEmail()==null)
		{
			model.addAttribute("content", "login");
			return "index";
		}

		User user = userService.findUserByeMail(userService.getUserEmail());
		model.addAttribute("userEmail",user.geteMail());  // map content to html elements
		model.addAttribute("content", "passreset"); 

		return "index";
	}

	@PostMapping("/passreset") 
	public String passResetPost(@RequestParam (required = false) String userEmail, @RequestParam (required = false) String oldSecret, @RequestParam (required = false) String secret, @RequestParam (required = false) String confirmSecret, Model model, RedirectAttributes rm){

		if(userService.getSecret().equals(oldSecret))
		{		
			if(!secret.equals("") && !confirmSecret.equals(""))
			{
				if(secret.equals(confirmSecret))
				{
					userService.updateSecret(userEmail, confirmSecret);
					model.addAttribute("content", "passreset");
					rm.addFlashAttribute("message","Password succesfully changed");
					return "redirect:passreset";
				}
				else
				{
					model.addAttribute("content", "passwordreset");
					rm.addFlashAttribute("message","Password not the same as confirmation password");
					return "redirect:passreset";
				}
			}
			else
			{
				// Check if all fields are filled in
				model.addAttribute("content", "passreset");
				rm.addFlashAttribute("message","Fill in all fields");
				return "redirect:passreset";
			}
		}
		else
		{
			// Check if all fields are filled in
			model.addAttribute("content", "passreset");
			rm.addFlashAttribute("message","Your old password is not correct");
			return "redirect:passreset";
		}

	}

}