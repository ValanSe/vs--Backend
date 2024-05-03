package com.valanse.valanse.service.UserAnswerService;

import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.repository.jpa.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAnswerServicelmpl implements UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Override
    public List<Quiz> getUserPreferences(Integer userId, int preference) {
        return userAnswerRepository.findByUserIdAndPreferenceGreaterThanEqual(userId, preference);
    }
}
