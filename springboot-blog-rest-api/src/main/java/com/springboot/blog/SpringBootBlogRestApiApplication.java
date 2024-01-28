package com.springboot.blog;

import com.springboot.blog.entity.Role;
import com.springboot.blog.repository.RoleRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition (info = @Info(title = "spring Boot Blog App REST APIs",
		                         description ="Spring Boot Blog App REST APIs Documentation",version = "v1.0",
									contact =@Contact(
											name = "Shaina Hegde",
											email="shainahegde51@gmail.com",
											url = "https://www.linkedin.com/in/shainahegde51/"
),
		license = @License(
				name = "Apache 2.0",
				url = "https://www.blogapp.next/license"
		)

),
		externalDocs=@ExternalDocumentation
				(description = "Spring Boot Blog App Documentation",
					url = "https://github.com/shainahegde51/BlogApplication.git"
		)

)
public class SpringBootBlogRestApiApplication implements CommandLineRunner {
@Autowired
private RoleRepository roleRepository;
	//this is for replacing the manual code we write to convert entity to dto and vice versa
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBlogRestApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role  adminRole=new Role();
		adminRole.setName("ROLE_ADMIN");
		roleRepository.save(adminRole);
		Role userRole=new Role();
		userRole.setName("ROLE_USER");
		roleRepository.save(userRole);

	}
}
