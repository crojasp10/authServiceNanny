package com.authService.authService.domain.service;

import java.util.List;

import com.authService.authService.infraestructure.out.UserEntity;

public interface UserService {

    List<UserEntity> findAll();

    UserEntity save(UserEntity user);

    boolean existsByUsername(String username);
}
