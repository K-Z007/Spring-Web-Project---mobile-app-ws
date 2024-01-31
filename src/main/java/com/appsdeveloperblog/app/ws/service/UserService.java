package com.appsdeveloperblog.app.ws.service;

import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


//UserService to extend UserDetailsService so can be used in authenticationProvider() in WebSecurity class;
//UserDetailsService is an interface in spring security:
public interface UserService extends UserDetailsService
{
    UserDto createUser(UserDto userDto);
    UserDto getUser(String email);
    UserDto getByUserId(String userId);
    UserDto updateUser(String userId, UserDto userDto);
    void deleteUser(String userId);

    List<UserDto> getUsers(int page, int limit);
}
