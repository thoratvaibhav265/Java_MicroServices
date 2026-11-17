package com.vaibhav.userapp.services;

import java.util.List;

import com.vaibhav.userapp.entities.User;

public interface UserService {
  User saveUser(User user);

  List<User> getAllUser();

  User getUser(Long userId);
}
