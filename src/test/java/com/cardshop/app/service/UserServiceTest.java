package com.cardshop.app.service;

import com.cardshop.app.model.User;
import com.cardshop.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("rawPassword");
    }

    @Test
    void createUser_NewUser_ShouldSaveEncodedPassword() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(testUser)).thenReturn(testUser);

        User createdUser = userService.createUser(testUser);

        verify(passwordEncoder).encode(testUser.getPassword());
        verify(userRepository).save(testUser);
        assertEquals("encodedPassword", testUser.getPassword());
    }

    @Test
    void createUser_ExistingUsername_ShouldThrowException() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        assertThrows(RuntimeException.class, () -> userService.createUser(testUser));
    }

    @Test
    void updateUser_ShouldUpdateUserDetails() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("olduser");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldPassword");

        User updateDetails = new User();
        updateDetails.setUsername("newuser");
        updateDetails.setEmail("new@example.com");
        updateDetails.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updateDetails.getPassword())).thenReturn("encodedNewPassword");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.updateUser(1L, updateDetails);

        assertEquals("newuser", updatedUser.getUsername());
        assertEquals("new@example.com", updatedUser.getEmail());
        verify(passwordEncoder).encode(updateDetails.getPassword());
        verify(userRepository).save(existingUser);
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        userService.deleteUser(1L);

        verify(userRepository).delete(testUser);
    }
}
