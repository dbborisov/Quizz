package quiz.demo.service.service;


import quiz.demo.data.model.User;
import quiz.demo.service.model.UserServiceModel;

public interface RegistrationService {
	UserServiceModel startRegistration(UserServiceModel user);

	UserServiceModel continueRegistration(UserServiceModel user, String token);

	boolean isRegistrationCompleted(UserServiceModel user);
}
