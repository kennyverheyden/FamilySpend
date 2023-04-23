package be.kennyverheyden.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.kennyverheyden.models.GroupedCategory;
import be.kennyverheyden.repositories.GroupedCategoryRepository;

@Service
public class GroupedCategoryService {

	@Autowired
	GroupedCategoryRepository groupedCategoryRepository;
	@Autowired
	UserService userService;
	
	public GroupedCategoryService() {}
	
	public List<GroupedCategory> groupedCategories()
	{
		Long userID = userService.findUserByeMail(userService.getUserEmail()).getUserID();
		return groupedCategoryRepository.groupedCategories(userID);
	}
	
}
