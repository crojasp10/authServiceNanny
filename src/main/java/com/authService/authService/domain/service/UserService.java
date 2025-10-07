package com.authService.authService.domain.service;


import com.authService.authService.infraestructure.out.UserEntity;

import java.util.List;

public interface UserService {

    List<UserEntity> findAll();

    UserEntity save(UserEntity user);

    boolean existsByUsername(String username);
}

