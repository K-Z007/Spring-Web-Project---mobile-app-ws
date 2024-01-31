package com.appsdeveloperblog.app.ws.ui.model.requests;

//This is the model for users to login to the web before doing any query;
public class UserLoginRequestModel
{
    public String email;
    public String password;


    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }


}