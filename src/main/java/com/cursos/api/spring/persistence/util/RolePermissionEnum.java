package com.cursos.api.spring.persistence.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RolePermissionEnum {

    READ_ALL_PRODUCTS( "READ_ALL_PRODUCTS" ),
    READ_ONE_PRODUCT( "READ_ONE_PRODUCT" ),
    CREATE_ONE_PRODUCT( "CREATE_ONE_PRODUCT" ),
    UPDATE_ONE_PRODUCT( "UPDATE_ONE_PRODUCT" ),
    DISABLE_ONE_PRODUCT( "DISABLE_ONE_PRODUCT" ),

    READ_ALL_CATEGORIES( "READ_ALL_CATEGORIES" ),
    READ_ONE_CATEGORY( "READ_ONE_CATEGORY" ),
    CREATE_ONE_CATEGORY( "CREATE_ONE_CATEGORY" ),
    UPDATE_ONE_CATEGORY( "UPDATE_ONE_CATEGORY" ),
    DISABLE_ONE_CATEGORY( "DISABLE_ONE_CATEGORY" ),

    READ_MY_PROFILE( "READ_MY_PROFILE" );

    private final String permission;

}