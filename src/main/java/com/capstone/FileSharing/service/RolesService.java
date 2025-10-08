package com.capstone.FileSharing.service;

import org.springframework.stereotype.Service;

import com.capstone.FileSharing.model.Role;
import com.capstone.FileSharing.repository.RolesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RolesRepository rolesRepository;

    // @PostConstruct
    // public void init() {
    //     rolesRepository.saveAll(
    //         List.of(
    //             new Role("ADMIN"),
    //             new Role("USER")
    //         )
    //     );
    // }

    public Role getAdminRole() {
        return  rolesRepository.findById(1L).get();
    }

    public Role getUserRole() {
        return  rolesRepository.findById(2L).get();
    }
}
