package cstech.ai.hamt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages="cstech.ai.hamt.repository")
@EntityScan(basePackages="cstech.ai.hamt.entity")
public class HamtApplication {

	public static void main(String[] args) {
		SpringApplication.run(HamtApplication.class, args);
	}

}
