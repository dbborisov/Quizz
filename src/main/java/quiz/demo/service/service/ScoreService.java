package quiz.demo.service.service;

import org.springframework.stereotype.Service;
import quiz.demo.service.model.ScoreServiceModel;

import java.util.List;


public interface ScoreService {
    ScoreServiceModel save(ScoreServiceModel scoreServiceModel);
    List<ScoreServiceModel> findAllByQuizId(Long id);
    List<ScoreServiceModel> findAllByUserId(Long id);
    List<ScoreServiceModel> findAll();
}
