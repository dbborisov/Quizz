package quiz.demo.service.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quiz.demo.data.model.Answer;
import quiz.demo.data.model.Question;
import quiz.demo.data.model.Quiz;
import quiz.demo.data.repository.QuestionRepository;
import quiz.demo.exceptions.ActionRefusedException;
import quiz.demo.exceptions.ResourceUnavailableException;
import quiz.demo.exceptions.UnauthorizedActionException;
import quiz.demo.service.service.AnswerService;
import quiz.demo.service.service.QuestionService;

import java.util.List;

@Service("QuestionService")
@Transactional
public class QuestionServiceImpl implements QuestionService {

	private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
	private QuestionRepository questionRepository;

	private AnswerService answerService;

	@Autowired
	public QuestionServiceImpl(QuestionRepository questionRepository, AnswerService answerService) {
		this.questionRepository = questionRepository;
		this.answerService = answerService;
	}

	@Override
	public Question save(Question question) throws UnauthorizedActionException {
		int count = questionRepository.countByQuiz(question.getQuiz());
		question.setOrder(count + 1);

		return questionRepository.save(question);
	}

	@Override
	public Question find(Long id) throws ResourceUnavailableException {
		Question question = questionRepository.findById(id).orElse(null);

		if (question == null) {
			logger.error("Question " + id + " not found");
			throw new ResourceUnavailableException("Question " + id + " not found");
		}

		return question;
	}

	@Override
	public Question update(Question newQuestion) throws ResourceUnavailableException, UnauthorizedActionException {
		Question currentQuestion = find(newQuestion.getId());

		mergeQuestions(currentQuestion, newQuestion);
		return questionRepository.save(currentQuestion);
	}

	@Override
	public void delete(Question question) throws ResourceUnavailableException, UnauthorizedActionException {
		Quiz quiz = question.getQuiz();

		if (quiz.getIsPublished() && question.getIsValid() && countValidQuestionsInQuiz(quiz) <= 1) {
			throw new ActionRefusedException("A published Quiz can't contain less than one valid question");
		}

		questionRepository.delete(question);
	}

	private void mergeQuestions(Question currentQuestion, Question newQuestion) {
		currentQuestion.setText(newQuestion.getText());

		if (newQuestion.getOrder() != null)
			currentQuestion.setOrder(newQuestion.getOrder());
	}

	@Override
	public Boolean checkIsCorrectAnswer(Question question, Long answer_id) {
		if (!question.getIsValid() || question.getCorrectAnswer() == null) {
			return false;
		}

		return question.getCorrectAnswer().getId().equals(answer_id);
	}

	@Override
	public List<Question> findQuestionsByQuiz(Quiz quiz) {
		return questionRepository.findByQuizOrderByOrderAsc(quiz);
	}

	@Override
	public List<Question> findValidQuestionsByQuiz(Quiz quiz) {
		return questionRepository.findByQuizAndIsValidTrueOrderByOrderAsc(quiz);
	}

	@Override
	public void setCorrectAnswer(Question question, Answer answer) {
		question.setCorrectAnswer(answer);
		save(question);
	}

	@Override
	public Answer addAnswerToQuestion(Answer answer, Question question) {
		int count = answerService.countAnswersInQuestion(question);
		Answer newAnswer = updateAndSaveAnswer(answer, question, count);

		checkQuestionInitialization(question, count, newAnswer);

		return newAnswer;
	}

	private void checkQuestionInitialization(Question question, int count, Answer newAnswer) {
		checkAndUpdateQuestionValidity(question, true);
		setCorrectAnswerIfFirst(question, count, newAnswer);
	}

	private Answer updateAndSaveAnswer(Answer answer, Question question, int count) {
		answer.setOrder(count + 1);
		answer.setQuestion(question);
		return answerService.save(answer);
	}

	private void checkAndUpdateQuestionValidity(Question question, boolean newState) {
		if (!question.getIsValid()) {
			question.setIsValid(newState);
			save(question);
		}
	}

	private void setCorrectAnswerIfFirst(Question question, int count, Answer newAnswer) {
		if (count == 0) {
			question.setCorrectAnswer(newAnswer);
			questionRepository.save(question);
		}
	}

	@Override
	public int countQuestionsInQuiz(Quiz quiz) {
		return questionRepository.countByQuiz(quiz);
	}

	@Override
	public int countValidQuestionsInQuiz(Quiz quiz) {
		return questionRepository.countByQuizAndIsValidTrue(quiz);
	}

	@Override
	public Answer getCorrectAnswer(Question question) {
		return question.getCorrectAnswer();
	}

}
