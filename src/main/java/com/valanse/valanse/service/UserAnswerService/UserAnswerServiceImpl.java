package com.valanse.valanse.service.UserAnswerService;

import com.valanse.valanse.entity.Quiz;
import com.valanse.valanse.repository.jpa.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAnswerServiceImpl implements UserAnswerService {

    private UserAnswerRepository userAnswerRepository;

    @Override
    public List<Quiz> getUserPreferences(Integer userId, int preference) {
        return userAnswerRepository.findByUserIdAndPreferenceGreaterThanEqual(userId, preference);
    }
}
