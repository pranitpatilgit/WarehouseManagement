package com.pranitpatil.dao;

import com.pranitpatil.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Override
    Optional<Product> findById(Long s);

    @Override
    Page<Product> findAll(Pageable pageable);
}
