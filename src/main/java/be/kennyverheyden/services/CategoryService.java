package be.kennyverheyden.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.kennyverheyden.models.Category;
import be.kennyverheyden.models.Group;
import be.kennyverheyden.models.User;
import be.kennyverheyden.repositories.CategoryRepository;
import be.kennyverheyden.repositories.GroupRepository;
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

	public List<Category> findCategoryByCategoryName (String categoryName) {
		return categoryRepository.findCategoryByCategoryName(categoryName);
	}

	public List<Category> findCategoryByUserUserID (Long userID) {
		return categoryRepository.findCategoryByUserUserID(userID);
	}


	// Create Sample categories for one specific user
	// Assign a group to a category
	// All data from all the users are stored in one table tblCategories
	// We need to retrieve the groupID (PK) from the current user to assign the foreign key
	public void createCategorySampleData(User user)
	{
		List<Category> categories = new ArrayList();
		categories.add(new Category("Electricity/gas")); // Add new category
		categories.get(0).setGroup(groupService.getGroups().get(1)); // Assign foreign key to group
		categories.add(new Category("Wage"));
		categories.get(1).setGroup(groupService.getGroups().get(0));
		categories.add(new Category("Hypotheek"));
		categories.get(2).setGroup(groupService.getGroups().get(4));
		categories.add(new Category("Other incomes"));
		categories.get(3).setGroup(groupService.getGroups().get(0));
		categories.add(new Category("Other expenses"));
		categories.get(4).setGroup(groupService.getGroups().get(7));
		categories.add(new Category("School"));
		categories.get(5).setGroup(groupService.getGroups().get(3));
		categories.add(new Category("Car taxes"));
		categories.get(6).setGroup(groupService.getGroups().get(2));
		categories.add(new Category("Petrol"));
		categories.get(7).setGroup(groupService.getGroups().get(2));
		categories.add(new Category("Shopping"));
		categories.get(8).setGroup(groupService.getGroups().get(5));
		categories.add(new Category("Insurance"));
		categories.get(9).setGroup(groupService.getGroups().get(6));
		categories.add(new Category("Telecom"));
		categories.get(10).setGroup(groupService.getGroups().get(1));
		categories.add(new Category("Renovation loan"));
		categories.get(11).setGroup(groupService.getGroups().get(4));
		categories.add(new Category("Restaurant"));
		categories.get(12).setGroup(groupService.getGroups().get(7));
		// Assign user (foreign key)
		for(int i=0;i<categories.size();i++)
		{
			categories.get(i).setUser(user);
		}
		this.categories=categories;
		categoryRepository.saveAll(categories);
	}

	public void updateCategory(Long categoryID, String categoryName, Long userID)
	{
		// Collect categories from specific user
		categories = categoryRepository.findCategoryByUserUserID(userID);

		// Search the category by categoryName
		for(Category categorie:categories)
		{
			if(categorie.getCategoryID()==categoryID)
			{
				categorie.setCategoryName(categoryName);
				categoryRepository.save(categorie);
			}
		}

	}
}
