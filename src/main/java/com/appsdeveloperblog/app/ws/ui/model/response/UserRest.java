package com.appsdeveloperblog.app.ws.ui.model.response;


import java.util.List;

//UserRest is the User model to be returned to UI client front-end to display non-sensitive info.
public class UserRest
{
    private String userId;  //this is different from database ID;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressRest> addresses;



    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public List<AddressRest> getAddresses()
    {
        return addresses;
    }

    public void setAddresses(List<AddressRest> addresses)
    {
        this.addresses = addresses;
    }

}
