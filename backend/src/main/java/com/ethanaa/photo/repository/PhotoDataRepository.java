package com.ethanaa.photo.repository;

import com.ethanaa.photo.model.PhotoData;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhotoDataRepository extends PagingAndSortingRepository<PhotoData, UUID> {

}
