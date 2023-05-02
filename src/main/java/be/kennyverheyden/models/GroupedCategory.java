package be.kennyverheyden.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GroupedCategory {

	@Id
	private Long id;
	
	private String categoryName;
	private double total;
	
	public GroupedCategory()
	{
	}
	
	public GroupedCategory(String categoryName, double total)
	{
		this.categoryName=categoryName;
		this.total=total;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
