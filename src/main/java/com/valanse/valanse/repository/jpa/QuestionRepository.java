package com.valanse.valanse.repository.jpa;
import com.valanse.valanse.entity.Category;
import com.valanse.valanse.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

}
