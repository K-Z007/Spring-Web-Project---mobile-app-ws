package com.appsdeveloperblog.app.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class MobileAppWsApplication extends SpringBootServletInitializer
{

	public static void main(String[] args) {
		SpringApplication.run(MobileAppWsApplication.class, args);
	}

	//@Bean is used for method level annotation when you want to use a class which from third-party package;
	//or you want to create complex logic before create an instance of a class in a method;
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//Override this method under SpringBootServletInitializer.class;
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MobileAppWsApplication.class);
	}

}
