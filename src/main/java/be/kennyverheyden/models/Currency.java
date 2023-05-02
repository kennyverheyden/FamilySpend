package be.kennyverheyden.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name="tblCurrency")
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long currencyID;
	
	@Column(name="currencySymbol")
	private String currencySymbol;

	public Long getCurrencyID() {
		return currencyID;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}
	
}
