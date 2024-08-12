package com.cursos.api.spring.service.impl;

import com.cursos.api.spring.dto.SaveCategory;
import com.cursos.api.spring.exception.ObjectNotFoundException;
import com.cursos.api.spring.persistence.entity.Category;
import com.cursos.api.spring.persistence.repository.CategoryRepository;
import com.cursos.api.spring.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Page< Category > findAll( Pageable pageable ) {
        return categoryRepository.findAll( pageable );
    }

    @Override
    public Optional< Category > findOneById( Long categoryId ) {
        return categoryRepository.findById( categoryId );
    }

    @Override
    public Category createOne( SaveCategory saveCategory ) {

        Category category = new Category();
        category.setName( saveCategory.getName() );
        category.setStatus( Category.CategoryStatus.ENABLED );

        return categoryRepository.save( category );

    }

    @Override
    public Category updateOneById( Long categoryId, SaveCategory saveCategory ) {

        Category categoryFromDB = categoryRepository.findById( categoryId )
                .orElseThrow( () -> new ObjectNotFoundException( "Category not found with id: " + categoryId ) );
        categoryFromDB.setName( saveCategory.getName() );

        return categoryRepository.save( categoryFromDB );

    }

    @Override
    public Category disableOneById( Long categoryId ) {

        Category categoryFromDB = categoryRepository.findById( categoryId )
                .orElseThrow( () -> new ObjectNotFoundException( "Category not found with id: " + categoryId ) );
        categoryFromDB.setStatus( Category.CategoryStatus.DISABLED );

        return categoryRepository.save( categoryFromDB );

    }

}