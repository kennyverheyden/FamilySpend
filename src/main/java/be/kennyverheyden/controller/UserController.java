package be.kennyverheyden.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.User;
import be.kennyverheyden.models.UserRole;
import be.kennyverheyden.processors.UserDetailsImpl;
import be.kennyverheyden.services.CurrencyService;
import be.kennyverheyden.services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private UserDetailsImpl userDetails;

	public UserController() {}

	@GetMapping("/admin") // get request
	public String usersGet(Model model) {
		List<UserRole> roles = userService.findUserRoles();
		model.addAttribute("roles",roles);  // map content to html elements
		model.addAttribute("content", "admin");
		return "index";
	}

	// Admin can search for users
	@PostMapping("/admin/find") 
	public String findUsersPost(@RequestParam (required = false) String email, @RequestParam (required = false) String name, @RequestParam (required = false) String firstName, @RequestParam (required = false) String roleName, Model model, RedirectAttributes rm){

		List<User> foundUsers = userService.findUsers(email, name, firstName, roleName);
		List<UserRole> roles = userService.findUserRoles(); // List is used for dropdown select box
		model.addAttribute("roles",roles);  // map content to html elements
		model.addAttribute("users",foundUsers);  // map content to html elements
		model.addAttribute("currencies",currencyService.findAllCurrencies());
		model.addAttribute("content", "admin"); 
		return "index";
	}

	// Admin can edit users
	@PostMapping("/admin/users") 
	public String updateUserPost(@RequestParam (required = false) String email, @RequestParam (required = false) String name, @RequestParam (required = false) String firstName, @RequestParam (required = false) String secret, @RequestParam (required = false) String userRole, @RequestParam (required = false) Long currencyFK, @RequestParam (required = false) Integer enable, @RequestParam (required = false) Boolean delete, Model model, RedirectAttributes rm){

		if(delete==null) // Avoid error Cannot invoke "java.lang.Boolean.booleanValue()" because "delete" is null
		{
			delete=false;
		}
		if(enable==null) // Could be empty, because isEnable is disabled for own admin account
		{
			enable=1;
		}

		if(delete)
		{
			if(email.equals(userDetails.getUsername())) // You cannot delete your own admin account
			{
				model.addAttribute("content", "admin");
				rm.addFlashAttribute("message","You cannot delete your own account");
				return "redirect:/admin";
			}
			else
			{
				if(email.equals("kenny.verheyden@gmail.com")) // You cannot delete the primary admin account
				{
					model.addAttribute("content", "admin");
					rm.addFlashAttribute("message","You cannot delete the primary admin account");
					return "redirect:/admin";
				}
				else
				{
					userService.deleteUserByAdmin(email);
					model.addAttribute("content", "admin");
					rm.addFlashAttribute("message","User deleted");
					return "redirect:/admin";
				}
			}
		}
		else
		{
			if(!name.equals("") && !firstName.equals(""))
			{
				if(email.equals("kenny.verheyden@gmail.com") && !userRole.equals("Admin")) {

					model.addAttribute("content", "admin");
					rm.addFlashAttribute("message","You cannot change the role of the primary admin account");
					return "redirect:/admin";
				}
				else
				{
					// Update user
					userService.updateUser(email, name, firstName, secret, userRole, currencyFK, enable);
					model.addAttribute("content", "admin");
					rm.addFlashAttribute("message","Information succesfully updated");
					return "redirect:/admin";
				}
			}
			else
			{
				// Check if all fields are filled in
				model.addAttribute("content", "admin");
				rm.addFlashAttribute("message","Fill in all fields");
				return "redirect:/admin";
			}
		}
	}

}
