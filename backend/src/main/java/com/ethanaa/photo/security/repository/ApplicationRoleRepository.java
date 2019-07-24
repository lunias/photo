package com.ethanaa.photo.security.repository;


import com.ethanaa.photo.security.entity.ApplicationRole;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ApplicationRoleRepository extends PagingAndSortingRepository<ApplicationRole, String> {

    Optional<ApplicationRole> findByApplicationNameAndRoleName(String applicationName, String roleName);
}
