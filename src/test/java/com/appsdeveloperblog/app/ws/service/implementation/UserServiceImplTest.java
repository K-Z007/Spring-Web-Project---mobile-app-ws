package com.appsdeveloperblog.app.ws.service.implementation;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceImplTest
{
    //This annotation is used to create an instance of the class being tested (UserServiceImpl in this case) and inject mock objects into its dependencies.
    @InjectMocks
    UserServiceImpl userService;

    // This annotation is used to create a mock. It’s a way to create a new class instance.
    //ock objects mimic real objects but allow you to control their behavior during tests, isolating the component under test from external dependencies
    @Mock
    UserRepository userRepository;

    @BeforeEach //marked with @BeforeEach, indicating it will run before each test.
    void setUp()
    {
        //Mockito will initialize the mocks in the current test instance. This ensures
        // that the mocks are fresh and not carrying any state over from previous tests.
        // This is a common pattern in unit testing with Mockito. It helps to keep tests
        // isolated and avoid unexpected behavior due to shared state.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser()
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Kevin");
        userEntity.setUserId("hhty57ehfyfdf");
        userEntity.setEncryptedPassword("74dsfojh54");

        //indicates userRepository.findByEmail() to receive a string input
        //then to return above userEntity object:
        /*Using Mockito to fake the findByEmail() of the userRepository.
        It’s saying “whenever findByEmail is called with any string as input, return the userEntity we just created”.
        This allows the test to control the behavior of the userRepository during this test.
        It instructs the test framework to intercept calls to userRepository.findByEmail() with any string argument and return the predefined userEntity object.
        This simulates data retrieval without actually interacting with a database.*/
        when( userRepository.findByEmail( anyString())).thenReturn( userEntity);

      UserDto userDto = userService.getUser("test@test.com");

      assertNotNull(userDto);
      assertEquals("Kevin", userDto.getFirstName());

    }

    @Test
    void testGetUsers() {
        // Arrange
        int page = 1;
        int limit = 5;

        // Create a list of UserEntity objects
        List<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId((long) i);
            userEntity.setFirstName("Test User " + i);
            userEntity.setUserId("testUser" + i);
            userEntity.setEncryptedPassword("testPassword" + i);
            userEntities.add(userEntity);
        }

        // Create a Page object
        Page<UserEntity> pageObj = new PageImpl<>(userEntities);

        // intercept the userRepository.findAll method:
        //any(PageRequest.class) is an argument matcher provided by the Mockito library.
        //to match any instance of the PageRequest class when the findAll method of the userRepository is invoked
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(pageObj);

        // Act:
        List<UserDto> userDtos = userService.getUsers(page, limit);

        // Assert
        assertNotNull(userDtos);
        assertEquals(limit, userDtos.size());
        for (int i = 0; i < limit; i++) {
            assertEquals(userEntities.get(i).getFirstName(), userDtos.get(i).getFirstName());
        }
    }

}