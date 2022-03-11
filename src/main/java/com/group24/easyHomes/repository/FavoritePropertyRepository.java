package com.group24.easyHomes.repository;

import com.group24.easyHomes.model.FavoriteProperty;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FavoritePropertyRepository extends JpaRepository<FavoriteProperty, Long> {
}
