package br.com.rodoviaria.spring_clean_arch;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Para que o Swagger UI mostre um campo "Authorize" onde você pode colar o token JWT, adicione uma configuração na sua classe principal.
@OpenAPIDefinition(info = @Info(title = "Rodoviária API", version = "v1", description = "Documentação da API do sistema de rodoviária"))
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer")
public class
SpringCleanArchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCleanArchApplication.class, args);
	}

}
