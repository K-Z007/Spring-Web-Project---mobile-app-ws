package com.appsdeveloperblog.app.ws.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
//BasicAuthenticationFilter is basic spring security class to process Http request with its headers,
//then put the result to the secruity

//This class is for Authenticate the JWTS token for protected api points:
//such as when a logined user try to update user details, his JT
public class AuthorizationFilter extends BasicAuthenticationFilter
{

    public AuthorizationFilter(AuthenticationManager authenticationManager)
    {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        //Get the value of the "Authorization" in request header;
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)){
             chain.doFilter(request, response);
             return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        /*This line sets the obtained Authentication object (authentication) into the SecurityContextHolder.
        The SecurityContextHolder is a central class in Spring Security that holds the details of the security context for
        the current thread of execution. By setting the authentication object, you are essentially telling Spring Security
        that the current user has been successfully authenticated. This is crucial for Spring Security to
        make authorization decisions and to provide information about the authenticated user throughout the application.
        */
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    /**
     * From http request to decode the JWT access token so to get the User infomation
     * @param request the new <code>HttpServletRequest</code> token, or
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request)
    {
        //Get the value of the "Authorization" in request header;
        String authorizationHeader = request.getHeader(SecurityConstants.HEADER_STRING);

        if(authorizationHeader == null) return null;

        //Extract the token from the value of the "Authorization" in request header:
        //removes the prefix (e.g., "Bearer ") from the header, leaving the actual token string.
        String token = authorizationHeader.replace(SecurityConstants.TOKEN_PREFIX, "");

        //decoding the token?
        //encodes the secret key to byte array using Base64, a common encoding format for secrets.
        byte[] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        // builds a JWT parser with the secret key to validate the token signature.
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();

        //attempts to parse and validate the token. If the signature is invalid or the token is expired,
        // an exception will be thrown.
        //The Claims type represents the claims (payload) of the JWT, including information like:
        //issuer, subject, expiration time, etc.
        Jwt<Header, Claims> jwt = jwtParser.parse(token);

        //Extract the subject (username) from the JWT and create Authentication object:
        // retrieves the "sub" claim (subject) from the decoded token.
        // The subject typically identifies the user associated with the token.
        String subject = jwt.getBody().getSubject();

        //If the subject (username) is missing (null), the code returns null, indicating an invalid or incomplete token.
        if(subject == null) return null;

        //Otherwise, a UsernamePasswordAuthenticationToken object is created
        //"subject" serves as the username.
        //"null" signifies no password is involved (common for JWT-based authentication).
        //"new ArrayList<>()" defines an empty list of authorities (roles) initially.
        return new UsernamePasswordAuthenticationToken(subject, null, new ArrayList<>());

    }

}
