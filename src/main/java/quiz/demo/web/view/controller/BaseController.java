package quiz.demo.web.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import quiz.demo.service.model.LogServiceModel;
import quiz.demo.service.service.LogService;

import java.security.Principal;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class BaseController {
    @Autowired
    private  LogService log;



    public BaseController(){

    }

    public ModelAndView view(String viewName, ModelAndView modelAndView) {
        modelAndView.setViewName(viewName);

        return modelAndView;
    }

    public ModelAndView view(String viewName) {
        return this.view(viewName, new ModelAndView());
    }

    public ModelAndView redirect(String url) {
        return this.view("redirect:" + url);
    }

    public void logPrincipal(Principal principal, String... data) {
        if (principal != null) {

            log.seedLogInDB(new LogServiceModel(principal.getName(), " played quiz with id = " + Arrays.stream(data).collect(Collectors.joining(" "))));
        } else {
            log.seedLogInDB(new LogServiceModel("anonymous", " played quiz with id = " + Arrays.stream(data).collect(Collectors.joining(" "))));
        }
    }
}
