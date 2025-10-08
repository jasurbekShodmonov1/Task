package com.example.task;

import com.example.task.entity.User;
import com.example.task.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;

@SpringBootApplication
public class TaskApplication {

	public static void main(String[] args) {

		SpringApplication.run(TaskApplication.class, args);
		String sardor = "sardor";
		String password = new BCryptPasswordEncoder().encode(sardor);
		System.out.println(password);


	}


//	@Bean
//	CommandLineRunner run(UserRepository userRepository){
//		return args -> {
//			User user = new User();
//			user.setFullName("Jasurbek");
//			user.setBalance(BigDecimal.valueOf(123456.789));
//			user.setUsername("jasur");
//			user.setPassword("jasur");
//			userRepository.save(user);
//		};
//	}

}
