package com.appsdeveloperblog.app.ws.shared.dto;

import java.io.Serializable;
import java.util.List;

//UserDto object to be shared across various layers (UI, Service, Database)
// to transfer data from UI Level (userDetails) to Service Layer.
public class UserDto implements Serializable
{
    private static final long serialVersionUID = 1L; //serialVersionUID is needed
    private long id;    //id for user's id stored in database (different from userId shown to client);
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean eamilVerificationStatus = false;
    private List<AddressDTO> addresses;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEncryptedPassword()
    {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword)
    {
        this.encryptedPassword = encryptedPassword;
    }

    public String getEmailVerificationToken()
    {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken)
    {
        this.emailVerificationToken = emailVerificationToken;
    }

    public Boolean getEamilVerificationStatus()
    {
        return eamilVerificationStatus;
    }

    public void setEamilVerificationStatus(Boolean eamilVerificationStatus)
    {
        this.eamilVerificationStatus = eamilVerificationStatus;
    }

    public List<AddressDTO> getAddresses()
    {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses)
    {
        this.addresses = addresses;
    }

}
