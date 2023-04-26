package be.kennyverheyden.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import be.kennyverheyden.models.User;
import be.kennyverheyden.models.UserRole;
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

	public List<UserRole> findUserRoles() {
		return userRoleRepository.findAll();
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

	// Used for searching users by admin // eMail = username
	public List<User> findUsers(String eMail, String name, String firstName, String roleName)
	{
		System.out.println(eMail);
		System.out.println(name);
		System.out.println(firstName);
		System.out.println(roleName);
		List<User> userList = new ArrayList<>();
		for (User i:userRepository.findAll())
		{
			// Search with rolename and username(email)
			if(roleName!="" && i.getUserRole().getRoleName().toString().equalsIgnoreCase(roleName) && i.geteMail().toString().equalsIgnoreCase(eMail) && name=="" && firstName=="")
			{
				userList.add(i);
			}
			// Search with rolename and username(email) and name
			else if(roleName!="" && i.getUserRole().getRoleName().toString().equalsIgnoreCase(roleName) && i.geteMail().toString().equalsIgnoreCase(eMail) && i.getName().toString().equalsIgnoreCase(name)  && firstName=="")
			{
				userList.add(i);
			}
			// Search with rolename and username(email) and firstname
			else if(roleName!="" && i.getUserRole().getRoleName().toString().equalsIgnoreCase(roleName) && i.geteMail().toString().equalsIgnoreCase(eMail) && i.getFirstName().toString().equalsIgnoreCase(firstName)  && name=="")
			{
				userList.add(i);
			}
			// Search with rolename and name
			else if(roleName!="" && i.getUserRole().getRoleName().toString().equalsIgnoreCase(roleName) && i.getName().toString().equalsIgnoreCase(name) && eMail=="" && firstName=="")
			{
				userList.add(i);
			}
			// Search with rolename and firstname
			else if(roleName!="" && i.getUserRole().getRoleName().toString().equalsIgnoreCase(roleName) && i.getFirstName().toString().equalsIgnoreCase(firstName) && eMail=="" && name=="")
			{
				userList.add(i);
			}
			// Search with rolename and firstname and name
			else if(roleName!="" && i.getUserRole().getRoleName().toString().equalsIgnoreCase(roleName) && i.getFirstName().toString().equalsIgnoreCase(firstName) && i.getName().toString().equalsIgnoreCase(name) && eMail=="")
			{
				userList.add(i);
			}
			// Search with rolename and firstname and name and username(email)
			else if(roleName!="" && i.getUserRole().getRoleName().toString().equalsIgnoreCase(roleName) && i.getFirstName().toString().equalsIgnoreCase(firstName) && i.getName().toString().equalsIgnoreCase(name) && i.geteMail().toString().equalsIgnoreCase(eMail))
			{
				userList.add(i);
			}
			// Search with rolename without username(email) and without name and without firstname
			else if(roleName!="" &&  i.getUserRole().getRoleName().toString().equalsIgnoreCase(roleName) && eMail=="" && name=="" && firstName=="")
			{
				userList.add(i);
			}
			else
			{
				// Search without rolename
				if(roleName=="")
				{
					// Search with username(email) and name and firstname without rolename
					if(i.geteMail().toString().equalsIgnoreCase(eMail) && i.getName().toString().equalsIgnoreCase(name) && i.getFirstName().toString().equalsIgnoreCase(firstName)) 
					{
						userList.add(i);
					}
					// Search with username(email) and name without firstname and without rolename
					if(i.geteMail().toString().equalsIgnoreCase(eMail) && i.getName().toString().equalsIgnoreCase(name) && firstName=="") 
					{
						userList.add(i);
					}
					// Search with username(email) and firstname without name and without rolename
					else if(i.geteMail().toString().equalsIgnoreCase(eMail) && i.getFirstName().toString().equalsIgnoreCase(firstName) && name=="")
					{
						userList.add(i);
					}
					// Search with firstname without username(email) and without name and without role
					else if(i.getFirstName().toString().equalsIgnoreCase(firstName) && eMail=="" && name=="")
					{
						userList.add(i);
					}
					// Search with name without username(email) and without firstname and without role
					else if(i.getName().toString().equalsIgnoreCase(name) && eMail=="" && firstName=="")
					{
						userList.add(i);
					}
					// Search with name and firstname without username(email) and without role
					else if(i.getName().toString().equalsIgnoreCase(name) && i.getFirstName().toString().equalsIgnoreCase(firstName) && eMail=="")
					{
						userList.add(i);
					}
					// Search with username(email) without name and without firstname and without role
					else if(i.geteMail().toString().equalsIgnoreCase(eMail) && name=="" && firstName=="")
					{
						userList.add(i);
					}
				}
			}
		}
		return userList;
	}

	public long findbyRoleName(String roleName)
	{
		for (UserRole i:userRoleRepository.findAll())
		{
			if(i.getRoleName().equals(roleName))
			{
				return i.getRoleID();
			}
		}
		return 0;
	}

	public void updateSecret(String userEmail, String secret) {
		User user = this.findUserByeMail(userEmail);
		String encodedPassword = this.passwordEncoder.encode(secret);
		user.setSecret(encodedPassword);
		userRepository.save(user);
	}

	// Create user
	// Set template content
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

	// Update account by user
	public void updateAccount(String email, String name, String firstname) {
		User user = this.findUserByeMail(email);
		user.setName(name);
		user.setFirstName(firstname);
		userRepository.save(user);
	}

	// Update by admin
	public void updateUser(String eMail, String name, String firstname, String secret, String userRole) {
		// Find user role name
		User user = this.findUserByeMail(eMail);
		user.setName(name);
		user.setFirstName(firstname);
		user.seteMail(eMail);
		if(!secret.equals(""))
		{
			String encodedPassword = this.passwordEncoder.encode(secret);
			user.setSecret(encodedPassword);
		}
		user.setUserRole(userRoleRepository.findById(findbyRoleName(userRole)).get()); // Set userRole
		userRepository.save(user);
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
