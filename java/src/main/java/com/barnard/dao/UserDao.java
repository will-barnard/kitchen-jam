package com.barnard.dao;

import com.barnard.model.LoginDto;
import com.barnard.model.RegisterUserDto;
import com.barnard.model.User;
import com.barnard.model.UserAttributes;

import java.util.List;

public interface UserDao {

    List<User> getUsers();
    User getUserById(int id);
    User getUserByUsername(String username);
    UserAttributes getAttributesByUser(int userId);
    String getUsernameByEmail(LoginDto loginDto);
    User createUser(RegisterUserDto user);
    String createResetPasswordLink(int userId);
    void clearPasswordResetToken(int userId);
    void updatePassword(int userId, String password);
    String getUserEmail(int userId);
    int getUserIdByEmail(String email);
    int getUserIdByUuid(String uuid);


}
