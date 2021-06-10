package com.neonflame.projectQ.controller;

import com.neonflame.projectQ.Dao.RegistrationUserDao;
import com.neonflame.projectQ.Utility.EmailValidator;
import com.neonflame.projectQ.Utility.PasswordEncoder;
import com.neonflame.projectQ.model.Role;
import com.neonflame.projectQ.model.User;
import com.neonflame.projectQ.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/registration")
public class registrationController {

    private final UserRepo userRepo;

    public registrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("")
    public ResponseEntity<?> registration (@RequestBody RegistrationUserDao registrationUserDao) {
        if (!EmailValidator.isValid(registrationUserDao.email))
            throw new IllegalStateException("Invalid email format");
        if (userRepo.findByEmail(registrationUserDao.email.toLowerCase()) != null)
            throw new IllegalStateException("User with this email exists");
        if (userRepo.findByUsername(registrationUserDao.username.toLowerCase()) != null)
            throw new IllegalStateException("User with this username exists");
        User user = new User();
        user.setEmail(registrationUserDao.email);
        user.setUsername(registrationUserDao.username);
        user.setPassword(new PasswordEncoder().encode(registrationUserDao.password));
        user.getRoles().add(Role.USER);
        userRepo.save(user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
