package com.ethanaa.photo.security.service;

import com.ethanaa.photo.security.controller.exception.ApplicationRoleNotFoundException;
import com.ethanaa.photo.security.entity.ApplicationRole;
import com.ethanaa.photo.security.repository.ApplicationRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationRoleService {

    private ApplicationRoleRepository applicationRoleRepository;

    @Autowired
    public ApplicationRoleService(ApplicationRoleRepository applicationRoleRepository) {

        this.applicationRoleRepository = applicationRoleRepository;
    }

    public ApplicationRole create(ApplicationRole applicationRole) {

        return applicationRoleRepository.save(applicationRole);
    }

    public Page<ApplicationRole> getAll(Pageable pageable) {

        return applicationRoleRepository.findAll(pageable);
    }

    public ApplicationRole get(String id) {

        Optional<ApplicationRole> applicationRole = applicationRoleRepository.findById(id);

        if (!applicationRole.isPresent()) throw new ApplicationRoleNotFoundException(id);

        return applicationRole.get();
    }

    public ApplicationRole getByApplicationNameAndRoleName(String applicationName, String roleName) {

        return applicationRoleRepository.findByApplicationNameAndRoleName(applicationName, roleName)
                .orElseThrow(() -> new ApplicationRoleNotFoundException(applicationName + " " + roleName));
    }

}
