package quiz.demo.service.service.impl;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import quiz.demo.base.TestBase;

import quiz.demo.data.model.AuthenticatedUser;
import quiz.demo.data.model.Role;
import quiz.demo.data.model.User;
import quiz.demo.data.repository.UserRepository;
import quiz.demo.service.model.UserServiceModel;
import quiz.demo.service.service.UserService;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

//@RunWith()
class UserServiceImplTest extends TestBase {
    @MockBean
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @MockBean
    ModelMapper modelMapper;


    User created;
//
//    @BeforeEach
//    public void init() {
//        User   user = new User();
//        user.setUsername("TestUser");
//        user.setRole(Role.Admin);
//        user.setEnabled(true);
//        user.setEmail("test@test");
//        user.setPassword(passwordEncoder.encode("123456"));
//
//
//
////        when(this.userRepository.save(Mockito.any(User.class))).thenAnswer(u -> u.getArguments()[0]);
//          created = this.modelMapper.map(this.userService.saveUser(user),User.class);
////        when(this.userRepository.findByUsername(Mockito.anyString())).thenReturn(created);
//
//    }

    @Test
    void saveUser() {
        User user = testUser();

        Mockito.when(this.userRepository.save(user)).thenReturn(user);
        created = this.userRepository.save(user);
        assertEquals("TestUser", created.getUsername());
        assertEquals(Role.Admin, created.getRole());
        assertEquals(user.getPassword(), created.getPassword());
        assertEquals(user.getEmail(), created.getEmail());
        assertTrue(created.getEnabled());

    }

    @Test
    void findByUsername() {

        Mockito.when(this.userRepository.findByUsername("TestUser")).thenReturn(testUser());

        created = this.userRepository.findByUsername("TestUser");
        assertEquals(created,testUser());
    }

    @Test
    void findById_test() {

//        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser()));
       when(this.userRepository.findById(1L)).thenReturn(Optional.of(testUser()));
        UserServiceModel byId = this.userService.findById(1L); // todo stop working

        assertEquals(byId.getUsername(),testUser().getUsername());
    }

    @Test
    void delete() {
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(testUser()));
            assertTrue(!this.userService.delete(1L));

    }

    @Test
    void setRegistrationCompleted() {
        created = testUser();

        Mockito.when(this.userRepository.save(new User())).thenReturn(created);
        UserServiceModel userServiceModel = this.userService.setRegistrationCompleted(this.modelMapper.map(created, UserServiceModel.class));
        assertEquals(userServiceModel,this.modelMapper.map(testUser(), UserServiceModel.class));

    }

    @Test
    void isRegistrationCompleted() {


    }

    @Test
    void findAll() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void updatePassword() {
    }

    private User testUser() {
        User   user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        user.setRole(Role.Admin);
        user.setEnabled(true);
        user.setEmail("test@test");
        user.setPassword(passwordEncoder.encode("123456"));
        return user;
    }
}