package quiz.demo.service.service.impl;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quiz.demo.data.model.Role;
import quiz.demo.data.model.User;
import quiz.demo.data.repository.UserRepository;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.UserManagementService;
import quiz.demo.service.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	private final UserService userService;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
//	private TokenServiceForgotPassword forgotPasswordService;
//	private TokenDeliverySystem tokenDeliveryService;

	@Autowired
	public UserManagementServiceImpl(UserService userService, UserRepository userRepository, ModelMapper modelMapper) {
//			this.forgotPasswordService = forgotPasswordService;
//			this.tokenDeliveryService = tokenDeliveryService;
		this.userService = userService;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public void resendPassword(UserServiceModel user) {
//		ForgotPasswordToken token = forgotPasswordService.generateTokenForUser(user);
//		tokenDeliveryService.sendTokenToUser(token, user, TokenType.FORGOT_PASSWORD);
	}

	@Override
	public void verifyResetPasswordToken(UserServiceModel user, String token) {
//		forgotPasswordService.validateTokenForUser(user, token);
	}

	@Override
	public void updatePassword(UserServiceModel user, String password) {
		User u = this.userRepository.findByUsername(user.getUsername());
		userService.updatePassword(u, password);

	}

	@Override
	public void updatePasswordAndRole(UserServiceModel user, String password, String role) {
		User u = this.userRepository.findByUsername(user.getUsername());
		u.setRole(Role.valueOf(role));
		userService.updatePassword(u, password);
	}

	@Override
	public List<UserServiceModel> findAll() {
		return this.userRepository.findAll().stream().
				map(e->modelMapper.map(e,UserServiceModel.class)).collect(Collectors.toList());
	}


}
