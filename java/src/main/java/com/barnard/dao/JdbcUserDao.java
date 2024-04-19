package com.barnard.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.barnard.exception.DaoException;
import com.barnard.model.LoginDto;
import com.barnard.model.RegisterUserDto;
import com.barnard.model.UserAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.barnard.model.User;

@Component
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(int userId) {

        User user = null;
        String sql = "SELECT user_id, username, password_hash, role FROM users WHERE user_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                user = mapRowToUser(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }


        return user;
    }

    @Override
    public List<User> getUsers() {

        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash, role FROM users";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                User user = mapRowToUser(results);
                users.add(user);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);

        }
        return users;
    }

    @Override
    public UserAttributes getAttributesByUser(int userId) {

        UserAttributes userAttributes = new UserAttributes();
        String sql = "SELECT * FROM user_attributes WHERE user_id = ?;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            if (rowSet.next()) {
                userAttributes = mapRowToUserAttributes(rowSet);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return userAttributes;

    }

    @Override
    public String getUsernameByEmail(LoginDto loginDto) {
        String username = null;
        User user = null;
        String sql = "SELECT * FROM users " +
                "JOIN user_attributes ON users.user_id = user_attributes.user_id " +
                "WHERE user_attributes.email = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, loginDto.getEmail());
            if (rowSet.next()) {
                username = mapRowToUser(rowSet).getUsername();
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return username;
    }

    @Override
    public User getUserByUsername(String username) {

        if (username == null) throw new IllegalArgumentException("Username cannot be null");
        User user = null;
        String sql = "SELECT user_id, username, password_hash, role FROM users WHERE username = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
            if (rowSet.next()) {
                user = mapRowToUser(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return user;
    }

    @Override
    public User createUser(RegisterUserDto user) {

        User newUser = null;
        String insertUserSql = "INSERT INTO users (username, password_hash, role) values (?, ?, ?) RETURNING user_id";
        String password_hash = new BCryptPasswordEncoder().encode(user.getPassword());
        String ssRole = user.getRole().toUpperCase().startsWith("ROLE_") ? user.getRole().toUpperCase() : "ROLE_" + user.getRole().toUpperCase();
        String sql2 = "INSERT INTO user_attributes (user_id, email, nurture_state) VALUES (?, ?, ?)";

        int newUserNurtureState = 0;

        try {
            int newUserId = jdbcTemplate.queryForObject(insertUserSql, int.class, user.getUsername(), password_hash, ssRole);
            newUser = getUserById(newUserId);
            jdbcTemplate.update(sql2, newUserId, user.getEmail(), newUserNurtureState);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }


        return newUser;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();

        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setAuthorities(Objects.requireNonNull(rs.getString("role")));
        user.setActivated(true);

        return user;
    }

    private UserAttributes mapRowToUserAttributes(SqlRowSet rs) {
        UserAttributes userAttributes = new UserAttributes();

        userAttributes.setUserId(rs.getInt("user_id"));
        userAttributes.setEmail(rs.getString("email"));
        userAttributes.setNurtureState(rs.getInt("nurture_state"));

        return userAttributes;
    }
}
