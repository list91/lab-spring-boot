package com.example.cardsshop.service;

import com.example.cardsshop.exception.ResourceNotFoundException;
import com.example.cardsshop.exception.UserAlreadyExistsException;
import com.example.cardsshop.model.User;
import com.example.cardsshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        logger.info("Получение списка всех пользователей");
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        logger.info("Поиск пользователя по ID: {}", id);
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        logger.info("Получение пользователя по ID: {}", id);
        return userRepository.findById(id);
    }

    @Transactional
    public User createUser(User user) {
        logger.info("Создание нового пользователя: {}", user.getUsername());

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("Пользователь с email {} уже существует", user.getEmail());
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.error("Пользователь с username {} уже существует", user.getUsername());
            throw new UserAlreadyExistsException("Пользователь с таким именем пользователя уже существует");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        logger.info("Пользователь {} успешно создан", savedUser.getUsername());
        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        logger.info("Обновление пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Пользователь с ID {} не найден", id);
                    return new ResourceNotFoundException("Пользователь не найден");
                });

        // Проверка уникальности email
        userRepository.findByEmail(userDetails.getEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        logger.error("Email {} уже используется другим пользователем", userDetails.getEmail());
                        throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
                    }
                });

        // Проверка уникальности username
        userRepository.findByUsername(userDetails.getUsername())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        logger.error("Username {} уже используется другим пользователем", userDetails.getUsername());
                        throw new UserAlreadyExistsException("Пользователь с таким именем пользователя уже существует");
                    }
                });

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setBirthDate(userDetails.getBirthDate());
        user.setRole(userDetails.getRole());

        User updatedUser = userRepository.save(user);
        logger.info("Пользователь {} успешно обновлен", updatedUser.getUsername());
        return updatedUser;
    }

    @Transactional
    public void deleteUser(Long id) {
        logger.info("Удаление пользователя с ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Пользователь с ID {} не найден", id);
                    return new ResourceNotFoundException("Пользователь не найден");
                });

        userRepository.delete(user);
        logger.info("Пользователь с ID {} успешно удален", id);
    }
}
