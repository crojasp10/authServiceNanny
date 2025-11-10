package com.authService.authService.domain.port;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.authService.authService.infraestructure.out.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);

}
