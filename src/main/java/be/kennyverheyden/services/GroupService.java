package be.kennyverheyden.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.kennyverheyden.models.Group;
import be.kennyverheyden.models.User;
import be.kennyverheyden.repositories.GroupRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class GroupService {

	@Autowired
	private GroupRepository groupRepository;
	//	@Autowired
	//	private UserService userService;

	// Specific groups by current user;
	private List<Group> groups;

	public GroupService() {}

	public List<Group> findAllGroups() {
		List<Group> groups = groupRepository.findAll();
		return groups;
	}

	public List<Group> findGroupByGroupName (String groupName) {
		return groupRepository.findGroupByGroupName(groupName);
	}

	public Group findGroupByGroupID (Long groupID)
	{
		return groupRepository.findGroupByGroupID(groupID);
	}

	public List<Group> findGroupByUserUserID (Long userID) {
		return groupRepository.findGroupByUserUserID(userID);
	}

	public void createGroupSampleData(User user)
	{
		List<Group> groups = new ArrayList(); // Make group list from user
		groups.add(new Group("Income"));
		groups.add(new Group("General"));
		groups.add(new Group("Car"));
		groups.add(new Group("Children"));
		groups.add(new Group("House"));
		groups.add(new Group("Shopping"));
		groups.add(new Group("Insurance"));
		groups.add(new Group("Various"));
		for(Group i:groups)
		{
			i.setUser(user);
		}
		this.groups=groups;
		groupRepository.saveAll(groups);
	}

	public List<Group> getGroups() {
		return groups;
	}
}
