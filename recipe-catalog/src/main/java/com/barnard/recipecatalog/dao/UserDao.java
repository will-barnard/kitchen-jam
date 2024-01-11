package com.barnard.recipecatalog.dao;

import com.barnard.recipecatalog.model.User;

import java.util.List;

public interface UserDao {

    String findUsernameById(int id);
    List<User> findAll();
    List<String> findAllUsernames();
    User findByUsername(String username);
    int findIdByUsername(String username);
    int create(String username, String password);

}
