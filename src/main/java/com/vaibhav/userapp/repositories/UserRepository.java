package com.vaibhav.userapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaibhav.userapp.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
