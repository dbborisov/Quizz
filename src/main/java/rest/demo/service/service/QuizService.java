package rest.demo.service.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rest.demo.data.model.Quiz;
import rest.demo.data.model.User;
import rest.demo.data.model.support.Response;
import rest.demo.data.model.support.Result;
import rest.demo.exceptions.ResourceUnavailableException;
import rest.demo.exceptions.UnauthorizedActionException;

import java.util.List;

public interface QuizService {
	Quiz save(Quiz quiz, User user);

	Page<Quiz> findAll(Pageable pageable);

	Page<Quiz> findAllPublished(Pageable pageable);

	Page<Quiz> findQuizzesByUser(User user, Pageable pageable);

	Quiz find(Long id) throws ResourceUnavailableException;

	Quiz update(Quiz quiz) throws ResourceUnavailableException, UnauthorizedActionException;

	void delete(Quiz quiz) throws ResourceUnavailableException, UnauthorizedActionException;

	Page<Quiz> search(String query, Pageable pageable);

	Result checkAnswers(Quiz quiz, List<Response> answersBundle);

	void publishQuiz(Quiz quiz);
}
