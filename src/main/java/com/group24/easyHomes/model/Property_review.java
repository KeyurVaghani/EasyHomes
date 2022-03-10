package com.group24.easyHomes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name= "Property_review")
public class Property_review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int review_id;

    @Column(name = "customer_id")
    private String customer_id;
    @Column(name = "review_subject")
    private String review_subject;
    @Column(name = "review_description")
    private String review_description;
    @Column(name = "review_rating")
    private int review_rating;
    @Column(name = "review_given_time")
    private String review_given_time;



    public Property_review() {

    }


    public Property_review(String customer_id, String review_subject, String review_description, int review_rating,
            String review_given_time) {
        this.customer_id = customer_id;
        this.review_subject = review_subject;
        this.review_description = review_description;
        this.review_rating = review_rating;
        this.review_given_time = review_given_time;
    }


    public int getReview_id() {
        return review_id;
    }
    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }
    public String getCustomer_id() {
        return customer_id;
    }
    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
    public String getReview_subject() {
        return review_subject;
    }
    public void setReview_subject(String review_subject) {
        this.review_subject = review_subject;
    }
    public String getReview_description() {
        return review_description;
    }
    public void setReview_description(String review_description) {
        this.review_description = review_description;
    }
    public int getReview_rating() {
        return review_rating;
    }
    public void setReview_rating(int review_rating) {
        this.review_rating = review_rating;
    }
    public String getReview_given_time() {
        return review_given_time;
    }
    public void setReview_given_time(String review_given_time) {
        this.review_given_time = review_given_time;
    }
    
}
