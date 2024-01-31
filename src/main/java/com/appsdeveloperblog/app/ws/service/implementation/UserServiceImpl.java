package com.appsdeveloperblog.app.ws.service.implementation;

import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessageTypes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


//UserService's implementation: to checking, validating passed User details and also for encrypting.
@Service   //@Service is special type of @Component, it annotates classes at the service layer.
public class UserServiceImpl implements UserService
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    final int ID_LENGTH = 30;

    @Override
    public UserDto createUser(UserDto userDto)
    {
        //checking duplicated email address from database befre creating new user:
        if(userRepository.findByEmail(userDto.getEmail()) != null)
            throw new RuntimeException("This email has already been used.");

        //set the AddressDTO with the UserDto and id;
        for (int i = 0; i < userDto.getAddresses().size(); i++)
        {
            AddressDTO addressDTO = userDto.getAddresses().get(i);
            addressDTO.setUserDetails(userDto);
            addressDTO.setAddressId(utils.generateAddressID(ID_LENGTH));
            userDto.getAddresses().set(i, addressDTO);
        }

        //UserService to get transferred data from userDto, then map the data into userEntity;
        ModelMapper modelMapper = new ModelMapper();
        //NOTE: all userDto and addressDTO's fields'names must match the filed names in UserEntity and AddressEntity to let map() work:
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        String publicUerID = utils.generateUserID(ID_LENGTH);
        userEntity.setUserId(publicUerID);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        //then userRepository will put userEntity into the database to create new user.
        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnedUserDto = modelMapper.map(storedUserDetails, UserDto.class);

        return returnedUserDto;
    }


    //this is a method from implements interface "UserDetailsService"
    //this method is called by  getAuthenticationManager().authenticate(...) in class "AuthenticationFilter"
    @Override    //here "username" uses email;
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {   //Explain:
        //when a customer tries to login from UI, it will pass username and password,
        //then the AuthenticationManager in WebSecurity will get these customer inputted password (
        // then use the "bCryptPasswordEncoder" provided to AuthenticationManager to encode the inputted password);
        //Then SpringSecurity framework will compare with the above returned UserEntity username and EncryptedPassword
        // with the encoded inputted password to check if it is the same user;

        //username for users are their emails:
        UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity == null) throw new UsernameNotFoundException(username);

        //User() obj is the class from springframework.security package:
        //The following third argument is the list of granted authorities (roles and permissions) for this User;
        //Result will be passed onto getAuthenticationManager().authenticate(...) in class "AuthenticationFilter" to do security check
        return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUser(String email)
    {
        //username for users are their emails:
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) throw new UsernameNotFoundException(email);

        UserDto returnedUserDto = new UserDto();
        BeanUtils.copyProperties(userEntity, returnedUserDto);

        return returnedUserDto;
    }

    @Override
    public UserDto getByUserId(String userId)
    {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UserServiceException(ErrorMessageTypes.NO_RECORD_FOUND.getErrorMessage());

        UserDto returnedUserDto = new UserDto();
        BeanUtils.copyProperties(userEntity, returnedUserDto);

        return returnedUserDto;
    }

    @Override
    public UserDto updateUser(String userId, UserDto newUserDto)
    {
        UserDto returnedUserDto = new UserDto();

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UserServiceException(ErrorMessageTypes.NO_RECORD_FOUND.getErrorMessage());

        userEntity.UpdateDetails(newUserDto);

        UserEntity updatedUserDetails = userRepository.save(userEntity);

        BeanUtils.copyProperties(updatedUserDetails, returnedUserDto);

        return returnedUserDto;
    }

    @Override
    public void deleteUser(String userId)
    {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UserServiceException(ErrorMessageTypes.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);
    }

    //Due to UserRepository implemented interface "PagingAndSortingRepository", it can use
    //findAll() method, to accept "Pageable" inteface obj, thus pageRequest can be used to pass
    //the "page" and "limit" params to the function;
    @Override
    public List<UserDto> getUsers(int page, int limit)
    {
        //make sure page number starts with 1 not 0;
        if(page >0) page--;

        List<UserDto> userDtosList = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, limit);

        Page<UserEntity> pageObj = userRepository.findAll(pageRequest);

        List<UserEntity> userEntities = pageObj.getContent();

        for (UserEntity userEntity: userEntities)
        {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            userDtosList.add(userDto);
        }

        return userDtosList;
    }

}
