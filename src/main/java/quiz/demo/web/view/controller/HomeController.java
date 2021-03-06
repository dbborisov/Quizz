package quiz.demo.web.view.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import quiz.demo.data.model.AuthenticatedUser;
import quiz.demo.data.model.Question;
import quiz.demo.data.model.Quiz;
import quiz.demo.exceptions.ModelVerificationException;
import quiz.demo.service.accesscontrol.AccessControlService;
import quiz.demo.service.service.QuestionService;
import quiz.demo.service.service.QuizService;
import quiz.demo.web.utils.RestVerifier;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class HomeController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

   private  final QuizService quizService;

   private  final QuestionService questionService;

   private  final AccessControlService<Quiz> accessControlServiceQuiz;

   private  final AccessControlService<Question> accessControlServiceQuestion;

   @Autowired
    public HomeController(QuizService quizService, QuestionService questionService, AccessControlService<Quiz> accessControlServiceQuiz, AccessControlService<Question> accessControlServiceQuestion) {
       super();

       this.quizService = quizService;
        this.questionService = questionService;
        this.accessControlServiceQuiz = accessControlServiceQuiz;
        this.accessControlServiceQuestion = accessControlServiceQuestion;
    }

    @GetMapping(value = "/")
    public ModelAndView home() {
        ModelAndView model = new ModelAndView("home");

        return model;
    }


    @GetMapping(value = "/createQuiz")
    @PreAuthorize("isAuthenticated()")

    public String newQuiz(Map<String, Object> model) {

      logger.debug("Entering the creat Quiz GetMapping");
        return "quiz/createQuiz";
    }

    @PostMapping(value = "/createQuiz")
    @PreAuthorize("isAuthenticated()")
    public String newQuiz(@AuthenticationPrincipal AuthenticatedUser user, @Valid Quiz quiz, BindingResult result,
                          Map<String, Object> model) {
        Quiz newQuiz;

        try {
            RestVerifier.verifyModelResult(result);
            newQuiz = quizService.save(quiz, user.getUser());
        } catch (ModelVerificationException e) {
            return "quiz/createQuiz";
        }

        return "redirect:/editQuiz/" + newQuiz.getId();
    }

    @GetMapping(value = "/editQuiz/{quiz_id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editQuiz(@PathVariable long quiz_id) {
        Quiz quiz = quizService.find(quiz_id);
        accessControlServiceQuiz.canCurrentUserUpdateObject(quiz);

        ModelAndView mav = new ModelAndView();
        mav.addObject("quiz", quiz);
        mav.setViewName("quiz/editQuiz");

        return mav;
    }

    @GetMapping(value = "/editAnswer/{question_id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editAnswer(@PathVariable long question_id) {
        Question question = questionService.find(question_id);
        accessControlServiceQuestion.canCurrentUserUpdateObject(question);

        ModelAndView mav = new ModelAndView();
        mav.addObject("question", question);
        mav.setViewName("quiz/editAnswers");

        return mav;
    }

    @GetMapping(value = "/quiz/{quiz_id}")
    @PreAuthorize("permitAll")
    public ModelAndView getQuiz(@PathVariable long quiz_id) {
        Quiz quiz = quizService.find(quiz_id);

        ModelAndView mav = new ModelAndView();
        mav.addObject("quiz", quiz);
        mav.setViewName("quiz/quizView");

        return mav;
    }

    @GetMapping(value = "/quiz/{quiz_id}/play")
    @PreAuthorize("permitAll")
    public ModelAndView playQuiz(@PathVariable long quiz_id) {
        Quiz quiz = quizService.find(quiz_id);

        ModelAndView mav = new ModelAndView();
        mav.addObject("quiz", quiz);
        mav.setViewName("quiz/playQuiz");

        return mav;
    }
}
