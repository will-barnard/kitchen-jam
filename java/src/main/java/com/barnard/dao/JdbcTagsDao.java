package com.barnard.dao;


import com.barnard.exception.DaoException;
import com.barnard.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTagsDao implements TagsDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Tag getTag(int tagId) {

        Tag tag = null;
        String sql = "SELECT * " +
                "FROM tags " +
                "WHERE tag_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, tagId);
            if (rowSet.next()) {
                tag = mapRowToTag(rowSet);
            } else {
                throw new DaoException();
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return tag;
    }

    @Override
    public List<Tag> getTagsByMealId(int mealId) {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM tags " +
                "JOIN tags_meal ON tags.tag_id = tags_meal.tag_id " +
                "WHERE meal_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, mealId);
            while (rowSet.next()) {
                tags.add(mapRowToTag(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return tags;
    }

    @Override
    public Tag createTag(Tag tag, int userId) {

        String sql = "INSERT INTO tags (tag_name, user_id) " +
                "VALUES (?, ?) " +
                "RETURNING tag_id;";

        try {
            int tagId = jdbcTemplate.queryForObject(sql, int.class, tag.getTagName(), userId);
            tag.setTagId(tagId);
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return tag;

    }

    @Override
    public Tag updateTag(Tag tag) {

        Tag updateTag = null;
        String sql = "UPDATE tags SET tag_name = ? WHERE tag_id = ?;";

        try {
            jdbcTemplate.update(sql, tag.getTagName(), tag.getTagId());
            updateTag = getTag(tag.getTagId());
            if (!tag.getTagName().equals(updateTag.getTagName())) {
                throw new DaoException();
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return updateTag;

    }

    @Override
    public List<Tag> addTagToMeal(int tagId, int mealId) {

        List<Tag> tags = new ArrayList<>();
        String sql = "INSERT INTO tags_meal (tag_id, meal_id) " +
                "VALUES (?, ?);";

        try {
            int rowsAffected = jdbcTemplate.update(sql, tagId, mealId);
            if (rowsAffected != 1) {
                throw new DaoException();
            }
            tags = getTagsByMealId(mealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return tags;
    }

    @Override
    public List<Tag> deleteTagFromMeal(int tagId, int mealId) {

        List<Tag> tags = new ArrayList<>();
        String sql = "DELETE FROM tags_meal " +
                "WHERE tag_id = ? " +
                "AND meal_id = ?;";

        try {
            int rowsAffected = jdbcTemplate.update(sql, tagId, mealId);
            if (rowsAffected != 1) {
                throw new DaoException();
            }
            tags = getTagsByMealId(mealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return tags;
    }

    @Override
    public void deleteTag(int tagId) {

        String sql = "DELETE FROM tags_meal " +
                "WHERE tag_id = ?";

        String sql2 = "DELETE FROM tags " +
                "WHERE tag_id = ?;";

        try {
            jdbcTemplate.update(sql, tagId);
            int rowsAffected = jdbcTemplate.update(sql2, tagId);
            if (rowsAffected != 1) {
                throw new DaoException();
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    private Tag mapRowToTag(SqlRowSet rs) {
        Tag tag = new Tag();

        tag.setTagId(rs.getInt("tag_id"));
        tag.setTagName(rs.getString("tag_name"));

        return tag;
    }
}