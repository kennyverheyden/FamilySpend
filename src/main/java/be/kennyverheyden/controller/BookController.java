package be.kennyverheyden.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import be.kennyverheyden.models.User;
import be.kennyverheyden.services.BookService;
import be.kennyverheyden.services.CategoryService;
import be.kennyverheyden.services.UserService;


@Controller
public class BookController {

	@Autowired
	private BookService bookService;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;

	public BookController() {}

	@GetMapping("/book")
	public String bookGet(Model model)
	{

		String userEmail = userService.getUserEmail();
		//	When user is not logged on, the String is null

		if(userEmail==null)
		{
			model.addAttribute("content", "login");
			return "redirect:/";
		}
		User user=userService.findUserByeMail(userService.getUserEmail()); // Get user information
		bookService.loadBooks(user);
		model.addAttribute("books",bookService.findBookByUserUserID(user.getUserID())); // Read bookings to html
		model.addAttribute("categories",categoryService.findCategoryByUserUserID(user.getUserID())); // Read categories
		model.addAttribute("content", "book");
		return "index";
	}

	@PostMapping("/book/update")
	public String bookUpdatePost(@RequestParam (required = false) Long bookID, @RequestParam (required = false) String date, @RequestParam (required = false) float amount, @RequestParam (required = false) String description, @RequestParam (required = false) String categoryName, Model model, RedirectAttributes rm)
	{
		if(date!="" || amount!=0 || description!="" || categoryName!="")
		{
			User user=userService.findUserByeMail(userService.getUserEmail()); // Get user information
			bookService.updateBook(bookID, date, amount, description, categoryName, user);
			model.addAttribute("content", "book");
			rm.addFlashAttribute("message","Booking succesfully updated");
			return "redirect:/book";
		}
		else
		{
			model.addAttribute("content", "book");
			rm.addFlashAttribute("message","Fill in all fields");
			return "redirect:/group";
		}
	}

	@PostMapping("/book/add")
	public String bookAddPost(@RequestParam (required = false) float amount, @RequestParam (required = false) String description, @RequestParam (required = false) String categoryName, Model model, RedirectAttributes rm)
	{
	//	System.out.println(amount + " " + description);
		if(amount!=0 || description!="")
		{
			User user=userService.findUserByeMail(userService.getUserEmail()); // Get user information
			bookService.addBook(amount, description, categoryName, user);
			model.addAttribute("content", "book");
			rm.addFlashAttribute("message","Booking succesfully updated");
			return "redirect:/book";
		}
		else
		{
			model.addAttribute("content", "book");
			rm.addFlashAttribute("message","Fill in all fields");
			return "redirect:/book";
		}
	}
	
	@PostMapping("/book/delete") 
	public String deleteGroupPost(@RequestParam Long bookID, Model model, RedirectAttributes rm)
	{
			bookService.deleteBook(bookService.findBookBybookID(bookID));
			model.addAttribute("content", "book");
			rm.addFlashAttribute("message","Booking succesfully deleted");
			return "redirect:/book";
	}



}
