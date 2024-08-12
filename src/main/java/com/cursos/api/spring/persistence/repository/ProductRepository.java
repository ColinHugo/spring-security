package com.cursos.api.spring.persistence.repository;

import com.cursos.api.spring.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository< Product, Long > {
}