package quiz.demo.service.service.impl;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import quiz.demo.data.model.Question;
import quiz.demo.data.model.Quiz;
import quiz.demo.data.model.User;
import quiz.demo.data.model.support.Response;
import quiz.demo.data.model.support.Result;
import quiz.demo.data.repository.QuizRepository;
import quiz.demo.exceptions.ActionRefusedException;
import quiz.demo.exceptions.InvalidParametersException;
import quiz.demo.exceptions.ResourceUnavailableException;
import quiz.demo.exceptions.UnauthorizedActionException;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.LogService;
import quiz.demo.service.service.QuestionService;
import quiz.demo.service.service.QuizService;

import java.util.List;

@Service
public class QuizServiceImpl  implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);
    private QuizRepository quizRepository;
    private final ModelMapper modelMapper;
    private  final LogService log;
    private QuestionService questionService;

    @Autowired
    public QuizServiceImpl(QuizRepository quizRepository, ModelMapper modelMapper, LogService log, QuestionService questionService) {
        this.quizRepository = quizRepository;
        this.modelMapper = modelMapper;
        this.log = log;
        this.questionService = questionService;
    }

    @Override
    public Quiz save(Quiz quiz, User user) {
        quiz.setCreatedBy(user);
        return quizRepository.save(quiz);
    }

    @Override
    public Page<Quiz> findAll(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    @Override
    public Page<Quiz> findAllPublished(Pageable pageable) {
        System.out.println(pageable);
        Page<Quiz> byIsPublishedTrue = quizRepository.findByIsPublishedTrue(pageable);
        System.out.println(byIsPublishedTrue.getContent().size());
        return byIsPublishedTrue;
    }

    @Override
    public Quiz find(Long id) throws ResourceUnavailableException {
        Quiz quiz = quizRepository.findById(id).orElse(null);

        if (quiz == null) {
            logger.error("Quiz " + id + " not found");
            throw new ResourceUnavailableException("Quiz " + id + " not found");
        }

        return quiz;
    }

    @Override
    public Quiz update(Quiz newQuiz) throws UnauthorizedActionException, ResourceUnavailableException {
        Quiz currentQuiz = find(newQuiz.getId());

        mergeQuizzes(currentQuiz, newQuiz);
        return quizRepository.save(currentQuiz);
    }

    @Override
    public void delete(Quiz quiz) throws ResourceUnavailableException, UnauthorizedActionException {
        quizRepository.delete(quiz);
    }

    private void mergeQuizzes(Quiz currentQuiz, Quiz newQuiz) {
        currentQuiz.setName(newQuiz.getName());
        currentQuiz.setDescription(newQuiz.getDescription());
    }

    @Override
    public Page<Quiz> search(String query, Pageable pageable) {
        return quizRepository.searchByName(query, pageable);
    }

    @Override
    public Page<Quiz> findQuizzesByUser(UserServiceModel user, Pageable pageable) {
        return quizRepository.findByCreatedBy(modelMapper.map(user,User.class), pageable);
    }

    @Override
    public Result checkAnswers(Quiz quiz, List<Response> answersBundle) {
        Result results = new Result();

        for (Question question : quiz.getQuestions()) {
            boolean isFound = false;

            if (!question.getIsValid()) {
                continue;
            }

            for (Response bundle : answersBundle) {
                if (bundle.getQuestion().equals(question.getId())) {
                    isFound = true;

                    results.addAnswer(questionService.checkIsCorrectAnswer(question, bundle.getSelectedAnswer()));
                    break;
                }
            }

            if (!isFound) {
                throw new InvalidParametersException("No answer found for question: " + question.getText());
            }
        }

        return results;
    }

    @Override
    public void publishQuiz(Quiz quiz) {
        int count = questionService.countValidQuestionsInQuiz(quiz);

        if (count > 0) {
            quiz.setIsPublished(true);
            quizRepository.save(quiz);

        } else {

            throw new ActionRefusedException("The quiz doesn't have any valid questions");
        }
    }

}
