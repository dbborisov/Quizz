package quiz.demo.web.view.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import quiz.demo.data.model.User;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.UserManagementService;
import quiz.demo.service.service.UserService;

@Controller
@RequestMapping("/user")
public class UserManagementController {


	private final UserManagementService userManagementService;
	private final MessageSource messageSource;
	private final UserService userService;
	private final ModelMapper mapper;

	@Autowired
	public UserManagementController(UserManagementService userManagementService, MessageSource messageSource, UserService userService, ModelMapper mapper) {
		this.userManagementService = userManagementService;
		this.messageSource = messageSource;
		this.userService = userService;
		this.mapper = mapper;
	}

	@GetMapping(value = "/login")
	@PreAuthorize("permitAll")
	public String login(@ModelAttribute User user) {
		return "login";
	}
	
	@GetMapping(value = "/login-error")
	@PreAuthorize("permitAll")
	public String loginError(@ModelAttribute User user, Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}

	@GetMapping(value = "/forgotPassword")
	@PreAuthorize("permitAll")
	public String forgotPassword() {
		return "forgotPassword";
	}

	@PostMapping(value = "/forgotPassword")
	@PreAuthorize("permitAll")
	public ModelAndView forgotPassword(String email) {
		UserServiceModel user = userService.findByEmail(email);
		userManagementService.resendPassword(user);

		ModelAndView mav = new ModelAndView();
		mav.addObject("header", messageSource.getMessage("label.forgotpassword.success.header", null, null));
		mav.addObject("subheader", messageSource.getMessage("label.forgotpassword.success.subheader", null, null));
		mav.setViewName("simplemessage");

		return mav;
	}

	@GetMapping(value = "/{user_id}/resetPassword")
	@PreAuthorize("permitAll")
	public ModelAndView resetPassword(@PathVariable Long user_id, String token) {
		UserServiceModel user = userService.find(user_id);
		userManagementService.verifyResetPasswordToken(user, token);

		ModelAndView mav = new ModelAndView();
		mav.addObject("user", user);
		mav.addObject("token", token);
		mav.setViewName("resetPassword");

		return mav;
	}

	@PostMapping(value = "/{user_id}/resetPassword")
	@PreAuthorize("permitAll")
	public String resetPassword(@PathVariable Long user_id, String token, String password) {
		UserServiceModel user = userService.find(user_id);
		userManagementService.verifyResetPasswordToken(user, token);

		userManagementService.updatePassword(user, password);

		return "login";
	}
}
