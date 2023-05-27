package be.kennyverheyden.controller;

import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.mail.javamail.JavaMailSender;

import be.kennyverheyden.models.User;
import be.kennyverheyden.processors.Utility;
import be.kennyverheyden.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;

@SessionScope
@Controller
public class ForgotPasswordController {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserService userService;

	public ForgotPasswordController () {}

	@GetMapping("/forgot_password")
	public String showForgotPasswordForm(Model model) {
		model.addAttribute("content", "forgot_password_form");
		return "index";
	}

	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request, Model model, RedirectAttributes rm) {
		String email = request.getParameter("email");
		String token = RandomString.make(30);

		try {
			userService.updateResetPasswordToken(token, email);
			String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
			this.sendEmail(email, resetPasswordLink);
			model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}

		model.addAttribute("content", "forgot_password_form");
		return "index";
	}

	public void sendEmail(String recipientEmail, String link)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();              
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("familyspend@kennyverheyden.be", "Kenny Verheyden");
		helper.setTo(recipientEmail);

		String subject = "Here's the link to reset your password";

		String content = "<p>Hello,</p>"
				+ "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>"
				+ "<p><a href=\"" + link + "\">Change my password</a></p>"
				+ "<br>"
				+ "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}

	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
		User user = userService.getByResetPasswordToken(token);
		model.addAttribute("token", token);

		if (user == null) {
			model.addAttribute("message", "Invalid Token");
			return "message";
		}
		model.addAttribute("content", "reset_password_form");
		return "index";
	}

	@PostMapping("/reset_password")
	public String processResetPassword(HttpServletRequest request, Model model, RedirectAttributes rm) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");

		User user = userService.getByResetPasswordToken(token);
		model.addAttribute("title", "Reset your password");

		if (user == null) {
			rm.addFlashAttribute("message","You have successfully changed your password.");
		} else {           
			userService.updatePassword(user, password);
			rm.addFlashAttribute("message","You have successfully changed your password.");
		}
		model.addAttribute("content", "forgot_password_reset_output");
		return "redirect:/forgot_password_reset_output";
	}

	@GetMapping("/forgot_password_reset_output")
	public String resetPasswordOutputGet(Model model) {
		model.addAttribute("content", "forgot_password_reset_output");
		return "index";
	}
}