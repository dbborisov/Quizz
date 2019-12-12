package quiz.demo.web.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import quiz.demo.data.model.AuthenticatedUser;
import quiz.demo.data.model.Quiz;
import quiz.demo.data.model.User;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.QuizService;
import quiz.demo.service.service.RegistrationService;
import quiz.demo.service.service.UserManagementService;
import quiz.demo.service.service.UserService;
import quiz.demo.web.utils.RestVerifier;

import javax.validation.Valid;

@RestController
@RequestMapping(RestUserController.ROOT_MAPPING)
public class RestUserController {

	public static final String ROOT_MAPPING = "/api/users";
	private static final Logger logger = LoggerFactory.getLogger(RestUserController.class);
	private RegistrationService registrationService;

	private UserManagementService userManagementService;

	private UserService userService;

	private QuizService quizService;

	@Autowired
	public RestUserController(RegistrationService registrationService, UserManagementService userManagementService, UserService userService, QuizService quizService) {
		this.registrationService = registrationService;
		this.userManagementService = userManagementService;
		this.userService = userService;
		this.quizService = quizService;
	}

	@PostMapping(value = "/registration")
	@PreAuthorize("permitAll")
	public ResponseEntity<UserServiceModel> signUp(@Valid UserServiceModel user, BindingResult result) {

		RestVerifier.verifyModelResult(result);
		UserServiceModel newUser = registrationService.startRegistration(user);

		if (registrationService.isRegistrationCompleted(newUser)) {
			return new ResponseEntity<UserServiceModel>(newUser, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<UserServiceModel>(newUser, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/{user_id}/continueRegistration")
	@PreAuthorize("permitAll")
	public ResponseEntity<UserServiceModel> nextRegistrationStep(@PathVariable Long user_id, String token) {
		UserServiceModel user = userService.find(user_id);
		registrationService.continueRegistration(user, token);

		if (registrationService.isRegistrationCompleted(user)) {
			return new ResponseEntity<UserServiceModel>(user, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<UserServiceModel>(user, HttpStatus.OK);
		}
	}

	@DeleteMapping(value = "/{user_id}")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long user_id) {

		userService.delete(user_id);
	}

	@GetMapping(value = "/{user_id}/quizzes")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public Page<Quiz> getQuizzesByUser(Pageable pageable, @PathVariable Long user_id) {
		logger.debug("Requested page " + pageable.getPageNumber() + " from user " + user_id);

		UserServiceModel user = userService.find(user_id);
		return quizService.findQuizzesByUser(user, pageable);
	}
	
	@GetMapping(value = "/myQuizzes")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public Page<Quiz> getQuizzesByCurrentUser(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                              Pageable pageable) {
		logger.debug("Requested page " + pageable.getPageNumber() + " from user " + authenticatedUser.getUsername());
		
		return getQuizzesByUser(pageable, authenticatedUser.getId());
	}
	
	@RequestMapping(value = "/login")
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.OK)
	public User login(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
		logger.debug("Logged in as " + authenticatedUser.getUsername()+" and role "+ authenticatedUser.getAuthorities().toString()+" user has role "
				+ authenticatedUser.getUser().getRole().name()) ;
		return authenticatedUser.getUser();
	}

	@RequestMapping(value = "/logoutDummy")
	@PreAuthorize("permitAll()")
	@ResponseStatus(HttpStatus.OK)
	public void logout() {
		// Dummy endpoint to point Spring Security to
		logger.debug("Logged out");
	}
	
	@RequestMapping(value = "/forgotPassword")
	@PreAuthorize("permitAll()")
	@ResponseStatus(HttpStatus.OK)
	public UserServiceModel forgotPassword(String email) {
		UserServiceModel user = userService.findByEmail(email);
		userManagementService.resendPassword(user);
		
		return user;
	}



}
