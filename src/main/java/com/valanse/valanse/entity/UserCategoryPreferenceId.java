package com.valanse.valanse.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserCategoryPreferenceId implements Serializable {
    private Integer user_id;
    private String category;


}
