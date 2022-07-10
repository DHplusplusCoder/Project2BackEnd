package com.revature.repositories;

import com.revature.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("FROM Product WHERE featured = 'true'")
    List<Product> getFeaturedProducts();

    @Query("FROM Product WHERE sale > 0")
    List<Product> getProductsOnSale();
}
