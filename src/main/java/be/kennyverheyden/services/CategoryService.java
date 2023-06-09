package be.kennyverheyden.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import be.kennyverheyden.models.Category;
import be.kennyverheyden.models.User;
import be.kennyverheyden.repositories.CategoryRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private GroupService groupService;

	private List<Category> categories;

	public CategoryService() {}

	public List<Category> findAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories;
	}

	public Category findCategoryByCategoryName (String categoryName, User user) {
		Category categoryByNamePerUser = null;
		// Filter per User
		for(Category i:categories)
		{
			if(i.getUser().getUserID()==user.getUserID() && (i.getCategoryName().equalsIgnoreCase(categoryName)))
			{
				categoryByNamePerUser=i;
			} 
		}
		return categoryByNamePerUser;
	}

	public boolean categoryHasGroup(Long GroupID, String userEmail)
	{
		List<Category> cats = categoryRepository.findAll();
		for(Category i:cats)
		{
			if(i.getGroup().getGroupID()==GroupID && i.getUser().geteMail().equals(userEmail))
			{
				return true;
			}
		}
		return false;
	}


	public List<Category> findCategoryByUserUserID (Long userID) {
		return categoryRepository.findCategoryByUserUserID(userID);
	}

	public Category findCategoryByCategoryID (Long userID) {
		return categoryRepository.findCategoryByCategoryID(userID);
	}

	public boolean groupInCategoryIsEqual (String categoryName, User user, String groupName) {
		List<Category>categoryByName =categoryRepository.findCategoryByCategoryName(categoryName);

		for(Category i:categoryByName)
		{
			if(i.getUser().getUserID()==user.getUserID())
			{
				if(i.getGroup().getGroupName().equals(groupName))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void loadCategories(User user)
	{
		// Collect categories from specific user
		categories = categoryRepository.findCategoryByUserUserID(user.getUserID());
	}

	// Create Sample categories for one specific user
	// Assign a group to a category
	// All data from all the users are stored in one table tblCategories
	// We need to retrieve the groupID (PK) from the current user to assign the foreign key
	public void createCategorySampleData(User user)
	{
		List<Category> categories = new ArrayList();
		categories.add(new Category("Electricity/gas",1)); // Add new category
		categories.get(0).setGroup(groupService.getGroups().get(4)); // Assign foreign key to group
		categories.add(new Category("Wage",0));
		categories.get(1).setGroup(groupService.getGroups().get(0));
		categories.add(new Category("Mortage",1));
		categories.get(2).setGroup(groupService.getGroups().get(7));
		categories.add(new Category("Other income",0));
		categories.get(3).setGroup(groupService.getGroups().get(0));
		categories.add(new Category("Other expenses",1));
		categories.get(4).setGroup(groupService.getGroups().get(8));
		categories.add(new Category("School",1));
		categories.get(5).setGroup(groupService.getGroups().get(3));
		categories.add(new Category("Car taxes",1));
		categories.get(6).setGroup(groupService.getGroups().get(2));
		categories.add(new Category("Petrol",1));
		categories.get(7).setGroup(groupService.getGroups().get(2));
		categories.add(new Category("Shopping",1));
		categories.get(8).setGroup(groupService.getGroups().get(5));
		categories.add(new Category("Insurance",1));
		categories.get(9).setGroup(groupService.getGroups().get(6));
		categories.add(new Category("Telecom",1));
		categories.get(10).setGroup(groupService.getGroups().get(1));
		categories.add(new Category("Renovation loan",1));
		categories.get(11).setGroup(groupService.getGroups().get(7));
		categories.add(new Category("Restaurant",1));
		categories.get(12).setGroup(groupService.getGroups().get(9));
		// Assign user (foreign key)
		for(int i=0;i<categories.size();i++)
		{
			categories.get(i).setUser(user);
		}
		this.categories=categories;
		categoryRepository.saveAll(categories);
	}

	public void updateCategory(Long categoryID, String categoryName, String groupName, Integer inout, User user)
	{
		// Search the category by categoryName
		for(Category category:categories)
		{
			if(category.getCategoryID()==categoryID)
			{
				category.setCategoryName(categoryName);
				category.setGroup(groupService.findGroupByGroupName(groupName,user));
				category.setInOrOut(inout);
				categoryRepository.save(category);
			}
		}
	}

	public void addCategory(String categoryName, String groupName, int inout, User user)
	{
		Category category = new Category();
		category.setCategoryName(categoryName);
		category.setUser(user);
		category.setGroup(groupService.findGroupByGroupName(groupName,user));
		category.setInOrOut(inout);
		categories.add(category);
		categoryRepository.save(category);
	}

	public void deleteCategory(Category category)
	{
		categories.remove(category);
		categoryRepository.delete(category);
	}

	public List<Category> getCategories() {
		return categories;
	}

}
