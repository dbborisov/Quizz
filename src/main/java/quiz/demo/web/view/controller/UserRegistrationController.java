package quiz.demo.web.view.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import quiz.demo.data.model.User;
import quiz.demo.exceptions.ModelVerificationException;
import quiz.demo.exceptions.UserAlreadyExistsException;
import quiz.demo.service.service.RegistrationService;
import quiz.demo.service.service.UserService;
import quiz.demo.web.utils.RestVerifier;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserRegistrationController {

	private final RegistrationService registrationService;

	private final UserService userService;

	private final MessageSource messageSource;


@Autowired
	public UserRegistrationController(RegistrationService registrationService, UserService userService, MessageSource messageSource) {
		this.registrationService = registrationService;
		this.userService = userService;
		this.messageSource = messageSource;
	}


	@GetMapping(value = "/registration")
	@PreAuthorize("permitAll")
	public String showRegistrationForm(@ModelAttribute User user) {
		return "registration";
	}

	@PostMapping(value = "/registration")
	@PreAuthorize("permitAll")
	public ModelAndView signUp(@ModelAttribute @Valid User user, BindingResult result) {
		User newUser;
		ModelAndView mav = new ModelAndView();

		try {
			RestVerifier.verifyModelResult(result);
			newUser = registrationService.startRegistration(user);
		} catch (ModelVerificationException e) {
			mav.setViewName("registration");
			return mav;
		} catch (UserAlreadyExistsException e) {
			result.rejectValue("email", "label.user.emailInUse");
			mav.setViewName("registration");
			return mav;
		}

		return registrationStepView(newUser, mav);
	}

	@GetMapping(value = "/{user_id}/continueRegistration")
	@PreAuthorize("permitAll")
	public ModelAndView nextRegistrationStep(@PathVariable Long user_id, String token) {
		User user = userService.find(user_id);
		registrationService.continueRegistration(user, token);

		ModelAndView mav = new ModelAndView();
		return registrationStepView(user, mav);
	}

	private ModelAndView registrationStepView(User user, ModelAndView mav) {

		if (!registrationService.isRegistrationCompleted(user)) {
			mav.addObject("header", messageSource.getMessage("label.registration.step1.header", null, null));
			mav.addObject("subheader", messageSource.getMessage("label.registration.step1.subheader", null, null));
			mav.setViewName("simplemessage");
		} else {
			mav.addObject("header", messageSource.getMessage("label.registration.step2.header", null, null));
			mav.addObject("subheader", messageSource.getMessage("label.registration.step2.subheader", null, null));
			mav.setViewName("simplemessage");
		}

		return mav;
	}
}
