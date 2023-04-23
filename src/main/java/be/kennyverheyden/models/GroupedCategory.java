package be.kennyverheyden.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GroupedCategory {

	@Id
	private Long id;
	
	private String categoryName;
	private float total;
	
	GroupedCategory()
	{
	}
	
	GroupedCategory(String categoryName, float total)
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

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}
	
}
