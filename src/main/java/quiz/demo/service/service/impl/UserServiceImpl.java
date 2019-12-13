package quiz.demo.service.service.impl;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quiz.demo.data.model.AuthenticatedUser;
import quiz.demo.data.model.Role;
import quiz.demo.data.model.User;
import quiz.demo.data.repository.UserRepository;
import quiz.demo.exceptions.ResourceUnavailableException;
import quiz.demo.exceptions.UnauthorizedActionException;
import quiz.demo.exceptions.UserAlreadyExistsException;
import quiz.demo.service.model.LogServiceModel;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.LogService;
import quiz.demo.service.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private  final LogService log;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, ModelMapper modelMapper, LogService log) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.log = log;
    }

    @Override
    public UserServiceModel saveUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            logger.error("The mail " + user.getEmail() + " is already in use");
            log.seedLogInDB(new LogServiceModel("anonymous","The mail " + user.getEmail() + " is already in use"));
            throw new UserAlreadyExistsException("The mail  or email is already in use");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            logger.error("The username " + user.getUsername() + " is already in use");
            log.seedLogInDB(new LogServiceModel("anonymous","The Username " + user.getEmail() + " is already in use"));
            throw new UserAlreadyExistsException("The username or email  is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setEnabled(false);

        if(this.userRepository.findUserByRole(Role.valueOf("ROOT")) == null){ //todo must be optimize
            user.setRole(Role.ROOT);
            user.setEnabled(true);
            log.seedLogInDB(new LogServiceModel(user.getUsername(),"is Created with authority" + user.getRole().name()));
        }else if(this.userRepository.findUserByRole(Role.valueOf("Admin")) == null){
            user.setRole(Role.Admin);
            user.setEnabled(true);
            log.seedLogInDB(new LogServiceModel(user.getUsername(),"is Created with authority" + user.getRole().name()));
        }else {
            user.setEnabled(false);
            user.setRole(Role.Master_User);
        }
        return modelMapper.map(userRepository.save(user),UserServiceModel.class);
    }

    @Override
    /*
     * Look up by both Email and Username. Throw exception if it wasn't in
     * either. TODO: Join Username and Email into one JPQL
     */
    public AuthenticatedUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserServiceModel user;
                //todo may be bather to use findByEmailOrUsername
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
            log.seedLogInDB(new LogServiceModel("anonymous","The user " + username + " doesn't exist"));
            throw new ResourceUnavailableException("The user " + username + " doesn't exist");
        }

        return  modelMapper.map(user,UserServiceModel.class);
    }

    @Override
    public UserServiceModel findById(Long id) throws ResourceUnavailableException {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            logger.error("The user with id" + id + " can't be found");
            log.seedLogInDB(new LogServiceModel("anonymous","The user with id" + id + " can't be found"));
            throw new ResourceUnavailableException("User with id " + id + " not found.");
        }

        return modelMapper.map(user,UserServiceModel.class);
    }

    @Override
    public boolean delete(Long user_id) throws UnauthorizedActionException, ResourceUnavailableException {
        UserServiceModel userToDelete = findById(user_id);

        userRepository.delete(modelMapper.map(userToDelete,User.class));
        return true;
    }

    @Override
    public UserServiceModel setRegistrationCompleted(UserServiceModel user) {
        if(user==null){
            return null;
        }
        user.setEnabled(true);

        return modelMapper.map(userRepository.save(modelMapper.map(user,User.class)),UserServiceModel.class);
    }

    @Override
    public boolean isRegistrationCompleted(UserServiceModel user) {
        return modelMapper.map(user,User.class).getEnabled();
    }

    @Override
    public List<UserServiceModel> findAll() {
        return this.userRepository.findAll().stream().map(e->this.modelMapper.map(e,UserServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public UserServiceModel findByEmail(String email) throws ResourceUnavailableException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            logger.error("The mail " + email + " can't be found");
            log.seedLogInDB(new LogServiceModel("anonymous","The mail " + email + " can't be found"));
            throw new ResourceUnavailableException("The mail " + email + " can't be found");
        }

        return modelMapper.map(user,UserServiceModel.class);
    }

    @Override
    public UserServiceModel updatePassword(User user, String password) throws ResourceUnavailableException {
        user.setPassword(passwordEncoder.encode(password));
        return modelMapper.map(userRepository.save(modelMapper.map(user,User.class)),UserServiceModel.class);
    }
}
