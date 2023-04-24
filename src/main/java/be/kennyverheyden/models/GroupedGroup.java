package be.kennyverheyden.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GroupedGroup {

	@Id
	private Long id;
	
	private String groupName;
	private float total;
	
	public GroupedGroup()
	{
	}
	
	public GroupedGroup(String groupName, float total)
	{
		this.groupName=groupName;
		this.total=total;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}
	
	public void addAmount(float amount) {
		this.total = total+amount;
	}
	
}
