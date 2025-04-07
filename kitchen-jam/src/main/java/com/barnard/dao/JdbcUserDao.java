package com.barnard.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
        String sql2 = "INSERT INTO user_attributes (user_id, email, display_name, nurture_state, is_public, default_public) VALUES (?, ?, ?, ?, true, true)";

        int newUserNurtureState = 0;

        try {
            int newUserId = jdbcTemplate.queryForObject(insertUserSql, int.class, user.getUsername().toLowerCase(), password_hash, ssRole);
            newUser = getUserById(newUserId);
            jdbcTemplate.update(sql2, newUserId, user.getEmail(), user.getUsername(), newUserNurtureState);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }


        return newUser;
    }

    @Override
    public String createResetPasswordLink(int userId) {
        String uuid = UUID.randomUUID().toString();
        String sql = "INSERT INTO password_reset (user_id, uuid_value, time_generated) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, userId, uuid, LocalDateTime.now());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return uuid;
    }

    @Override
    public void clearPasswordResetToken(int userId) {
        String sql = "DELETE FROM password_reset WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void updatePassword(int userId, String password) {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        try {
            jdbcTemplate.update(sql, password_hash, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public String getUserEmail(int userId) {
        String email = null;
        String sql = "SELECT email FROM user_attributes WHERE user_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            if (rowSet.next()) {
                email = rowSet.getString("email");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return email;
    }

    @Override
    public int getUserIdByEmail(String email) {
        int userId = 0;
        String sql = "SELECT user_id FROM user_attributes WHERE email = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, email);
            if (rowSet.next()) {
                userId = rowSet.getInt("user_id");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return userId;
    }

    @Override
    public int getUserIdByUuid(String uuid) {
        int userId = 0;
        String sql = "SELECT user_id FROM password_reset WHERE uuid_value = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, uuid);
            if (rowSet.next()) {
                userId = rowSet.getInt("user_id");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return userId;
    }

    @Override
    public boolean checkUuid(String uuid) {
        boolean exists = false;
        String sql = "SELECT EXISTS(SELECT 1 FROM password_reset WHERE uuid_value = ?)";
        try {
            exists = jdbcTemplate.queryForObject(sql, boolean.class, uuid);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return exists;
    }

    @Override
    public void deleteOldPasswordResets() {
        String sql = "DELETE FROM password_reset WHERE time_generated < CURRENT_TIMESTAMP - INTERVAL '1 DAY'";
        try {
            jdbcTemplate.update(sql);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
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
        userAttributes.setDisplayName(rs.getString("display_name"));
        userAttributes.setNurtureState(rs.getInt("nurture_state"));

        return userAttributes;
    }
}
