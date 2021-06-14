package com.neonflame.projectQ.service;

import com.neonflame.projectQ.dao.RegistrationUserDao;
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

    public User register (RegistrationUserDao registrationUserDao) {
        if (!EmailValidator.isValid(registrationUserDao.email))
            throw new IllegalStateException("Invalid email format");
        if (userRepo.findByEmail(registrationUserDao.email.toLowerCase()) != null)
            throw new IllegalStateException("User with this email exists");
        if (userRepo.findByUsername(registrationUserDao.username.toLowerCase()) != null)
            throw new IllegalStateException("User with this username exists");
        com.neonflame.projectQ.model.User user = new com.neonflame.projectQ.model.User();
        user.setEmail(registrationUserDao.email);
        user.setUsername(registrationUserDao.username);
        user.setPassword(new PasswordEncoder().encode(registrationUserDao.password));
        user.getRoles().add(Role.USER);
        user.setActive(true);
        user.setActivationToken(UUID.randomUUID().toString());
        userRepo.save(user);
        sentVerificationToken(user);
        return user;
    }

    public void sentVerificationToken (User user) {
        mailSender.sent(
                user.getEmail(),
                "Activate your account",
                String.format("Your activation link + \n " +
                        "http://localhost:8080/api/v1/registration/activate?username=%s&token=%s",
                        user.getUsername(), user.getActivationToken()));
    }

    public boolean activateToken (String username, String activationToken) {
        User user = userRepo.findByUsernameAndActivationToken(username, activationToken);
        if (user == null)
            throw new IllegalStateException("Invalid activation token");
        user.setActivationToken(null);
        userRepo.save(user);
        return true;
    }
}
