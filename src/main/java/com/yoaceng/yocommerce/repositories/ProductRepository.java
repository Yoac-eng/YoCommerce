package com.yoaceng.yocommerce.repositories;

import com.yoaceng.yocommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
