package quiz.demo.service.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quiz.demo.data.model.Scores;
import quiz.demo.data.repository.ScoreRepository;
import quiz.demo.service.model.ScoreServiceModel;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.ScoreService;
import quiz.demo.service.service.UserService;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ScoreServiceImpl implements ScoreService {

    private  final UserService userService;
    private  final ScoreRepository scoreRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ScoreServiceImpl(UserService userService, ScoreRepository scoreRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.scoreRepository = scoreRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ScoreServiceModel save(ScoreServiceModel scoreServiceModel) {
        if(!scoreServiceModel.getUsername().equals("anonymous")) {
            UserServiceModel userName = this.userService.findByUsername(scoreServiceModel.getUsername());
            scoreServiceModel.setUserId(userName.getId());
        }else{
            scoreServiceModel.setUserId(-1L);
        }
        this.scoreRepository.save(modelMapper.map(scoreServiceModel, Scores.class));

        return scoreServiceModel;
    }

    @Override
    public List<ScoreServiceModel> findAllByQuizId(Long id) {
        List<Scores> allByQuizId = scoreRepository.findAllByQuizId(id);
        if(allByQuizId.size() == 0){
            return null;
        }
        return allByQuizId.stream().map(e->this.modelMapper.map(e,ScoreServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public List<ScoreServiceModel> findAllByUserId(Long id) {
        List<Scores> allByUserId = scoreRepository.findAllByUserId(id);
        if(allByUserId.size() == 0){
            return null;
        }
        return allByUserId.stream().map(e->this.modelMapper.map(e,ScoreServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public List<ScoreServiceModel> findAll() {

        return this.scoreRepository.findAll().stream().map(e->this.modelMapper.map(e,ScoreServiceModel.class)).collect(Collectors.toList());
    }
}
