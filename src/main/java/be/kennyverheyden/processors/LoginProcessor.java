package be.kennyverheyden.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import be.kennyverheyden.models.User;
import be.kennyverheyden.repositories.UserRepository;
import be.kennyverheyden.services.UserService;


@Component
@RequestScope
public class LoginProcessor {

	private String userEmail;
	private String secret;

	@Autowired
	private UserService userService;

	private  PasswordEncoder passwordEncoder;

	public LoginProcessor()
	{
		this.passwordEncoder =  new BCryptPasswordEncoder();
	}

	public boolean login()
	{
		// Session scope bean, userEmail must be available
		userService.setUserEmail(userEmail);
		userService.setSecret(secret);

		// Check if userMail and password exist
		for(User user:userService.findAll())
		{
			if(user.geteMail().equals(userService.getUserEmail()) && passwordEncoder.matches(userService.getSecret(),user.getSecret()))
			{
				userService.clearToken(user); // // If the user never used the reset password link
				return true;
			}
		}
		userService.setUserEmail(null);
		userService.setSecret(null);
		return false;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
