package com.authService.authService.domain.port;

import com.authService.authService.infraestructure.out.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RoleEntity,Long> {

    Optional<RoleEntity> findByName(String name);

}
