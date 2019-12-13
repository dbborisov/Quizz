package quiz.demo.service.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import quiz.demo.data.model.User;
import quiz.demo.exceptions.ResourceUnavailableException;
import quiz.demo.exceptions.UnauthorizedActionException;
import quiz.demo.exceptions.UserAlreadyExistsException;
import quiz.demo.service.model.UserServiceModel;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserServiceModel saveUser(User user) throws UserAlreadyExistsException;

    UserServiceModel findById(Long id) throws ResourceUnavailableException;

    UserServiceModel findByEmail(String email) throws ResourceUnavailableException;

    UserServiceModel findByUsername(String username) throws ResourceUnavailableException;

    UserServiceModel updatePassword(User user, String password) throws ResourceUnavailableException;

    boolean delete(Long user_id) throws UnauthorizedActionException, ResourceUnavailableException;

    UserServiceModel setRegistrationCompleted(UserServiceModel user) throws ResourceUnavailableException;

    boolean isRegistrationCompleted(UserServiceModel user) throws ResourceUnavailableException;

    List<UserServiceModel> findAll();

}