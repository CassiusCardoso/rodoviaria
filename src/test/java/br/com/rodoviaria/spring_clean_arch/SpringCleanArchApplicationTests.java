package br.com.rodoviaria.spring_clean_arch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class SpringCleanArchApplicationTests {

	@Container
	private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test");

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		// Propriedades do banco de dados (que você já tem)
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

		// ✅ ADICIONE ESTAS DUAS LINHAS PARA O RABBITMQ
		// Fornecemos valores fictícios apenas para o teste passar.
		registry.add("rabbitmq.queue.name", () -> "test.queue");
		registry.add("rabbitmq.exchange.name", () -> "test.exchange");
	}

	@Test
	void contextLoads() {
	}
}