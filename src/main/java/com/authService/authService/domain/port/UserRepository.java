package com.authService.authService.domain.port;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.authService.authService.infraestructure.out.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    UserEntity save(UserEntity user);

}
