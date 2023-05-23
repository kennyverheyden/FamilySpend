package be.kennyverheyden.services;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import be.kennyverheyden.models.User;
import be.kennyverheyden.models.UserRole;
import be.kennyverheyden.processors.UserDetailsImpl;
import be.kennyverheyden.repositories.UserRepository;
import be.kennyverheyden.repositories.UserRoleRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class UserService implements UserDetailsService{

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  UserRoleRepository userRoleRepository;
	@Autowired
	private  GroupService groupService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private UserDetailsImpl userDetails;

	private  PasswordEncoder passwordEncoder;


	public UserService() {
		this.passwordEncoder =  new BCryptPasswordEncoder();
	}

	// Part of Spring security
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException(username);
		}
		else
		{
			userDetails.setUser(user);
		}
		return userDetails;
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

	public boolean userExist(String userEmail)
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

	// Change password
	public void updateSecret(User user, String secret) {
		String encodedPassword = this.passwordEncoder.encode(secret);
		user.setSecret(encodedPassword);
		userRepository.save(user);
	}

	// User registration - create user and set inactive until verification mail is send
	public void register(String userEmail, String secret, String name, String firstName, Long role, Long currencyID, String siteURL)
			throws UnsupportedEncodingException, MessagingException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		User user = new User();
		user.seteMail(userEmail); // = username
		String encodedPassword = this.passwordEncoder.encode(secret);
		user.setSecret(encodedPassword);
		secret=null;
		user.setName(name);
		user.setFirstName(firstName);
		user.setUserRole(userRoleRepository.findById(role).get());
		user.setCreation(dtf.format(now));
		user.setCurrency(currencyService.findCurrencyByCurrencyID(currencyID));
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(0);
		userRepository.findAll().add(user);
		userRepository.save(user);
		groupService.createGroupSampleData(user);
		categoryService.createCategorySampleData(user);
		sendVerificationEmail(user, siteURL);
	}

	// Send verification mail - user registration
	private void sendVerificationEmail(User user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.geteMail();
		String fromAddress = "familyspend@kennyverheyden.be";
		String senderName = "FamilySpend";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>"
				+ "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
				+ "Thank you,<br>"
				+ "FamilySpend.";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", user.getFirstName());
		String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);
	}

	// Verify verification code
	public boolean verify(String verificationCode) {
		User user = userRepository.findByVerificationCode(verificationCode);

		if (user == null || user.isEnabled()==1) {
			return false;
		} else {
			user.setVerificationCode(null);
			user.setEnabled(1);
			userRepository.save(user);
			return true;
		}
	}

	public void updateUser(User user) {
		userRepository.save(user);
	}

	// Delete the user and all his content in the DB
	public void deleteUser(String userEmail)
	{
		User user = this.findUserByeMail(userEmail);
		userRepository.deleteBookingsFromUser(user.getUserID());
		userRepository.deleteCategoriesFromUser(user.getUserID());
		userRepository.deleteGroupsFromUser(user.getUserID());
		userRepository.delete(user);
	}

	public void deleteUserByAdmin(String userEmail)
	{
		User user = this.findUserByeMail(userEmail);
		userRepository.deleteBookingsFromUser(user.getUserID());
		userRepository.deleteCategoriesFromUser(user.getUserID());
		userRepository.deleteGroupsFromUser(user.getUserID());
		userRepository.delete(user);
	}

	// Update account by user
	public void updateAccount(String email, String name, String firstname, Long currencyID) {
		User user = this.findUserByeMail(email);
		user.setName(name);
		user.setFirstName(firstname);
		user.setCurrency(currencyService.findCurrencyByCurrencyID(currencyID));
		userRepository.save(user);
	}

	// Update by admin
	public void updateUser(String eMail, String name, String firstname, String secret, String userRole, Long currencyID, int enable) {
		// Find user role name
		User user = this.findUserByeMail(eMail);
		user.setName(name);
		user.setFirstName(firstname);
		user.seteMail(eMail);
		user.setCurrency(currencyService.findCurrencyByCurrencyID(currencyID));
		if(!secret.equals(""))
		{
			String encodedPassword = this.passwordEncoder.encode(secret);
			user.setSecret(encodedPassword);
		}
		user.setUserRole(userRoleRepository.findById(findbyRoleName(userRole)).get()); // Set userRole
		user.setVerificationCode(null);
		user.setResetPasswordToken(null);
		user.setEnabled(enable); // 0 or 1
		userRepository.save(user);
	}

	public void updateResetPasswordToken(String token, String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			user.setResetPasswordToken(token);
			userRepository.save(user);
		} 
	} 

	public User getByResetPasswordToken(String token) {
		return userRepository.findByResetPasswordToken(token);
	}

	public void updatePassword(User user, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setSecret(encodedPassword);
		user.setResetPasswordToken(null);
		user.setEnabled(1); // Enable account, when account is blocked after max amount failed login attempts
		userRepository.save(user);
	}

	// If the user never used the reset password link
	public void clearToken(User user)
	{
		if(user!=null)
		{
			user.setResetPasswordToken(null);
			userRepository.save(user);
		}
	}

}
