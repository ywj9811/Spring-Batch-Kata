package com.batch.kata.repository;

import com.batch.kata.domain.EditProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditProductRepository extends JpaRepository<EditProduct, Long> {
}
