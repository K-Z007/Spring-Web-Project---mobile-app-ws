package com.appsdeveloperblog.app.ws.io.entity;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessageTypes;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

//UserEntity is for storing user data into database;
@Entity(name = "users") // name="users" is the table name called "users" for this class to be stored data into;
public class UserEntity implements Serializable
{
//    private static final long serialVersionUID =

    @Id //meaning this id is for database and will be auto increment.
    @GeneratedValue
    private long id;

    //@Column (from JPA package) annotation for Adding the column the name in the table of a particular MySQL database.
    @Column(nullable = false)  //nullable = false means this column must not be empty;
    private String userId;  //pass for in HTTP request;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;
    private String emailVerificationToken;

    @Column(nullable = false)
    private Boolean eamilVerificationStatus = false;


    //meaning map this addresses column to the "userDetails" under AddressEntity.class (table "addresses")
    //cascade=CascadeType.ALL meaning this UserEntity will auto update the table with addresses,
    // when deleted, the address table info will also be deleted;
    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
    private List<AddressEntity> addresses;


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

    public void setEmailVerificationStatus(Boolean eamilVerificationStatus)
    {
        this.eamilVerificationStatus = eamilVerificationStatus;
    }


    public void UpdateDetails(UserDto newUserDto)
    {

        if (newUserDto.getFirstName().isEmpty() || newUserDto.getLastName().isEmpty())
            throw new UserServiceException(ErrorMessageTypes.MISSING_REQUIRED_FIELD.getErrorMessage());

        setFirstName(newUserDto.getFirstName());
        setLastName(newUserDto.getLastName());
    }

    public List<AddressEntity> getAddresses()
    {
        return addresses;
    }

    public void setAddresses(List<AddressEntity> addresses)
    {
        this.addresses = addresses;
    }
}
