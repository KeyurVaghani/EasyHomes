package com.group24.easyHomes.repository;

import com.group24.easyHomes.model.Property_review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface Property_reviewRepository extends JpaRepository<Property_review, Long>{
    
}
