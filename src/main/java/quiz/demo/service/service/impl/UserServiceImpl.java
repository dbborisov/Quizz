package quiz.demo.service.service.impl;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quiz.demo.data.model.AuthenticatedUser;
import quiz.demo.data.model.User;
import quiz.demo.data.repository.UserRepository;
import quiz.demo.exceptions.ResourceUnavailableException;
import quiz.demo.exceptions.UnauthorizedActionException;
import quiz.demo.exceptions.UserAlreadyExistsException;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserServiceModel saveUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            logger.error("The mail " + user.getEmail() + " is already in use");
            throw new UserAlreadyExistsException("The mail " + user.getEmail() + " is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);

        return modelMapper.map(userRepository.save(user),UserServiceModel.class);
    }

    @Override
    /*
     * Look up by both Email and Username. Throw exception if it wasn't in
     * either. TODO: Join Username and Email into one JPQL
     */
    public AuthenticatedUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserServiceModel user;

        try {
            user = findByUsername(username);
        } catch (ResourceUnavailableException e) {
            try {
                user = findByEmail(username);
            } catch (ResourceUnavailableException e2) {
                throw new UsernameNotFoundException(username + " couldn't be resolved to any user");
            }
        }

        return new AuthenticatedUser(modelMapper.map(user,User.class));
    }

    @Override
    public UserServiceModel findByUsername(String username) throws ResourceUnavailableException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            logger.error("The user " + username + " doesn't exist");
            throw new ResourceUnavailableException("The user " + username + " doesn't exist");
        }

        return  modelMapper.map(user,UserServiceModel.class);
    }

    @Override
    public UserServiceModel find(Long id) throws ResourceUnavailableException {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            logger.error("The user " + id + " can't be found");
            throw new ResourceUnavailableException("User " + id + " not found.");
        }

        return modelMapper.map(user,UserServiceModel.class);
    }

    @Override
    public void delete(Long user_id) throws UnauthorizedActionException, ResourceUnavailableException {
        UserServiceModel userToDelete = find(user_id);

        userRepository.delete(modelMapper.map(userToDelete,User.class));
    }

    @Override
    public UserServiceModel setRegistrationCompleted(UserServiceModel user) {
        user.setEnabled(true);
        return modelMapper.map(userRepository.save(modelMapper.map(user,User.class)),UserServiceModel.class);
    }

    @Override
    public boolean isRegistrationCompleted(UserServiceModel user) {
        return modelMapper.map(user,User.class).getEnabled();
    }

    @Override
    public UserServiceModel findByEmail(String email) throws ResourceUnavailableException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            logger.error("The mail " + email + " can't be found");
            throw new ResourceUnavailableException("The mail " + email + " can't be found");
        }

        return modelMapper.map(user,UserServiceModel.class);
    }

    @Override
    public UserServiceModel updatePassword(UserServiceModel user, String password) throws ResourceUnavailableException {
        user.setPassword(passwordEncoder.encode(password));
        return modelMapper.map(userRepository.save(modelMapper.map(user,User.class)),UserServiceModel.class);
    }
}
