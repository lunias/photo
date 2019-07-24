package com.ethanaa.photo.security.repository;


import com.ethanaa.photo.security.entity.Application;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ApplicationRepository extends PagingAndSortingRepository<Application, String> {

    Optional<Application> findByName(String name);
}
