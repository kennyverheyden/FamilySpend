package be.kennyverheyden.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.kennyverheyden.models.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
	Currency findCurencyByCurrencyID(Long currencyID);
}
