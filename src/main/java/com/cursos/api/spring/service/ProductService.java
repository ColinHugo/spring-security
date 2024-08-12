package com.cursos.api.spring.service;

import com.cursos.api.spring.dto.SaveProduct;
import com.cursos.api.spring.persistence.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Page< Product > findAll( Pageable pageable );

    Optional< Product > findOneById( Long productId );

    Product createOne( SaveProduct saveProduct );

    Product updateOneById( Long productId, SaveProduct saveProduct );

    Product disableOneById( Long productId );

}