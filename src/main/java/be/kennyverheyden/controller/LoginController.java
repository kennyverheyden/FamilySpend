package be.kennyverheyden.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.User;
import be.kennyverheyden.processors.LoginProcessor;
import be.kennyverheyden.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController{

	@Autowired
	private LoginProcessor loginProcessor;
	@Autowired
	private UserService userService;


	int maxLoginAttempts=5;
	List<String> userEmails = new ArrayList(); // Store failed login attempts same userEmail

	public LoginController() {}

	@GetMapping("/login") // get request
	public String loginGet(Model model) {
		model.addAttribute("content", "login");
		return "index";
	}

	@PostMapping("/login/submit") 
	public String loginPost(@RequestParam String userEmail, @RequestParam String secret, Model model, RedirectAttributes rm,  HttpServletRequest request) throws ServletException {
		boolean loggedIn = false;

		try {
			loggedIn = loginProcessor.login(userEmail, secret);
		} catch (Exception e) {
			int loginAttempts=0; // Count failed logins in the list
			userEmails.add(userEmail); // Add to failed login list
			for(int i=0;i<userEmails.size();i++) // Check to list
			{
				if(userEmails.get(i).equalsIgnoreCase(userEmail));
				{
					loginAttempts++;
				}
			}
			if(loginAttempts==maxLoginAttempts)
			{
				User user = userService.findUserByeMail(userEmail);
				// If user exist with given userName (userEmail)
				// Admin account can not disabled
				if(user!=null && user.getUserRole().getRoleID()!= 1)
				{
					user.setEnabled(0); // Int 0 = false
					userService.updateUser(user);
				}
			}
			model.addAttribute("content", "login");
			rm.addFlashAttribute("message","Your credentials are incorrect");
			return "redirect:/login";
		}

		if(loggedIn == true)
		{
			User user = userService.findUserByeMail(userEmail);
			if(user.isEnabled()!=0)
			{
				userService.clearToken(userService.findUserByeMail(userEmail)); // Clear password reset (in mail) token in case never used
				userEmails.clear();
				model.addAttribute("content", "main");
				return "redirect:/main";
			}
			else
			{
				request.logout();
				model.addAttribute("content", "login");
				rm.addFlashAttribute("message","Account inactive or blocked, please contact the admin");
				return "redirect:/login";
			}
		}
		model.addAttribute("content", "login");
		rm.addFlashAttribute("message","Your credentials are incorrect");
		return "redirect:/login";
	}

}