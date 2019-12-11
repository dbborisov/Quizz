package quiz.demo.web.view.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.UserService;

import java.security.Principal;
@Controller
@RequestMapping(UserProfileController.ROOT_MAPPING)
public class UserProfileController extends BaseController {

    public static final String ROOT_MAPPING = "/user/profile";
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserProfileController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @GetMapping(value = "")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getProfile(Principal principal, ModelAndView modelAndView){
        modelAndView
                .addObject("model", this.userService.findByUsername(principal.getName()));
        return super.view("userProfile",modelAndView);
    }
}
