package com.group24.easyHomes.model;

public class FavoriteProperty {
    private Long userId;
    private int propertyId;

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
}
