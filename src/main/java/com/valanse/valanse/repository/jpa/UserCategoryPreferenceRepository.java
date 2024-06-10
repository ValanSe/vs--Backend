package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.UserCategoryPreference;
import com.valanse.valanse.entity.UserCategoryPreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCategoryPreferenceRepository extends JpaRepository<UserCategoryPreference, UserCategoryPreferenceId> {
    Optional<UserCategoryPreference> findByCategory(String category);
    @Query("SELECT ucp FROM UserCategoryPreference ucp WHERE ucp.category IN :categories")
    List<UserCategoryPreference> findByCategories(List<String> categories);


    @Modifying
    @Transactional
    @Query("UPDATE UserCategoryPreference ucp " +
            "SET " +
            "ucp.answerCount = ucp.answerCount + 1, " +
            "ucp.totalPreference = ucp.totalPreference + :preference " +
            "WHERE ucp.userId = :userId AND ucp.category IN :categories")
    void updatePreferences(Integer userId, List<String> categories, Integer preference);

    @Modifying
    @Transactional
    @Query("UPDATE UserCategoryPreference ucp " +
            "SET ucp.commentCount = ucp.commentCount + 1 " +
            "WHERE ucp.userId = :userId AND ucp.category IN :categories")
    void incrementCommentCounts(Integer userId, List<String> categories);

    @Modifying
    @Transactional
    @Query("UPDATE UserCategoryPreference ucp " +
            "SET ucp.registrationCount = ucp.registrationCount + 1 " +
            "WHERE ucp.userId = :userId AND ucp.category IN :categories")
    void incrementRegistrationCounts(Integer userId, List<String> categories);

}