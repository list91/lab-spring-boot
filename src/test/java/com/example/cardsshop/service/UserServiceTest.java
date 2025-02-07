package com.example.cardsshop.service;

import com.example.cardsshop.model.User;
import com.example.cardsshop.model.enums.Role;
import com.example.cardsshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
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
        testUser.setRole(Role.USER);
        testUser.setEnabled(true);
    }

    @Test
    void testCreateUser() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User createdUser = userService.createUser(testUser);

        // Assert
        assertNotNull(createdUser);
        verify(passwordEncoder, times(1)).encode(testUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(testUser.getUsername(), createdUser.getUsername());
    }

    @Test
    void testCreateUser_DuplicateEmail() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(testUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateUsername() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(testUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindUserById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> foundUser = userService.findUserById(1L);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getId(), foundUser.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserById_NotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = userService.findUserById(999L);

        // Assert
        assertTrue(foundUser.isEmpty());
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void testFindUserByUsername() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> foundUser = userService.findUserByUsername("testuser");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getUsername(), foundUser.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindUserByEmail() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> foundUser = userService.findUserByEmail("test@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testUpdateUser() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("olduser");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldPassword");

        User updatedUserDetails = new User();
        updatedUserDetails.setUsername("newuser");
        updatedUserDetails.setEmail("new@example.com");
        updatedUserDetails.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        User updatedUser = userService.updateUser(1L, updatedUserDetails);

        // Assert
        verify(userRepository, times(1)).save(existingUser);
        assertEquals("newuser", updatedUser.getUsername());
        assertEquals("new@example.com", updatedUser.getEmail());
        verify(passwordEncoder, times(1)).encode("newPassword");
    }

    @Test
    void testUpdateUser_DuplicateEmail() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("olduser");
        existingUser.setEmail("old@example.com");

        User updatedUserDetails = new User();
        updatedUserDetails.setUsername("newuser");
        updatedUserDetails.setEmail("existing@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, updatedUserDetails));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(999L));
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(testUser, new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> allUsers = userService.getAllUsers();

        // Assert
        assertEquals(2, allUsers.size());
        verify(userRepository, times(1)).findAll();
    }
}
