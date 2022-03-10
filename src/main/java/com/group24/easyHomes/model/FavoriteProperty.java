package com.group24.easyHomes.model;

import javax.persistence.*;

@Entity
@Table(name = "FavoriteProperty")
public class FavoriteProperty {
    private Long userId;
    private int propertyId;

    @Id
    @GeneratedValue
    private Integer id;

    public FavoriteProperty(Long userId, int propertyId) {
        this.userId = userId;
        this.propertyId = propertyId;
    }

    public FavoriteProperty() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
