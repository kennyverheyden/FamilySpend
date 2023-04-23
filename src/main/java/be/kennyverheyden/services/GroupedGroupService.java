package be.kennyverheyden.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import be.kennyverheyden.models.GroupedGroup;
import be.kennyverheyden.repositories.GroupedGroupRepository;

@Service
public class GroupedGroupService {

	@Autowired
	GroupedGroupRepository groupedGroupRepository;
	@Autowired
	UserService userService;
	
	public GroupedGroupService() {}
	
	public List<GroupedGroup> groupedGroups()
	{
		Long userID = userService.findUserByeMail(userService.getUserEmail()).getUserID();
		return groupedGroupRepository.groupedGroups(userID);
	}
	
}
