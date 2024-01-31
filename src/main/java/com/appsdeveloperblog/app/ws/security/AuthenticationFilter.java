package com.appsdeveloperblog.app.ws.security;

import com.appsdeveloperblog.app.ws.SpringApplicationContext;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.service.implementation.UserServiceImpl;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.requests.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

//Explain: UsernamePasswordAuthenticationFilter.class is the default filter chin: when there is a login request,
//the http will pass login info to this filter first;
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    @Autowired
    UserService userService;


    public AuthenticationFilter(AuthenticationManager authenticationManager)
    {
        super(authenticationManager);
    }

    @Override // method from UsernamePasswordAuthenticationFilter.class
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException
    {
        try
        {
            //The ObjectMapper().readValue() method is used to parse (deserialize) JSON from a client's UI inputted JSON value into Jave obj.
            //This line uses Jackson's ObjectMapper to deserialize the JSON data received in the request's input stream into a UserLoginRequestModel object.
            // This assumes that the JSON structure from the client's UI matches the structure of the UserLoginRequestModel class.
            UserLoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(),
                    UserLoginRequestModel.class);

            //If the JSON object is successfully read, it is used to create a new UsernamePasswordAuthenticationToken object.
            // This object contains the user's email address, password, and an empty list of authorities (permissions).
            //The getAuthenticationManager().authenticate() method is called with the UsernamePasswordAuthenticationToken object.
            // This method will attempt to authenticate the user using the provided credentials by invoking loadUserByUsername() method
            // under UserServiceImpl.class, and then return an "Authentication" object if successful.
            // then this "Authentication" object is passed to following method successfulAuthentication(...Authentication authResult);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    ));

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

        /*
        * Calling the AuthenticationManager:

        getAuthenticationManager() retrieves the AuthenticationManager object provided by the Spring Security framework.

        The authenticate() method of the AuthenticationManager object is called with the UsernamePasswordAuthenticationToken object as an argument.

        This method will perform the following steps:

        Find a suitable AuthenticationProvider based on the type of authentication token (in this case, UsernamePasswordAuthenticationToken).
        Delegate the authentication process to the chosen provider.
        The chosen provider will typically use a UserDetailsService to retrieve the user details based on the provided email address.
        It will then compare the retrieved user's password with the provided password.
        If the credentials match, the provider will create a fully authenticated Authentication object containing the user details and granted authorities.
        3. Authentication result:

        If the authentication process is successful, the authenticate() method will return a fully authenticated Authentication object. This object will be used by Spring Security to manage the user's session and grant them access to protected resources based on their assigned authorities.
        If the authentication fails, the authenticate() method will throw an AuthenticationException, which will be handled by the filter and result in an appropriate error response to the client.
         * */

        //Once authentication is successful, Spring framework will then invoke following method;
        @Override
        protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication authResult) throws IOException, ServletException
        {
            //Following logic is to return a JWTS access Token to the client side so JWTS can be used as an access
            //token to prevent unwanted access to a protected resource. which the API will decode and validate before sending a response.
            //A valid token allows a user to retain access to an online service or web application until the token expires.
            //This offers convenience, as the user can continue to access a resource without re-entering their
            //login credentials every time. A token's life cycle varies depending on the type of token it is.
            byte[] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes());
            SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

            // get current time;
            Instant now = Instant.now();

            //authResult is the previous Authentication result you get from above attemptAuthentication() method:
             String userName = ((User) authResult.getPrincipal()).getUsername();
            String token = Jwts.builder()
                    .setSubject(userName)
                    .setExpiration(
                            Date.from(now.plusMillis(SecurityConstants.EXPIRATION_TIME)))
                    .setIssuedAt(Date.from(now)).signWith(secretKey, SignatureAlgorithm.HS512).compact();

            //add the JWTS token to the client header;
            res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);

            //Retrieve UserID from database when successfully Authenticated a login, then pass to the response Header;
            //UserService userService = (UserService) SpringApplicationContext.getBeanByName("userServiceImpl");
            UserService userService = SpringApplicationContext.getBeanByType(UserServiceImpl.class);
            UserDto userDto = userService.getUser(userName);
            res.addHeader("UserId", userDto.getUserId());
        }


}
