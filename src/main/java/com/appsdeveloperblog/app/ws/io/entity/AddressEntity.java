package com.appsdeveloperblog.app.ws.io.entity;

import jakarta.persistence.*;

import java.io.Serializable;

//AddressEntity is for storing user's address data into database;
@Entity(name = "addresses") // name="addresses" is the table name;
public class AddressEntity implements Serializable
{
    @Id //meaning this id is for database and will be auto increment.
    @GeneratedValue
    private long id;

    //@Column (from JPA package) annotation for Adding the column the name in the table of a particular MySQL database.
    @Column(nullable = false, length = 30)  //nullable = false means this column must not be empty;
    private String addressId;

    @Column(nullable = false, length = 15)
    private String country;

    @Column(nullable = false, length = 15)
    private String state;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 10)
    private String postCode;

    @Column(nullable = false, length = 10)
    private String type;

    @ManyToOne
    @JoinColumn(name="user_id") //"user_id" is the column name in "users" table to join with;
    private UserEntity userDetails;


    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getAddressId()
    {
        return addressId;
    }

    public void setAddressId(String addressId)
    {
        this.addressId = addressId;
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

    public UserEntity getUserDetails()
    {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails)
    {
        this.userDetails = userDetails;
    }



}
