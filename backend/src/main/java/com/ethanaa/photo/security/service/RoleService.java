package com.ethanaa.photo.security.service;

import com.ethanaa.photo.security.controller.exception.RoleNotFoundException;
import com.ethanaa.photo.security.entity.Role;
import com.ethanaa.photo.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {

        this.roleRepository = roleRepository;
    }

    public Role create(Role role) {

        return roleRepository.save(role);
    }

    public Iterable<Role> create(Role... roles) {

        return roleRepository.saveAll(Arrays.asList(roles));
    }

    public Page<Role> getAll(Pageable pageable) {

        return roleRepository.findAll(pageable);
    }

    public Role get(Long id) {

        Optional<Role> role = roleRepository.findById(id);

        if (!role.isPresent()) throw new RoleNotFoundException(id);

        return role.get();
    }

    public Role getByName(String name) {

        return roleRepository.findByName(name).orElseThrow(
                () -> new RoleNotFoundException(name));
    }
}
