package com.ethanaa.photo.repository;

import com.ethanaa.photo.model.Photo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends PagingAndSortingRepository<Photo, String> {


}
