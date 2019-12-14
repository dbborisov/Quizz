package quiz.demo.web.view.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import quiz.demo.exceptions.ModelVerificationException;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.UserManagementService;
import quiz.demo.service.service.UserService;
import quiz.demo.web.utils.RestVerifier;

import javax.validation.Valid;
import java.security.Principal;
@Controller
@RequestMapping(UserProfileController.ROOT_MAPPING)
public class UserProfileController extends BaseController {

    public static final String ROOT_MAPPING = "/user/profile";
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserManagementService userManagementService;

    @Autowired
    public UserProfileController(UserService userService, ModelMapper modelMapper, UserManagementService userManagementService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userManagementService = userManagementService;
    }


    @GetMapping(value = "")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getProfile(Principal principal, ModelAndView modelAndView){
        modelAndView
                .addObject("model", this.userService.findByUsername(principal.getName()));
        return super.view("user/userProfile",modelAndView);
    }

    @GetMapping(value = "/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView){
        modelAndView
                .addObject("model", this.userService.findByUsername(principal.getName()));
        return super.view("user/editUserProfile",modelAndView);
    }

    @PostMapping(value = "/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView signUp(@ModelAttribute @Valid UserServiceModel user, BindingResult result) {

        ModelAndView mav = new ModelAndView();
UserServiceModel userNew = this.userService.findByUsername(user.getUsername());
        try {
            RestVerifier.verifyModelResult(result);
           userManagementService.updatePassword(userNew,user.getPassword());
        } catch (ModelVerificationException e) {
            mav.setViewName("user/registration");
            return mav;
        }

        return redirect("/user/profile");
    }
}
