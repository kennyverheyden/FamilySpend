package be.kennyverheyden.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.kennyverheyden.models.Book;
import be.kennyverheyden.models.Category;
import be.kennyverheyden.models.User;
import be.kennyverheyden.repositories.BookRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;

	private List<Book> books;

	public BookService() {}

	public List<Book> findBookByUserUserID (Long userID) {
		return bookRepository.findBookByUserUserID(userID);
	}

	public Book findBookBybookID (Long bookID) {
		return bookRepository.findBookBybookID(bookID);
	}

	public void loadBooks(User user)
	{
		// Collect books from specific user
		books = bookRepository.findBookByUserUserID(user.getUserID());
	}

	public void updateBook(Long bookID, String date, float amount, String description, String categoryName, User user) {
		System.out.println(categoryName);
		Book book = this.findBookBybookID(bookID);
		book.setDate(date);
		book.setAmount(amount);
		book.setDescription(description);
		book.setCategory(categoryService.findCategoryByCategoryName(categoryName, user));
		bookRepository.save(book);
	}

	public void addBook(float amount, String description, String categoryName, User user)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");  
		LocalDateTime now = LocalDateTime.now();
		Book book = new Book();
		book.setDate(dtf.format(now));
		book.setAmount(amount);
		book.setDescription(description);
		book.setUser(user);
		categoryService.loadCategories(userService.findUserByeMail(userService.getUserEmail())); // Collect and load categories from specific user
		book.setCategory(categoryService.findCategoryByCategoryName(categoryName,user));
		books.add(book);
		bookRepository.save(book);
	}

	public void deleteBook(Book book)
	{
		books.remove(book);
		bookRepository.delete(book);
	}

	public List<Book> getBooks() {
		return books;
	}

}
