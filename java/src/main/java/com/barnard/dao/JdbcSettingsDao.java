package com.barnard.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcSettingsDao implements SettingsDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean makeProfilePublic(int userId) {
        String sql = "UPDATE user_attributes SET is_public = true WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, userId);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean makeProfilePrivate(int userId) {
        String sql = "UPDATE user_attributes SET is_public = false WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, userId);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean makeDefaultPublic(int userId) {
        String sql = "UPDATE user_attributes SET default_public = true WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, userId);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean makeDefaultPrivate(int userId) {
        String sql = "UPDATE user_attributes SET default_public = false WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, userId);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
