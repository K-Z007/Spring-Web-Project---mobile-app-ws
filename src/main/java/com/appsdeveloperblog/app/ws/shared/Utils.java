package com.appsdeveloperblog.app.ws.shared;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Utils
{
    private final Random RANDOM = new Random();
    private final String TEXTBASE = "0123456789ABCDEFGHIJKLMNOPQISTUVWXYZabcdefghijklmnopqistuvwxyz";

    public String generateUserID(int length)
    {
        return generateRandomString(length);
    }

    public String generateAddressID(int length)
    {
        return generateRandomString(length);
    }

    private String generateRandomString(int length)
    {
        StringBuilder returnedString = new StringBuilder();

        for (int i = 0; i < length; i++)
        {
            returnedString.append(TEXTBASE.charAt(RANDOM.nextInt(TEXTBASE.length())));
        }

        return returnedString.toString();
    }
}
