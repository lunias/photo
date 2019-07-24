package com.ethanaa.photo.security.service;


import com.ethanaa.photo.security.controller.exception.ApplicationNotFoundException;
import com.ethanaa.photo.security.entity.Application;
import com.ethanaa.photo.security.entity.ApplicationRole;
import com.ethanaa.photo.security.entity.Role;
import com.ethanaa.photo.security.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {

        this.applicationRepository = applicationRepository;
    }

    public Application create(Application application) {

        return applicationRepository.save(application);
    }

    public Page<Application> getAll(Pageable pageable) {

        return applicationRepository.findAll(pageable);
    }

    public Application get(String id) {

        Optional<Application> application = applicationRepository.findById(id);

        if (!application.isPresent()) throw new ApplicationNotFoundException(id);

        return application.get();
    }

    public Application getByName(String name) {

        return applicationRepository.findByName(name).orElseThrow(
                () -> new ApplicationNotFoundException(name));
    }

    public List<Role> getRoles(String id) {

        return get(id).getApplicationRoles().stream()
                .map(ApplicationRole::getRole)
                .collect(Collectors.toList());
    }
}

