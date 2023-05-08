package be.kennyverheyden.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import be.kennyverheyden.models.Book;
import be.kennyverheyden.models.Category;
import be.kennyverheyden.models.Group;
import be.kennyverheyden.models.GroupedCategory;
import be.kennyverheyden.models.GroupedGroup;
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

	// Find all book lines from a specific user
	public List<Book> findBookByUserUserID (Long userID) {
		return bookRepository.findBookByUserUserID(userID);
	}

	// Find a book line from a specific user
	public Book findBookBybookID (Long bookID) {
		return bookRepository.findBookBybookID(bookID);
	}

	// Find book lines from a specific user per month (and year)
	public List<Book> findBookByUserUserIDperMonth (Long userID, String month, String year) {
		List<Book> books = bookRepository.findBookByUserUserID(userID); // Load book lines from specific user
		List<Book> filteredBooks = new ArrayList(); // New list for storing filtered book lines
		String subString = "/"+month+"/"; // Store month /MM/ for month filter (substring)
		for(Book line:books)
		{
			if(line.getDate().contains(subString) && line.getDate().contains(year)) // Date contains specific month /MM/ and year YYYY
			{
				filteredBooks.add(line); // Add to new list
			}
		}
		return filteredBooks;
	}

	// Get the years
	public List<String> getYears (Long userID) {
		List<Book> books = bookRepository.findBookByUserUserID(userID); // Get all the books
		List<String> years = new ArrayList(); // Make list with the registered years
		years.add(Integer.toString(LocalDate.now().getYear())); // Avoid empty dropdown by empty book
		for(Book book:books)
		{
			String yearStr=book.getDate().substring(6,10);
			years.add(yearStr);
		}
		// Remove duplicates
		List<String> noDuplicatesYears = years.stream().distinct().collect(Collectors.toList());
		return noDuplicatesYears;
	}

	// Shows the result
	public String monthResult(Long userID, String month, String year)
	{
		List<Book> filteredBooks = findBookByUserUserIDperMonth (userID, month,year); // Get list by user and month
		double income=0;
		double spending=0;
		double result=0;
		String currency=userService.findUserByeMail(userService.getUserEmail()).getCurrency().getCurrencySymbol(); // Get currency from user
		for(Book line:filteredBooks)
		{
			// Spendings are recognized by the negative number (-) 
			if(line.getAmount() > 0) // > = income
			{
				income+=line.getAmount();
			}
			else
			{
				spending+=line.getAmount();
			}
		}
		spending=Math.abs(spending);
		result=income-spending; // Make total
		return "Income: "+currency+String.format("%.2f", income)+" | Spending: "+currency+String.format("%.2f", spending)+" | Result: "+currency+String.format("%.2f", result);
	}

	// Check if booking has category
	// If a category is assigned to a booking, user cannot delete category
	public boolean bookingHasCategory(Long catID, String userEmail)
	{
		List<Book> books = bookRepository.findAll();
		for(Book i:books)
		{
			if(i.getCategory().getCategoryID()==catID && i.getUser().geteMail().equals(userEmail))
			{
				return true;
			}
		}
		return false;
	}

	// Totals on category page
	public List<GroupedCategory> bookGroupByCategoryMonth(Long userID,String month, String year)
	{
		List<Book> books = bookRepository.findBookByUserUserID(userID); // Load book lines from specifiek user
		List<GroupedCategory> groupedCategories = new ArrayList(); // New list for storing "group by" categories
		String subString = "/"+month+"/"; // Store month /MM/ for month filter (substring)
		for(Book line:books) // Read book lines
		{
			if(line.getDate().toString().contains(subString) && line.getDate().contains(year)) { // Month filter: Check if date contains month /04/ and year YYYY

				// Check if category already exists in new grouped by category list
				int i=0;
				boolean duplicate=false;
				while(i<groupedCategories.size()) // Check new grouped by category list
				{
					if(line.getCategory().getCategoryName().equals(groupedCategories.get(i).getCategoryName()))
					{
						duplicate=true;
						break;
					}
					i++;
				}
				if(duplicate)
				{
					// If item exist, add total
					groupedCategories.get(i).addAmount(line.getAmount());
				}
				else
				{
					// If item not exist, create one
					groupedCategories.add(new GroupedCategory(line.getCategory().getCategoryName(),line.getAmount()));
				}
			}
		}
		return groupedCategories;
	}

	// Totals on group page
	public List<GroupedGroup> bookGroupByGroupMonth(Long userID, String month, String year)
	{
		List<GroupedCategory> groupedCategories =  bookGroupByCategoryMonth(userID, month, year); // Load from (grouped by) Category totals, already used on category page
		List<GroupedGroup> groupedGroups = new ArrayList(); // New list for "grouped by" groups
		List<Category> cats = categoryService.findCategoryByUserUserID(userID); // Load existing categories from specific user

		for(GroupedCategory line:groupedCategories) // Read totals from category page
		{	
			int i=0;
			while(i<cats.size()) // Read category list
			{
				if(line.getCategoryName().equals(cats.get(i).getCategoryName()))
				{
					int j=0;
					boolean duplicate=false;
					while(j<groupedGroups.size()) // Check new list "grouped by" groups
					{
						// Check if existing used group name is already added or not (duplicate)
						if(cats.get(i).getGroup().getGroupName().equals(groupedGroups.get(j).getGroupName()))
						{
							duplicate=true;
							break;
						}
						j++;
					}
					if(duplicate)
					{
						// If Group exist, add total
						groupedGroups.get(j).addAmount(line.getTotal());
					}
					else
					{
						// If Group not exist, create en add to "grouped by" groups list
						groupedGroups.add(new GroupedGroup(cats.get(i).getGroup().getGroupName(),line.getTotal()));
					}
					break;
				}
				i++;
			}
		}
		return groupedGroups;
	}

	public void loadBooks(User user)
	{
		// Collect books from specific user
		books = bookRepository.findBookByUserUserID(user.getUserID());
	}

	public void updateBook(Long bookID, String date, Double amount, String description, String categoryName, User user) {
		categoryService.loadCategories(userService.findUserByeMail(userService.getUserEmail())); // Collect and load categories from specific user
		Book book = this.findBookBybookID(bookID);
		book.setDate(date);
		book.setAmount(amount);
		book.setDescription(description);
		book.setCategory(categoryService.findCategoryByCategoryName(categoryName, user));
		bookRepository.save(book);
	}

	public void addBook(double amount, String date, String description, String categoryName, User user)
	{
		Book book = new Book();
		book.setDate(date);
		book.setAmount(amount);
		book.setDescription(description);
		book.setUser(user);
		categoryService.loadCategories(userService.findUserByeMail(userService.getUserEmail())); // Collect and load categories from specific user
		book.setCategory(categoryService.findCategoryByCategoryName(categoryName,user)); // Find and add category name
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

	// Delete all bookings from a specific month and user
	public void deleteMonthBook(Long UserID, String month, String year)
	{
		List<Book> bookingsToDelete = this.findBookByUserUserIDperMonth(UserID, month, year);
		bookRepository.deleteAll(bookingsToDelete);
	}

}
