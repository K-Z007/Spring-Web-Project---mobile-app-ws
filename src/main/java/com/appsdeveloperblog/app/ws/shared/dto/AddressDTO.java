package com.appsdeveloperblog.app.ws.shared.dto;

public class AddressDTO
{
    private long id;    //id for user's id stored in database (different from userId shown to client);
    private String addressId;
    private String country;
    private String state;
    private String street;
    private String postCode;
    private String type;
    private UserDto userDetails;
    // all addressDTO's fields'names must match the filed names in AddressEntity to let ModelMapper.map() work:


    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getPostCode()
    {
        return postCode;
    }

    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public UserDto getUserDetails()
    {
        return userDetails;
    }

    public void setUserDetails(UserDto userDetails)
    {
        this.userDetails = userDetails;
    }

    public String getAddressId()
    {
        return addressId;
    }

    public void setAddressId(String addressId)
    {
        this.addressId = addressId;
    }
}
