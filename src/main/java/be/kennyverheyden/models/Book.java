package be.kennyverheyden.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity(name="tblBook")
public class Book implements Comparable<Book> { // Book is comparable - sort by date

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long bookID;

	@Column(name="date")
	String date;

	@Column(name="amount")
	double amount;

	@Column(name="description")
	String description;

	@OneToOne
	@JoinColumn(name="categoryFK")
	Category category;

	@OneToOne
	@JoinColumn(name="userFK")
	User user;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getBookID() {
		return bookID;
	}

	// Sort by date (Compare)
	@Override
	public int compareTo(Book o) {
		return this.getDate().compareTo(o.getDate());
	}

}
