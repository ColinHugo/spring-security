package com.cursos.api.spring.persistence.repository;

import com.cursos.api.spring.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository< Category, Long > {
}