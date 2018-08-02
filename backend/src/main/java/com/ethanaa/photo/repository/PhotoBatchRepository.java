package com.ethanaa.photo.repository;

import com.ethanaa.photo.model.PhotoBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PhotoBatchRepository extends PagingAndSortingRepository<PhotoBatch, String> {

    Page<PhotoBatch> findByUsername(@Param("username") String username, Pageable pageable);
}
