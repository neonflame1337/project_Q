package com.neonflame.projectQ.service;

import com.neonflame.projectQ.dto.RegistrationUserDto;
import com.neonflame.projectQ.model.User;
import com.neonflame.projectQ.repository.UserRepo;
import com.neonflame.projectQ.utility.MyMailSender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepo userRepo;
    @MockBean
    private MyMailSender mailSender;

    private static RegistrationUserDto registrationUserDto;

    @BeforeAll
    static void setUp() {
        registrationUserDto = new RegistrationUserDto();
        registrationUserDto.username = "test";
        registrationUserDto.email = "test@test.test";
        registrationUserDto.password = "testPass";
    }

    @Test
    public void registerUser() {
        assertNotNull(userService.register(registrationUserDto));
        verify(userRepo, times(1)).save(ArgumentMatchers.any(User.class));
        verify(mailSender, times(1))
                .send(
                ArgumentMatchers.eq(registrationUserDto.email),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString());
    }

    @Test
    public void registerUserFailExistingUsername() {

        doReturn(new User())
                .when(userRepo)
                .findByUsername(registrationUserDto.username);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.register(registrationUserDto); });

        verify(userRepo, times(0)).save(ArgumentMatchers.any(User.class));
        verify(mailSender, times(0))
                .send(
                        ArgumentMatchers.eq(registrationUserDto.email),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString());
    }

    @Test
    public void registerUserFailExistingEmail() {
        doReturn(new User())
                .when(userRepo)
                .findByEmail(registrationUserDto.email);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.register(registrationUserDto); });

        verify(userRepo, times(0)).save(ArgumentMatchers.any(User.class));
        verify(mailSender, times(0))
                .send(
                        ArgumentMatchers.eq(registrationUserDto.email),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString());
    }

    @Test
    public void activate() {
        User user = new User();
        user.setUsername(registrationUserDto.username);
        user.setActivationToken("token");

        doReturn(user)
                .when(userRepo)
                .findByUsernameAndActivationToken(
                        user.getUsername(),
                        user.getActivationToken());

        userService.activate(user.getUsername(), user.getActivationToken());
        assertNull(user.getActivationToken());
    }

    public void activateFail() {
        User user = new User();
        user.setUsername(registrationUserDto.username);
        user.setActivationToken("token");

        doReturn(user)
                .when(userRepo)
                .findByUsernameAndActivationToken(
                        user.getUsername(),
                        user.getActivationToken());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.activate(user.getUsername(), "wrong token");
        });
        assertNotNull(user.getActivationToken());
    }
}