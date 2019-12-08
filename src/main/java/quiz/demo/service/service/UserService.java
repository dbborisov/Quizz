package quiz.demo.service.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import quiz.demo.data.model.User;
import quiz.demo.exceptions.ResourceUnavailableException;
import quiz.demo.exceptions.UnauthorizedActionException;
import quiz.demo.exceptions.UserAlreadyExistsException;
import quiz.demo.service.model.UserServiceModel;

public interface UserService extends UserDetailsService {
    UserServiceModel saveUser(User user) throws UserAlreadyExistsException;

    UserServiceModel find(Long id) throws ResourceUnavailableException;

    UserServiceModel findByEmail(String email) throws ResourceUnavailableException;

    UserServiceModel findByUsername(String username) throws ResourceUnavailableException;

    UserServiceModel updatePassword(UserServiceModel user, String password) throws ResourceUnavailableException;

    void delete(Long user_id) throws UnauthorizedActionException, ResourceUnavailableException;

    UserServiceModel setRegistrationCompleted(UserServiceModel user) throws ResourceUnavailableException;

    boolean isRegistrationCompleted(UserServiceModel user) throws ResourceUnavailableException;

}