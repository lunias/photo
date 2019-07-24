package com.ethanaa.photo.repository;

import com.ethanaa.photo.entity.PhotoBatch;
import com.ethanaa.photo.entity.PhotoBatchId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PhotoBatchRepository extends PagingAndSortingRepository<PhotoBatch, PhotoBatchId> {

    Page<PhotoBatch> findByIdUsername(@Param("username") String username, Pageable pageable);
}
