package com.barnard.recipecatalog.dao;

import com.barnard.recipecatalog.model.User;

import java.util.List;

public class JdbcUserDao implements UserDao {
    @Override
    public String findUsernameById(int id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<String> findAllUsernames() {
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public int findIdByUsername(String username) {
        return 0;
    }

    @Override
    public int create(String username, String password) {
        return 0;
    }
}
