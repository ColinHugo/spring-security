package com.cursos.api.spring.service.impl;

import com.cursos.api.spring.dto.SaveProduct;
import com.cursos.api.spring.exception.ObjectNotFoundException;
import com.cursos.api.spring.persistence.entity.Category;
import com.cursos.api.spring.persistence.entity.Product;
import com.cursos.api.spring.persistence.repository.ProductRepository;
import com.cursos.api.spring.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page< Product > findAll( Pageable pageable ) {
        return productRepository.findAll( pageable );
    }

    @Override
    public Optional< Product > findOneById( Long productId ) {
        return productRepository.findById( productId );
    }

    @Override
    public Product createOne( SaveProduct saveProduct ) {

        Product product = new Product();
        product.setName( saveProduct.getName() );
        product.setPrice( saveProduct.getPrice() );
        product.setStatus( Product.ProductStatus.ENABLED );

        Category category = new Category();
        category.setId( saveProduct.getCategoryId() );
        product.setCategory( category );

        return productRepository.save( product );

    }

    @Override
    public Product updateOneById( Long productId, SaveProduct saveProduct ) {

        Product productFromDB = productRepository.findById( productId )
                .orElseThrow( () -> new ObjectNotFoundException( "Product not found with id: " + productId ) );

        productFromDB.setName( saveProduct.getName() );
        productFromDB.setPrice( saveProduct.getPrice() );

        Category category = new Category();
        category.setId( saveProduct.getCategoryId() );
        productFromDB.setCategory( category );

        return productRepository.save( productFromDB );

    }

    @Override
    public Product disableOneById( Long productId ) {

        Product productFromDB = productRepository.findById( productId )
                .orElseThrow( () -> new ObjectNotFoundException( "Product not found with id: " + productId ) );

        productFromDB.setStatus( Product.ProductStatus.DISABLED );

        return productRepository.save( productFromDB );

    }

}