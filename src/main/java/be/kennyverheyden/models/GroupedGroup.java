package be.kennyverheyden.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GroupedGroup {

	@Id
	private Long id;
	
	private String groupName;
	private double total;
	
	public GroupedGroup()
	{
	}
	
	public GroupedGroup(String groupName, double  total)
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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public void addAmount(double amount) {
		this.total = total+amount;
	}
	
}
