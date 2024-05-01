package com.valanse.valanse.repository.jpa;
import com.valanse.valanse.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}

