package com.authService.authService.application.service;

import com.authService.authService.domain.port.RoleRepository;
import com.authService.authService.domain.port.UserRepository;
import com.authService.authService.domain.service.UserService;
import com.authService.authService.infraestructure.out.RoleEntity;
import com.authService.authService.infraestructure.out.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        return (List<UserEntity>) userRepository.findAll();
    }

    @Override
    @Transactional
    public UserEntity save(UserEntity user) {

        Optional<RoleEntity> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<RoleEntity> roles = new ArrayList();
        optionalRoleUser.ifPresent(roles::add);


        if(user.isAdmin()){
            Optional<RoleEntity> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
