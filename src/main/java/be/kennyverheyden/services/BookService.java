package be.kennyverheyden.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
	private GroupService groupService;
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

	public List<GroupedCategory> bookGroupByCategoryMonth(Long userID,String month)
	{
		List<Book> books = bookRepository.findBookByUserUserID(userID);
		List<GroupedCategory> groupedCategories = new ArrayList();
		String subString = "/"+month+"/";
		for(Book line:books)
		{
			if(line.getDate().toString().contains(subString)) {

				// Check if category already exists
				int i=0;
				boolean duplicate=false;
				while(i<groupedCategories.size())
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

	public List<GroupedGroup> bookGroupByGroupMonth(Long userID, String month)
	{
		List<GroupedCategory> groupedCategories =  bookGroupByCategoryMonth(userID, month);
		List<GroupedGroup> groupedGroups = new ArrayList();
		List<Category> cats = categoryService.findCategoryByUserUserID(userID);

		for(GroupedCategory line:groupedCategories)
		{	
			int i=0;

			while(i<cats.size())
			{
				if(line.getCategoryName().equals(cats.get(i).getCategoryName()))
				{
					int j=0;
					boolean duplicate=false;
					while(j<groupedGroups.size())
					{
						if(cats.get(i).getGroup().getGroupName().equals(groupedGroups.get(j).getGroupName()))
						{
							duplicate=true;
							break;
						}
						j++;
					}

					if(duplicate)
					{
						// If item exist, add total
						groupedGroups.get(j).addAmount(line.getTotal());
					}
					else
					{
						// If item not exist, create one
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

	public void updateBook(Long bookID, String date, float amount, String description, String categoryName, User user) {
		categoryService.loadCategories(userService.findUserByeMail(userService.getUserEmail())); // Collect and load categories from specific user
		Book book = this.findBookBybookID(bookID);
		book.setDate(date);
		book.setAmount(amount);
		book.setDescription(description);
		book.setCategory(categoryService.findCategoryByCategoryName(categoryName, user));
		bookRepository.save(book);
	}

	public void addBook(float amount, String description, String categoryName, User user)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
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
