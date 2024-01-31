package com.appsdeveloperblog.app.ws.security;

import com.appsdeveloperblog.app.ws.SpringApplicationContext;
import org.springframework.core.env.Environment;


public class SecurityConstants
{
    //Token expiration time (normally should be only 1 hour);
    public static final long EXPIRATION_TIME=6000000;  // 100 mins
    public static final String TOKEN_PREFIX="Bearer ";
    public static final String HEADER_STRING="Authorization";
    public static final String SIGN_UP_URL="/users";


    public static String getTokenSecret() {
        Environment environment = (Environment) SpringApplicationContext.getBeanByType(Environment.class);
        return environment.getProperty("tokenSecret");
    }

}
