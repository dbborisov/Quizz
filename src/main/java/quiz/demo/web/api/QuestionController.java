package quiz.demo.web.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import quiz.demo.data.model.Answer;
import quiz.demo.data.model.Question;
import quiz.demo.data.model.Quiz;
import quiz.demo.service.service.AnswerService;
import quiz.demo.service.service.QuestionService;
import quiz.demo.service.service.QuizService;
import quiz.demo.web.utils.RestVerifier;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(QuestionController.ROOT_MAPPING)
public class QuestionController {

	public static final String ROOT_MAPPING = "/api/questions";

	@Autowired
	private QuestionService questionService;

	@Autowired
	private QuizService quizService;

	@Autowired
	private AnswerService answerService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
//	@ResponseStatus(HttpStatus.CREATED)
	public Question save(@Valid Question question, BindingResult result, @RequestParam Long quiz_id) {

		RestVerifier.verifyModelResult(result);

		Quiz quiz = quizService.find(quiz_id);
		question.setQuiz(quiz);

		return questionService.save(question);
	}

	@RequestMapping(value = "/updateAll", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void updateAll(@RequestBody List<Question> questions) {
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			question.setOrder(i + 1);

			questionService.update(question);
		}
	}

	@RequestMapping(value = "/{question_id}", method = RequestMethod.GET)
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public Question find(@PathVariable Long question_id) {

		return questionService.find(question_id);
	}

	@RequestMapping(value = "/{question_id}", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public Question update(@PathVariable Long question_id, @Valid Question question, BindingResult result) {

		RestVerifier.verifyModelResult(result);

		question.setId(question_id);
		return questionService.update(question);

	}

	@RequestMapping(value = "/{question_id}", method = RequestMethod.DELETE)
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long question_id) {
		Question question = questionService.find(question_id);
		questionService.delete(question);
	}

	@RequestMapping(value = "/{question_id}/answers", method = RequestMethod.GET)
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public List<Answer> findAnswers(@PathVariable Long question_id) {
		Question question = questionService.find(question_id);
		return answerService.findAnswersByQuestion(question);
	}

	@RequestMapping(value = "/{question_id}/correctAnswer", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public Answer getCorrectAnswer(@PathVariable Long question_id) {
		Question question = questionService.find(question_id);
		return questionService.getCorrectAnswer(question);
	}

	@RequestMapping(value = "/{question_id}/correctAnswer", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void setCorrectAnswer(@PathVariable Long question_id, @RequestParam Long answer_id) {

		Question question = questionService.find(question_id);
		Answer answer = answerService.find(answer_id);
		questionService.setCorrectAnswer(question, answer);
	}

}
