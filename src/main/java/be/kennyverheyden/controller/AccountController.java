package be.kennyverheyden.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.User;
import be.kennyverheyden.processors.UserDetailsImpl;
import be.kennyverheyden.services.CurrencyService;
import be.kennyverheyden.services.UserService;
import jakarta.servlet.http.HttpServletRequest;

@SessionScope
@Controller
public class AccountController {

	@Autowired
	private UserService userService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private UserDetailsImpl userDetails;

	public AccountController() {}


	@GetMapping("/account") // get request
	public String accountGet(Model model) {

		User user = userService.findUserByeMail(userDetails.getUsername());
		model.addAttribute("content", "account");
		model.addAttribute("email",user.geteMail());  // Map content to html elements
		model.addAttribute("firstName",user.getFirstName());  
		model.addAttribute("name",user.getName());
		model.addAttribute("currencies",currencyService.findAllCurrencies()); // Option for currency
		model.addAttribute("currencyID",user.getCurrency().getCurrencyID());
		return "index";
	}

	@PostMapping("/account") 
	public String updateAccountPost(@RequestParam (required = false) String email, @RequestParam (required = false) String name, @RequestParam (required = false) Long currencyFK, @RequestParam (required = false) String firstName, Model model, RedirectAttributes rm){

		if(!name.equals("") && !firstName.equals(""))
		{
			// Update user
			try
			{
				userService.updateAccount(email, name, firstName, currencyFK);
				model.addAttribute("content", "account");
				rm.addFlashAttribute("message","Information succesfully updated");
				return "redirect:account";
			}
			catch (Exception e) {
				model.addAttribute("error", e.getMessage());
				return "redirect:/book";
			}
		}
		else
		{
			// Check if all fields are filled in
			model.addAttribute("content", "account");
			rm.addFlashAttribute("message","Fill in all fields");
			return "redirect:/account";
		}
	}

	@PostMapping("/account/delete") 
	public String deleteAccountPost(Model model, RedirectAttributes rm, HttpServletRequest request){
		if(!userDetails.getUsername().equals("test@test.com"))
		{
			try
			{
				userService.deleteUser(userDetails.getUsername());
				request.logout();
				model.addAttribute("content", "account");
				return "redirect:/login";
			}
			catch (Exception e) {
				model.addAttribute("error", e.getMessage());
				return "redirect:/account";
			}
		}
		else
		{
			model.addAttribute("content", "account");
			rm.addFlashAttribute("message","You cannot delete the demo account");
			return "redirect:/account";
		}
	}

}