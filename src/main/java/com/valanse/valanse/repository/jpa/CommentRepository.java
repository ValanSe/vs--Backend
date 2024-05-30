package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
