package com.barnard.dao;

import com.barnard.model.RegisterUserDto;
import com.barnard.model.User;

import java.util.List;

public interface UserDao {

    List<User> getUsers();

    User getUserById(int id);

    User getUserByUsername(String username);

    User createUser(RegisterUserDto user);
}
