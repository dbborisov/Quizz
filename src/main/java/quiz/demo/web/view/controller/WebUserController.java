package quiz.demo.web.view.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import quiz.demo.service.service.UserService;

@Controller
@RequestMapping("/user")
public class WebUserController {


	private final UserService userService;

	@Autowired
	public WebUserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = "/{user_id}/quizzes")
	@PreAuthorize("permitAll")
	public String getQuizzesForUser(@PathVariable Long user_id) {
		userService.find(user_id);

		// TODO: Unimplemented
		return "error";
	}

	@GetMapping(value = "/quizzes")
	@PreAuthorize("isAuthenticated()")
	public String getQuizzesForAuthenticatedUser() {
		return "myQuizzes";
	}
}
