package com.cursos.api.spring.persistence.repository;

import com.cursos.api.spring.persistence.entity.security.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationRepository extends JpaRepository< Operation, Long > {

    @Query( "SELECT o FROM Operation o WHERE o.permitAll =  true" )
    List< Operation > findByPublicAccess();

}