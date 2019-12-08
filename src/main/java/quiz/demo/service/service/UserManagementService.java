package quiz.demo.service.service;


import quiz.demo.data.model.User;
import quiz.demo.service.model.UserServiceModel;

public interface UserManagementService {

	void resendPassword(UserServiceModel user);

	void verifyResetPasswordToken(UserServiceModel user, String token);

	void updatePassword(UserServiceModel user, String password);

}
