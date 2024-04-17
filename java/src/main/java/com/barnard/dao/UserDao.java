package com.barnard.dao;

import com.barnard.model.RegisterUserDto;
import com.barnard.model.User;
import com.barnard.model.UserEmail;

import java.util.List;

public interface UserDao {

    List<User> getUsers();
    User getUserById(int id);
    User getUserByUsername(String username);
    UserEmail getEmailByUser(int userId);
    User createUser(RegisterUserDto user);

}
