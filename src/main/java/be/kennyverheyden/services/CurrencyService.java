package be.kennyverheyden.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import be.kennyverheyden.models.Currency;
import be.kennyverheyden.repositories.CurrencyRepository;

@Service
public class CurrencyService {

	@Autowired
	private CurrencyRepository currencyRepository;

	public CurrencyService() {}

	public List<Currency> findAllCurrencies() {
		List<Currency> currencies =currencyRepository.findAll();
		return currencies;
	}

	public Currency findCurrencyByCurrencyID(Long currencyID)
	{
		return currencyRepository.findCurencyByCurrencyID(currencyID);
	}
}
