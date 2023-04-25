package be.kennyverheyden.models;

import jakarta.persistence.Id;

public class Month {

	@Id
	private Long id;

	private String month;

	private String monthNumber;

	public Month(String month, String monthNumber)
	{
		this.month=month;
		this.monthNumber=monthNumber;
	}

	public String getMonth() {
		return month;
	}

	public String getMonthNumber() {
		return monthNumber;
	}

	public static String getMonthByStringNumber(String number)
	{
		switch(number){
		case "01":
			return "January";
		case "02":
			return "February";
		case "03":
			return "March";
		case "04":
			return "April";
		case "05":
			return "May";
		case "06":
			return "June";
		case "07":
			return "July";
		case "08":
			return "August";
		case "09":
			return "September";
		case "10":
			return "October";
		case "11":
			return "November";
		case "12":
			return "December";
		default:
			return "Invalid month";
		} 
	}

}
