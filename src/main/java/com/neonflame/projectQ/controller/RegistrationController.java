package com.neonflame.projectQ.controller;

import com.neonflame.projectQ.dto.RegistrationUserDto;
import com.neonflame.projectQ.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/registration")
//@PreAuthorize("permitAll()")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?> registration (@RequestBody RegistrationUserDto registrationUserDto) {

        userService.register(registrationUserDto);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("activate")
    public ResponseEntity<Object> activate (@RequestParam("username") String username,
                                            @RequestParam("token") String token) {
        if (userService.activate(username, token))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
