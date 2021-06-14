package com.neonflame.projectQ.controller;

import com.neonflame.projectQ.dao.RegistrationUserDao;
import com.neonflame.projectQ.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/registration")
//@PreAuthorize("permitAll()")
public class registrationController {

    private final UserService userService;

    public registrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?> registration (@RequestBody RegistrationUserDao registrationUserDao) {

        userService.register(registrationUserDao);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("activate")
    public ResponseEntity<Object> activate (@RequestParam("username") String username,
                                            @RequestParam("token") String token) {
        if (userService.activateToken(username, token))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
