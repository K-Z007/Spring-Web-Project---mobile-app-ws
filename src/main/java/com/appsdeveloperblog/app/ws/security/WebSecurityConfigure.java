package com.appsdeveloperblog.app.ws.security;

import com.appsdeveloperblog.app.ws.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//By adding @Configuration, you mark the WebSecurityConfigure class as a bean definition.
// This instructs Spring to scan for and register it as a Spring managed bean during application startup.
// Without it, Spring wouldn't recognize the class or its configuration settings.

//The @Configuration annotation in the context of Spring signifies that the class is a configuration class, and it is often
// used in conjunction with @EnableWebSecurity to indicate that the class contains configuration for Spring Security.
//In the case of a WebSecurityConfigurerAdapter class (or any class that extends it), adding @Configuration is important because
// it signals to Spring that the class should be considered as a source of bean definitions and configuration settings.
// Without this annotation, Spring might not recognize the class as a configuration class, and the security configuration
// defined within it may not be applied.
@Configuration
@EnableWebSecurity
public class WebSecurityConfigure
{
    //this can work as UserService implements
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurityConfigure(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //Here is the main method to configure all the Security filter chains:
    //Spring framework will call this methond automatically.
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception
    {
        // Configure AuthenticationManagerBuilder:
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        //Tells AuthenticationManager to use "userService" class to load user details from database;
        //also tells which encryption object (method) to use to verify the password user passed trying to login
        //matches the password stored in the database;
        authenticationManagerBuilder
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        //Instantiate AuthenticationFilter and customize the User login url:
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/users/login"); //this is a method of authenticationFilter inherited from AbstractAuthenticationProcessingFilter class;

        // Stateless rest api requires csrf(Cross-Site Request Forgery) to be disabled;
        /*
        auth.requestMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL).permitAll():
        Allows anyone to access the /signup endpoint using POST requests for creating new accounts.
        * */
          http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth ->
                            auth.requestMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL)
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated()
                    )    //update the http security obj with new "authenticationManager";
                                    .authenticationManager(authenticationManager)
                                    .addFilter(authenticationFilter)
                  //This line adds an authorization filter (AuthorizationFilter) to the filter chain.
                  // Authorization filters are responsible for checking whether the authenticated user has the necessary permissions to access a particular resource.
                  .addFilter(new AuthorizationFilter(authenticationManager))
                  // By setting the sessionCreationPolicy to STATELESS, you confirm that your API won't
                  // create or manage HTTP sessions for authenticated users, as you have JWT token to valid the user authority.
                  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


          return http.build();

    }
}
