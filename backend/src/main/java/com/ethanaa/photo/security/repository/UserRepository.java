package com.ethanaa.photo.security.repository;

import com.ethanaa.photo.security.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface UserRepository extends PagingAndSortingRepository<User, String> {

    Optional<User> findByUsername(String username);
}
