package quiz.demo.web.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import quiz.demo.data.model.AuthenticatedUser;
import quiz.demo.data.model.Question;
import quiz.demo.data.model.Quiz;
import quiz.demo.data.model.support.Response;
import quiz.demo.data.model.support.Result;
import quiz.demo.service.service.LogService;
import quiz.demo.service.service.QuestionService;
import quiz.demo.service.service.QuizService;
import quiz.demo.web.utils.RestVerifier;
import quiz.demo.web.view.controller.BaseController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(RestQuizController.ROOT_MAPPING)
public class RestQuizController extends BaseController {

    public static final String ROOT_MAPPING = "/api/quizzes";

    private static final Logger logger = LoggerFactory.getLogger(RestQuizController.class);


    private QuizService quizService;
    private QuestionService questionService;


    @Autowired
    public RestQuizController(LogService log, QuizService quizService, QuestionService questionService) {
        super();
        this.quizService = quizService;
        this.questionService = questionService;
    }

    @GetMapping(value = "")
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.OK)
    public Page<Quiz> findAll(Pageable pageable,
                              @RequestParam(required = false, defaultValue = "false") Boolean published) {

        if (published) {
            //if is not logged user

            Page<Quiz> allPublished = quizService.findAllPublished(pageable);
            logger.debug("The Quiz's are view! Loaded " + allPublished.getTotalElements() + " quizzes");

            return allPublished;
        } else {
            return quizService.findAll(pageable);
        }
    }

    @GetMapping(value = "/search")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    public Page<Quiz> searchAll(Pageable pageable, @RequestParam(required = true) String filter,
                                @RequestParam(required = false, defaultValue = "false") Boolean onlyValid) {

        return quizService.search(filter, pageable);
    }

    @PostMapping(value = "")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public Quiz save(@AuthenticationPrincipal AuthenticatedUser user, @Valid Quiz quiz, BindingResult result) {

        logger.debug("The Quiz " + quiz.getName() + " is going to be created");

        RestVerifier.verifyModelResult(result);

        return quizService.save(quiz, user.getUser());
    }

    @GetMapping(value = "/{quiz_id}")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    public Quiz find(@PathVariable Long quiz_id) {

        return quizService.find(quiz_id);
    }

    @PostMapping(value = "/{quiz_id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public Quiz update(@PathVariable Long quiz_id, @Valid Quiz quiz, BindingResult result, Principal principal) {

        RestVerifier.verifyModelResult(result);

        quiz.setId(quiz_id);
        log(principal,"User update quiz with id = "+quiz_id);
        return quizService.update(quiz);
    }

    @RequestMapping(value = "/{quiz_id}", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long quiz_id) {
        Quiz quiz = quizService.find(quiz_id);
        quizService.delete(quiz);
    }

    @GetMapping(value = "/{quiz_id}/questions")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    public List<Question> findQuestions(@PathVariable Long quiz_id,
                                        @RequestParam(required = false, defaultValue = "false") Boolean onlyValid) {

        Quiz quiz = quizService.find(quiz_id);

        if (onlyValid) {
            return questionService.findValidQuestionsByQuiz(quiz);
        } else {
            return questionService.findQuestionsByQuiz(quiz);
        }

    }

    @PostMapping(value = "/{quiz_id}/publish")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void publishQuiz(@PathVariable long quiz_id,Principal principal) {
        Quiz quiz = quizService.find(quiz_id);
        quizService.publishQuiz(quiz);
        log(principal,principal.getName() +" publish quiz id = "+ quiz_id);
    }

    @PostMapping(value = "/{quiz_id}/submitAnswers")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    public Result playQuiz(@PathVariable long quiz_id, @RequestBody List<Response> answersBundle, Principal principal) {

        Quiz quiz = quizService.find(quiz_id);
        log(principal,""+quiz_id);
        return quizService.checkAnswers(quiz, answersBundle);
    }



}
