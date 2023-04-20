package be.kennyverheyden.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import be.kennyverheyden.models.Category;


@Repository
public class BookRepositorySQL {
	
	

	//Custom querry
	@Autowired
	private JdbcTemplate jdbc;

	public List<String> CategoryTotals() {
		String sqlSelect = "select categoryName, sum(amount) as total from tblBook join tblCategory on categoryFK = categoryID where tblBook.userFK=9 group by categoryID;";

		RowMapper<Category> rowMapper = (r, i) -> {
			Category rowObject =
					new Category(r.getString("categoryName"),r.getFloat(total));
			return rowObject;
		};
		return jdbc.query(sqlSelect, rowMapper);
	}
	
}
