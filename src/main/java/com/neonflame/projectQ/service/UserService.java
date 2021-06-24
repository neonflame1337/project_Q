package com.neonflame.projectQ.service;

import com.neonflame.projectQ.dto.user.RegistrationUserDto;
import com.neonflame.projectQ.exceptions.user.InvalidActivationCodeException;
import com.neonflame.projectQ.exceptions.user.InvalidEmailFormatException;
import com.neonflame.projectQ.exceptions.user.UserEmailExistsException;
import com.neonflame.projectQ.exceptions.user.UserUsernameExistsException;
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

    /**
     * Registers user in the system
     * @param registrationUserDto DTO object with the necessary information for user registration
     * @throws InvalidEmailFormatException if user's email has invalid form
     * @throws UserEmailExistsException if user with email already exists
     * @throws UserUsernameExistsException if user with username already exists
     * @return registered user
     */
    public User register (RegistrationUserDto registrationUserDto) {
        if (!EmailValidator.isValid(registrationUserDto.getEmail()))
            throw new InvalidEmailFormatException("Invalid email format");
        if (userRepo.findByEmail(registrationUserDto.getEmail().toLowerCase()) != null)
            throw new UserEmailExistsException("User with this email exists");
        if (userRepo.findByUsername(registrationUserDto.getUsername().toLowerCase()) != null)
            throw new UserUsernameExistsException("User with this username exists");
        com.neonflame.projectQ.model.User user = new com.neonflame.projectQ.model.User();
        user.setEmail(registrationUserDto.getEmail());
        user.setUsername(registrationUserDto.getUsername());
        user.setPassword(new PasswordEncoder().encode(registrationUserDto.getPassword()));
        user.getRoles().add(Role.USER);
        user.setActive(true);
        user.setActivationToken(UUID.randomUUID().toString());
        userRepo.save(user);
        sentVerificationToken(user);
        return user;
    }

    /**
     * Sends to user email with verification token
     * @param user
     */
    public void sentVerificationToken (User user) {
        mailSender.send(
                user.getEmail(),
                "Activate your account",
                String.format("Your activation link + \n " +
                        "http://localhost:8080/api/v1/registration/activate?username=%s&token=%s",
                        user.getUsername(), user.getActivationToken()));
    }

    /**
     * Activates the user by verification token
     * @param username user's username
     * @param activationToken user's activation token
     * @throws InvalidActivationCodeException if user with activationToken not found
     * @return true if user was activated successfully
     */
    public boolean activate(String username, String activationToken) {
        User user = userRepo.findByUsernameAndActivationToken(username, activationToken);
        if (user == null)
            throw new InvalidActivationCodeException("Invalid activation token");
        user.setActivationToken(null);
        userRepo.save(user);
        return true;
    }
}
