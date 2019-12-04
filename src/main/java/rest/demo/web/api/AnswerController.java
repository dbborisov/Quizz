package rest.demo.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rest.demo.data.model.Answer;
import rest.demo.data.model.Question;
import rest.demo.service.service.AnswerService;
import rest.demo.service.service.QuestionService;
import rest.demo.web.utils.RestVerifier;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(AnswerController.ROOT_MAPPING)
public class AnswerController {

	public static final String ROOT_MAPPING = "/api/answers";

	@Autowired
	AnswerService answerService;

	@Autowired
	QuestionService questionService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.CREATED)
	public Answer save(@Valid Answer answer, BindingResult result, @RequestParam long question_id) {

		RestVerifier.verifyModelResult(result);

		Question question = questionService.find(question_id);
		return questionService.addAnswerToQuestion(answer, question);
	}

	@RequestMapping(value = "/updateAll", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void updateAll(@RequestBody List<Answer> answers) {
		for (int i = 0; i < answers.size(); i++) {
			Answer answer = answers.get(i);
			answer.setOrder(i + 1);

			answerService.update(answer);
		}
	}

	@RequestMapping(value = "/{answer_id}", method = RequestMethod.GET)
	@PreAuthorize("permitAll")
	@ResponseStatus(HttpStatus.OK)
	public Answer find(@PathVariable Long answer_id) {

		return answerService.find(answer_id);
	}

	@RequestMapping(value = "/{answer_id}", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public Answer update(@PathVariable Long answer_id, @Valid Answer answer, BindingResult result) {

		RestVerifier.verifyModelResult(result);

		answer.setId(answer_id);
		return answerService.update(answer);
	}

	@RequestMapping(value = "/{answer_id}", method = RequestMethod.DELETE)
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long answer_id) {
		Answer answer = answerService.find(answer_id);
		answerService.delete(answer);
	}
}
