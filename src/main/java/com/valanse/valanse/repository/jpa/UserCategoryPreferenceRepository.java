package com.valanse.valanse.repository.jpa;

import com.valanse.valanse.entity.UserCategoryPreference;
import com.valanse.valanse.entity.UserCategoryPreferenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCategoryPreferenceRepository extends JpaRepository<UserCategoryPreference, UserCategoryPreferenceId> {
}
