package com.neonflame.projectQ.service;

import com.neonflame.projectQ.dto.RegistrationUserDto;
import com.neonflame.projectQ.excrptions.user.InvalidActivationCodeException;
import com.neonflame.projectQ.excrptions.user.InvalidEmailFormatException;
import com.neonflame.projectQ.excrptions.user.UserEmailExistsException;
import com.neonflame.projectQ.excrptions.user.UserUsernameExistsException;
import com.neonflame.projectQ.model.Role;
import com.neonflame.projectQ.model.User;
import com.neonflame.projectQ.repository.UserRepo;
import com.neonflame.projectQ.utility.EmailValidator;
import com.neonflame.projectQ.utility.MyMailSender;
import com.neonflame.projectQ.utility.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final MyMailSender mailSender;

    public UserService(UserRepo userRepo, MyMailSender mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    public User register (RegistrationUserDto registrationUserDto) {
        if (!EmailValidator.isValid(registrationUserDto.email))
            throw new InvalidEmailFormatException("Invalid email format");
        if (userRepo.findByEmail(registrationUserDto.email.toLowerCase()) != null)
            throw new UserEmailExistsException("User with this email exists");
        if (userRepo.findByUsername(registrationUserDto.username.toLowerCase()) != null)
            throw new UserUsernameExistsException("User with this username exists");
        com.neonflame.projectQ.model.User user = new com.neonflame.projectQ.model.User();
        user.setEmail(registrationUserDto.email);
        user.setUsername(registrationUserDto.username);
        user.setPassword(new PasswordEncoder().encode(registrationUserDto.password));
        user.getRoles().add(Role.USER);
        user.setActive(true);
        user.setActivationToken(UUID.randomUUID().toString());
        userRepo.save(user);
        // TODO ENABLE AFTER DEBUG
        //sentVerificationToken(user);
        return user;
    }

    public void sentVerificationToken (User user) {
        mailSender.send(
                user.getEmail(),
                "Activate your account",
                String.format("Your activation link + \n " +
                        "http://localhost:8080/api/v1/registration/activate?username=%s&token=%s",
                        user.getUsername(), user.getActivationToken()));
    }

    public boolean activate(String username, String activationToken) {
        User user = userRepo.findByUsernameAndActivationToken(username, activationToken);
        if (user == null)
            throw new InvalidActivationCodeException("Invalid activation token");
        user.setActivationToken(null);
        userRepo.save(user);
        return true;
    }
}
