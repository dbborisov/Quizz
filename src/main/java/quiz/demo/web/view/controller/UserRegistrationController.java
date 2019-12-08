package quiz.demo.web.view.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import quiz.demo.exceptions.ModelVerificationException;
import quiz.demo.exceptions.UserAlreadyExistsException;
import quiz.demo.service.service.RegistrationService;
import quiz.demo.service.service.UserService;
import quiz.demo.web.utils.RestVerifier;
import quiz.demo.service.model.UserServiceModel;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserRegistrationController {

    private final RegistrationService registrationService;

    private final UserService userService;

    private final MessageSource messageSource;
    private final ModelMapper modelMapper;




    @Autowired
    public UserRegistrationController(RegistrationService registrationService, UserService userService, MessageSource messageSource, ModelMapper modelMapper) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.messageSource = messageSource;

        this.modelMapper = modelMapper;
    }


    @GetMapping(value = "/registration")
    @PreAuthorize("permitAll")
    public String showRegistrationForm( Model model) {
        ModelAndView modelAndView = new ModelAndView();
        model.addAttribute("user", new UserServiceModel());
        return "registration";
    }

    @PostMapping(value = "/registration")
    @PreAuthorize("permitAll")
    public ModelAndView signUp(@ModelAttribute @Valid UserServiceModel user, BindingResult result) {
        UserServiceModel newUser;
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
        UserServiceModel user = userService.find(user_id);
        registrationService.continueRegistration(user, token);

        ModelAndView mav = new ModelAndView();
        return registrationStepView(user, mav);
    }

    private ModelAndView registrationStepView(UserServiceModel user, ModelAndView mav) {

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
