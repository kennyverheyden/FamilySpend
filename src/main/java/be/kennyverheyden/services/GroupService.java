package be.kennyverheyden.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.kennyverheyden.models.Group;
import be.kennyverheyden.repositories.GroupRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class GroupService {

	private GroupRepository groupRepository;

	@Autowired
	public GroupService() {}
	
	 public List<Group> findAllGroups() {
	        List<Group> groups = groupRepository.findAll();
	        return groups;
	    }

	public List<Group> findGroupByGroupName (String groupName) {
		return groupRepository.findGroupByGroupName(groupName);
	}
}
