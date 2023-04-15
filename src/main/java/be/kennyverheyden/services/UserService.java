package be.kennyverheyden.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import be.kennyverheyden.models.User;
import be.kennyverheyden.repositories.UserRepository;
import be.kennyverheyden.repositories.UserRoleRepository;
import jakarta.transaction.Transactional;


@Service
@Transactional 
public class UserService{

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  UserRoleRepository userRoleRepository;
	@Autowired
	private  CategoryService categoryService;
	@Autowired
	private  GroupService groupService;
	
	private  PasswordEncoder passwordEncoder;

	private String userEmail;
	private String secret;

	public UserService() {
		this.passwordEncoder =  new BCryptPasswordEncoder();
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findUserByeMail(String eMail)
	{
		return userRepository.findUserByeMail(eMail);
	}

	public boolean userExist(String username)
	{
		for (User user:userRepository.findAll())
		{
			if(user.geteMail().equals(userEmail))
			{
				return true;
			}
		}
		return false;
	}

	public void updateSecret(String userEmail, String secret) {
		User user = this.findUserByeMail(userEmail);
		String encodedPassword = this.passwordEncoder.encode(secret);
		user.setSecret(encodedPassword);
		userRepository.save(user);
	}

	// create user
	// set template content
	public void signupUser(String userEmail, String secret, String name, String firstName, Long role) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		User user = new User();
		user.seteMail(userEmail); // = username
		String encodedPassword = this.passwordEncoder.encode(secret);
		user.setSecret(encodedPassword);
		user.setName(name);
		user.setFirstName(firstName);
		user.setUserRole(userRoleRepository.findById(role).get());
		user.setCreation(dtf.format(now));
		userRepository.findAll().add(user);
		userRepository.save(user);
		groupService.createGroupSampleData(user);
		categoryService.createCategorySampleData(user);
	}

	public void deleteUser(String userEmail)
	{
		User user = this.findUserByeMail(userEmail);
		userRepository.delete(user);
	}

	// Getters and Setters

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

}
