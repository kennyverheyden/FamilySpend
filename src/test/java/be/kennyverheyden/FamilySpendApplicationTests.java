package be.kennyverheyden;

import static org.assertj.core.api.Assertions.assertThat;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class FamilySpendApplicationTests {

	@Test
	void contextLoads(ApplicationContext context) {
		 assertThat(context).isNotNull();
	}


}
