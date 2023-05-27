package be.kennyverheyden.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity(name="tblGroup")
public class Group implements Comparable<Group> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long groupID;

	@Column(name="groupName")
	private String groupName;
	
	@OneToOne
	@JoinColumn(name="userFK")
	User user;
	
	public Group(){}
	
	public Group(String groupName)
	{
		this.groupName=groupName;
	}

	public long getGroupID() {
		return groupID;
	}

	public void setGroupID(long groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public int compareTo(Group g) {
		return this.getGroupName().compareToIgnoreCase(g.getGroupName());
	}
	
}
