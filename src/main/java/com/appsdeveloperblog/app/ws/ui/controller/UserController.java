package com.appsdeveloperblog.app.ws.ui.controller;


import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.requests.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")  //http://localhost:8080/users
public class UserController
{
    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    //The @PathVariable annotation is used to extract the value from the URI.
    //It is most suitable for the RESTful web service where the URL contains some value.
    //produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} indicates what
    //format will be for the returned data for response (first one is the default format)
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id)
    {
        UserRest returnedUser = new UserRest();

        UserDto userDto = userService.getByUserId(id);

        BeanUtils.copyProperties(userDto, returnedUser);

        return returnedUser;
    }

    @GetMapping( produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value="page", defaultValue="1") int page,
                                   @RequestParam(value="limit", defaultValue="25") int limit)
    {
        List<UserRest> returnedUsers = new ArrayList<>();

        List<UserDto> userDtos = userService.getUsers(page, limit);

        for (UserDto userDto :userDtos)
        {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(userDto, userRest);
            returnedUsers.add(userRest);
        }

        return returnedUsers;
    }

    //lcoalhost:8080/mobile-app-ws/users/id/addresses
    @GetMapping(path="/{userId}/addresses",
               produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CollectionModel<AddressRest> getUserAddresses(@PathVariable String userId)
    {
        List<AddressRest> returnValues = new ArrayList<>();

        List<AddressDTO> addressesDTO = addressService.getAddresses(userId);

        /* Type listType = new TypeToken<List<AddressRest>>(){}.getType();
        - This line is creating a TypeToken object that holds the type information for List<AddressRest>.
        This is necessary because Java generics don’t maintain their type information at runtime due to a
        feature called type erasure. The TypeToken object allows you to store this type information for
        use at runtime.
        returnValue = new ModelMapper().map(addressesDTO, listType); - This line is using the ModelMapper
        library to map the addressesDTO object to a new object of type List<AddressRest>. */
        if(addressesDTO != null && !addressesDTO.isEmpty()) {
            Type listType = new TypeToken<List<AddressRest>>(){}.getType();
            returnValues = new ModelMapper().map(addressesDTO, listType);

            //add each address link to its returned corresponding data;
            for (AddressRest addressRest: returnValues)
            {
                Link addressLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .getUserAddress(userId, addressRest.getAddressId())).withSelfRel();

                addressRest.add(addressLink);
            }
        }

        //adding lists of other related apis:
        Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUser(userId))
                        .withRel("user");

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUserAddresses(userId))
                        .withSelfRel();

        return CollectionModel.of(returnValues, userLink, selfLink);
    }

    //lcoalhost:8080/mobile-app-ws/users/userId/addresses/addressId
    @GetMapping(path="/{userId}/addresses/{addressId}",
            produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public EntityModel<AddressRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId)
    {
        //AddressDTO addressesDTO = addressService.getAddress(addressId);

        AddressDTO addressesDTO = addressService.getUserAddress(userId, addressId);

        AddressRest returnValue = new ModelMapper().map(addressesDTO, AddressRest.class);

        //adding lists of other related apis:
        Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUser(userId))
                .withRel("user");

        Link userAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserAddresses(userId))
                .withRel("addresses");

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUserAddress(userId, addressId))
                .withSelfRel();

        return EntityModel.of(returnValue, userLink, userAddressesLink, selfLink);
    }

    //@RequestBody-> is whatever being passed into the request body will be mapped into the User object and fill in each fields;
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception
    {
        if (userDetails.getLastName().isEmpty())
            throw new UserServiceException(ErrorMessageTypes.MISSING_REQUIRED_FIELD.getErrorMessage());

        //copy corresponding properties from userDeatils obj into userDto obj.
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        //userDto is shared object to transfer data from UI Level (userDetails) to Service Layer.
        //From userDto, to transfer data into userService.createUser(userDto) method to put new user info into the database:
        UserDto createdUser = userService.createUser(userDto);

        //UserRest is the User model to be returned to UI client front-end to display non-sensitive info.
        UserRest returnedUser = new UserRest();

        //get a returned UserDto "createdUser" after saving into database, then copy allowed(non-sensitive)
        // info to return to UI client (front-end);
//        BeanUtils.copyProperties(createdUser, returnedUser);
        returnedUser = modelMapper.map(createdUser, UserRest.class);

        return returnedUser;
    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest updateUser(@PathVariable String id,
                               @RequestBody UserDetailsRequestModel userDetails)
    {
        UserRest returnedUser = new UserRest();

        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUserDetails = userService.updateUser(id, userDto);

        BeanUtils.copyProperties(updatedUserDetails, returnedUser);

        return returnedUser;
    }

    @DeleteMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id)
    {
        OperationStatusModel operationStatus = new OperationStatusModel();

        operationStatus.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        operationStatus.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return operationStatus;
    }


}
