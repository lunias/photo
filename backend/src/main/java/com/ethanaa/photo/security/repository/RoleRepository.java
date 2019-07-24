package com.ethanaa.photo.security.repository;


import com.ethanaa.photo.security.entity.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
