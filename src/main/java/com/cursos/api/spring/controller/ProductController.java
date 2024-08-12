package com.cursos.api.spring.controller;

import com.cursos.api.spring.dto.SaveProduct;
import com.cursos.api.spring.persistence.entity.Product;
import com.cursos.api.spring.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping( "/products" )
public class ProductController {

    private final ProductService productService;

    // @PreAuthorize( "hasAnyRole( 'ADMINISTRATOR', 'ASSISTANT_ADMINISTRATOR' )" )
    @PreAuthorize( "hasAuthority( 'READ_ALL_PRODUCTS' )" )
    @GetMapping
    public ResponseEntity< Page< Product > > findAll( Pageable pageable ) {

        Page< Product > productPage = productService.findAll( pageable );

        if ( productPage.hasContent() ) {
            return ResponseEntity.ok( productPage );
        }

        return ResponseEntity.notFound().build();

    }

    // @PreAuthorize( "hasAnyRole( 'ADMINISTRATOR', 'ASSISTANT_ADMINISTRATOR' )" )
    @PreAuthorize( "hasAuthority( 'READ_ONE_PRODUCT' )" )
    @GetMapping( "/{productId}" )
    public ResponseEntity< Product > findOneById( @PathVariable Long productId ) {

        Optional< Product > product = productService.findOneById( productId );

        /* if ( product.isPresent() ) {
            return ResponseEntity.ok( product.get() );
        } */

        // return ResponseEntity.notFound().build();

        return product
                .map( ResponseEntity::ok )
                .orElseGet( () -> ResponseEntity.notFound().build() );

    }

    // @PreAuthorize( "hasRole( 'ADMINISTRATOR' )" )
    @PreAuthorize( "hasAuthority( 'CREATE_ONE_PRODUCT' )" )
    @PostMapping
    public ResponseEntity< Product > createOne( @Valid @RequestBody SaveProduct saveProduct ) {

        Product product = productService.createOne( saveProduct );

        return ResponseEntity.status( HttpStatus.CREATED ).body( product );

    }

    // @PreAuthorize( "hasAnyRole( 'ADMINISTRATOR', 'ASSISTANT_ADMINISTRATOR' )" )
    @PreAuthorize( "hasAuthority( 'UPDATE_ONE_PRODUCT' )" )
    @PutMapping( "/{productId}" )
    public ResponseEntity< Product > updateOneById( @PathVariable Long productId, @Valid @RequestBody SaveProduct saveProduct ) {

        Product product = productService.updateOneById( productId, saveProduct );

        return ResponseEntity.ok( product );

    }

    // @PreAuthorize( "hasRole( 'ADMINISTRATOR' )" )
    @PreAuthorize( "hasAuthority( 'DISABLE_ONE_PRODUCT' )" )
    @PutMapping( "/{productId}/disabled" )
    public ResponseEntity< Product > disableOneById( @PathVariable Long productId ) {

        Product product = productService.disableOneById( productId );

        return ResponseEntity.ok( product );

    }

}