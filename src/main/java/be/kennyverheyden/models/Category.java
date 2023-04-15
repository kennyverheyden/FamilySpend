package be.kennyverheyden.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity(name="tblCategory")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long categoryID;

	@Column(name="categoryName")
	private String categoryName;

	@Column(name="income")
	private int income; // 0 or 1 for false or true

	@OneToOne
	@JoinColumn(name="userFK")
	User user;

	@OneToOne
	@JoinColumn(name="groupFK")
	Group group;

	public Category() {}

	public Category(String categoryName)
	{
		this.categoryName=categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public long getCategoryID() {
		return categoryID;
	}

}
