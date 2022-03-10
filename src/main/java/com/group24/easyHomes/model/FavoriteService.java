package com.group24.easyHomes.model;

public class FavoriteService {
    private Long userId;
    private int serviceId;

    public FavoriteService(Long userId, int serviceId) {
        this.userId = userId;
        this.serviceId = serviceId;
    }

    public FavoriteService() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}
