package com.karainc.dailytalk.domain.category.repository;


import com.karainc.dailytalk.domain.category.entitiy.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category ,Long> {
    List<Category> findAll();
    Category findByCategoryName(String category);

    Category findByCategoryId(Long categoryId);
}
