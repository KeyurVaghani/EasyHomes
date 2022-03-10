package com.group24.easyHomes.controller;

import com.group24.easyHomes.repository.Property_reviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group24.easyHomes.model.Property_review;
import com.group24.easyHomes.repository.Property_reviewRepository;

import java.util.List;

@RestController
@RequestMapping("api/")
public class Property_reviewController {
    
    @Autowired
    private Property_reviewRepository property_reviewRepository;
    
    
    @GetMapping("Property_review")
    public List<Property_review> getProperty_review(){
        return this.property_reviewRepository.findAll();
    }
}
