package quiz.demo;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import quiz.demo.data.model.User;
import quiz.demo.data.repository.UserRepository;
import quiz.demo.exceptions.QuizZzException;
import quiz.demo.exceptions.ResourceUnavailableException;
import quiz.demo.exceptions.UnauthorizedActionException;
import quiz.demo.exceptions.UserAlreadyExistsException;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.LogService;
import quiz.demo.service.service.UserService;
import quiz.demo.service.service.impl.UserServiceImpl;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

	UserService service;

	// Mocks
	UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	LogService logService;
	ModelMapper modelMapper;

	User user = new User();

	@BeforeEach
	public void before() {
		userRepository = mock(UserRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);
		modelMapper =mock(ModelMapper.class);
		logService = mock(LogService.class);
		service = new UserServiceImpl(userRepository, passwordEncoder,modelMapper,logService);

		user.setEmail("a@a.com");
		user.setPassword("Password");
	}

//	@Test
//	public void saveNewUserShouldSucceed() throws UserAlreadyExistsException {
//		when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
//		when(userRepository.save(user)).thenReturn(user);
//
//		UserServiceModel returned = service.saveUser(user);
//
//		verify(userRepository, times(1)).save(user);
//		assertTrue((modelMapper.map(user,User.class).getUsername()).equals(returned.getUsername()));
//	}

	@Test()
	public void saveNewUserMailExistsShouldFail() throws UserAlreadyExistsException {
		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);

		Assertions.assertThrows(UserAlreadyExistsException.class,() -> {
			service.saveUser(user);
		});
	}

	@Test
	public void deleteUser() throws UnauthorizedActionException, ResourceUnavailableException {
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		assertTrue(!service.delete(user.getId()));
//		verify(userRepository, times(1)).delete(user);
	}

	@Test
	public void testDeleteUnexistentUser() throws QuizZzException {

		when(userRepository.findById(3L)).thenReturn(null);
		Assertions.assertThrows( ResourceUnavailableException.class,() -> {
			service.delete(user.getId());
		});
//		assertTrue(!(service.delete(user.getId())));
	}

	@Test
	public void testDeleteFromWrongUser() throws QuizZzException {
		User returnAs = new User();
		returnAs.setEnabled(true);
		returnAs.setUsername("Pesho");
		returnAs.setEmail("Ema@pe");
		returnAs.setId(5L);
		when(userRepository.findById(user.getId())).thenReturn(null);
		doThrow(new UnauthorizedActionException()).when(userRepository).delete(user);


		Assertions.assertThrows(ResourceUnavailableException.class,() -> {
			service.delete(5L);
	});
//		assertFalse(service.delete(5L));


	}

	@Test
	public void findUserByUsername_shouldntFind() {
		when(userRepository.findByEmail(user.getEmail())).thenThrow(new UsernameNotFoundException("test"));


		Assertions.assertThrows(UsernameNotFoundException.class,() -> {
			service.loadUserByUsername("test");
	});

	}

	@Test
	public void findUserByUsername_shouldFind() {
		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

		UserDetails localUser = service.loadUserByUsername(user.getEmail());

		verify(userRepository, times(1)).findByEmail(user.getEmail());
		assertNotNull(localUser);
	}

//	@Test
//	public void updatePasswordShouldEncrypt() {
//		final String clearPass = "clearPassword";
//		final String encodedPass = "encodedPassword";
//		when(passwordEncoder.encode(clearPass)).thenReturn(encodedPass);
//		when(userRepository.save(user)).thenReturn(user);
//
//		UserServiceModel newUser = service.updatePassword(user, clearPass);
//
//		verify(passwordEncoder, times(1)).encode(clearPass);
//		verify(userRepository, times(1)).save(user);
//		assertEquals(encodedPass, newUser.getPassword());
//	}
}
