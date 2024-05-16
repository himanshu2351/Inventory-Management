package com.inventory.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.management.dto.enums.ProductStatus;
import com.inventory.management.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE product SET quantity = :newQuantity, version = :newVersion,status = :productStatus  WHERE id = :productId and version = :oldVersion",
            nativeQuery = true)
    void updateProductQuantity(@Param("newQuantity") float newQuantity, @Param("productId") long productId,
                                  @Param("oldVersion") int oldVersion, @Param("newVersion") int newVersion, String productStatus);
}
