package com.admin_product;

//begins,springboot configuration,bootstrap process

import org.springframework.boot.SpringApplication; //main class bootstarp to launch springboot application
import org.springframework.boot.autoconfigure.SpringBootApplication; //annotation that combines several other annotations

@SpringBootApplication //enables auto-configuration and component scanning.
public class AdminProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminProductApplication.class, args);
	}

}
