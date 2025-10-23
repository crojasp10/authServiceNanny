package com.authService.authService.domain.port;


import com.authService.authService.infraestructure.out.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity,Long> {

    Optional<UserEntity> findByUsername(String username);
    UserEntity save(UserEntity user);

}
