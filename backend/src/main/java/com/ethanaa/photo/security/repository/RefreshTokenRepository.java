package com.ethanaa.photo.security.repository;


import com.ethanaa.photo.security.entity.RefreshToken;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RefreshTokenRepository extends PagingAndSortingRepository<RefreshToken, String> {

    RefreshToken findByUsername(String username);
}
