package com.bankingApp.bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.border.TitledBorder;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Asha Banking App",
		description = "Backend Rest APIs for Asha bank",
		version = "v1.0",
		contact = @Contact(
				name = "Asha Gasimova",
				email = "asha.gasimova@gmail.com",
				url = "https://github.com/ashagasimova/banking-app"

		),
		license = @License(
				name = "Asha",
				url = "https://github.com/ashagasimova/banking-app"
		)
),
		externalDocs = @ExternalDocumentation(
				description = "Backend Rest APIs for Asha bank documentation",
				url = "https://github.com/ashagasimova/banking-app"
		)
)
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
