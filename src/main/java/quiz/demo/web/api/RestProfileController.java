package quiz.demo.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(RestProfileController.ROOT_MAPPING)
public class RestProfileController {
    public static final String ROOT_MAPPING = "/api/profile/user";
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(RestUserController.class);
    public RestProfileController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "all",produces = "application/json")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public List<UserServiceModel> getUsers(){
        List<UserServiceModel> allWithoutROOT = this.userService.findAllWithoutROOT();
//        new ResponseStatus(HttpStatus.OK);
        return this.userService.findAllWithoutROOT();

    }

    @GetMapping(value = "all/{id}") // todo routing is not correct
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public UserServiceModel getUser(@PathVariable Long id){
        UserServiceModel user = this.userService.findById(id);
        return user; //todo
    }
}
