package quiz.demo.service.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quiz.demo.data.model.User;
import quiz.demo.service.service.UserManagementService;
import quiz.demo.service.service.UserService;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	private UserService userService;
//	private TokenServiceForgotPassword forgotPasswordService;
//	private TokenDeliverySystem tokenDeliveryService;

	@Autowired
	public UserManagementServiceImpl(UserService userService) {
//			this.forgotPasswordService = forgotPasswordService;
//			this.tokenDeliveryService = tokenDeliveryService;
		this.userService = userService;
	}

	@Override
	public void resendPassword(User user) {
//		ForgotPasswordToken token = forgotPasswordService.generateTokenForUser(user);
//		tokenDeliveryService.sendTokenToUser(token, user, TokenType.FORGOT_PASSWORD);
	}

	@Override
	public void verifyResetPasswordToken(User user, String token) {
//		forgotPasswordService.validateTokenForUser(user, token);
	}

	@Override
	public void updatePassword(User user, String password) {
		userService.updatePassword(user, password);
	}

}
