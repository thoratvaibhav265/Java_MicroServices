package com.vaibhav.userapp.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vaibhav.userapp.entities.User;
import com.vaibhav.userapp.exception.NoSuchElementException;
import com.vaibhav.userapp.repositories.UserRepository;
import com.vaibhav.userapp.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not available with ID:" + userId));
    }
}
