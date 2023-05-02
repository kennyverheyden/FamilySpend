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

	// Specific groups by current user;
	private List<Group> groups;

	public GroupService() {}

	public List<Group> findAllGroups() {
		List<Group> groups = groupRepository.findAll();
		return groups;
	}

	public Group findGroupByGroupName (String groupName, User user) {
		Group groupByNamePerUser = null;
		// Filter per User
		for(Group i:groups)
		{
			if(i.getUser().getUserID()==user.getUserID() && (i.getGroupName().equalsIgnoreCase(groupName)))
			{
				groupByNamePerUser=i;
			}
		}
		return groupByNamePerUser;
	}

	public Group findGroupByGroupID (Long groupID)
	{
		return groupRepository.findGroupByGroupID(groupID);
	}

	public List<Group> findGroupByUserUserID (Long userID) {
		return groupRepository.findGroupByUserUserID(userID);
	}

	public void loadGroups(User user)
	{
		// Collect groups from specific user
		groups = groupRepository.findGroupByUserUserID(user.getUserID());
	}

	// For new user to start
	public void createGroupSampleData(User user)
	{
		List<Group> groups = new ArrayList();
		groups.add(new Group("Income"));
		groups.add(new Group("General"));
		groups.add(new Group("Transport"));
		groups.add(new Group("Children"));
		groups.add(new Group("Home"));
		groups.add(new Group("Shopping"));
		groups.add(new Group("Insurance"));
		groups.add(new Group("Debts"));
		groups.add(new Group("Various"));
		for(Group i:groups)
		{
			i.setUser(user); // A group is linked to an user
		}
		this.groups=groups;
		groupRepository.saveAll(groups);
	}

	public void updateGroup(Long groupID, String groupName, User user)
	{

		// Search the group by groupName
		for(Group group:groups)
		{
			if(group.getGroupID()==groupID)
			{
				group.setGroupName(groupName);
				groupRepository.save(group);
			}
		}
	}

	public void addGroup(String groupName, User user)
	{	
		Group group = new Group();
		group.setGroupName(groupName);
		group.setUser(user);
		groups.add(group);
		groupRepository.save(group);
	}

	public void deleteGroup(Group group)
	{
		// Check if linked
		// List<Group> groups = this.getGroups();
		groups.remove(group);
		groupRepository.delete(group);
	}

	public List<Group> getGroups() {
		return groups;
	}
}
