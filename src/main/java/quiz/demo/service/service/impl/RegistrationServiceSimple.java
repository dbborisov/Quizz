package quiz.demo.service.service.impl;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quiz.demo.data.model.User;
import quiz.demo.service.service.RegistrationService;
import quiz.demo.service.service.UserService;
import quiz.demo.service.model.UserServiceModel;

@Service
public class RegistrationServiceSimple implements RegistrationService {


	private final UserService userService;
	private final ModelMapper modelMapper;

	@Autowired
	public RegistrationServiceSimple(UserService userService, ModelMapper modelMapper) {
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserServiceModel startRegistration(UserServiceModel user) {
		UserServiceModel newUser = userService.saveUser(modelMapper.map(user,User.class));
		userService.setRegistrationCompleted(newUser);

		return newUser;
	}

	@Override
	public UserServiceModel continueRegistration(UserServiceModel user, String token) {
		return null;
	}

	@Override
	public boolean isRegistrationCompleted(UserServiceModel user) {
		return userService.isRegistrationCompleted(user);
	}

}
