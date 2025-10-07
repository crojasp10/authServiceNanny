package com.authService.authService.infraestructure.out.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authService.authService.domain.service.UserService;
import com.authService.authService.infraestructure.out.UserEntity;

@RestController
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping
    public List<UserEntity> getAllUsers(){
       return userService.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/registerAdmin")
    public ResponseEntity<UserEntity> saveUser( @RequestBody UserEntity userEntity){

       return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userEntity));

    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register( @RequestBody UserEntity userEntity){
      userEntity.setAdmin(false);
       return saveUser(userEntity);

    }


}
