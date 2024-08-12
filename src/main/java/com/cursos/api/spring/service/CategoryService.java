package com.cursos.api.spring.service;

import com.cursos.api.spring.dto.SaveCategory;
import com.cursos.api.spring.persistence.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {

    Page< Category > findAll( Pageable pageable );

    Optional< Category > findOneById( Long categoryId );

    Category createOne( SaveCategory saveCategory );

    Category updateOneById( Long categoryId, SaveCategory saveCategory );

    Category disableOneById( Long categoryId );

}