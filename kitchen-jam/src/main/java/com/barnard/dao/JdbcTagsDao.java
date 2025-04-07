package com.barnard.dao;


import com.barnard.exception.DaoException;
import com.barnard.model.Tag;
import com.barnard.model.TagCategory;
import com.barnard.model.TagType;
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
                "LEFT JOIN tag_type on tags.tag_type_id = tag_type.tag_type_id " +
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
                "LEFT JOIN tag_type on tags.tag_type_id = tag_type.tag_type_id " +
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
    public List<Tag> getTagsByUserId(int userId) {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM tags " +
                "LEFT JOIN tag_type on tags.tag_type_id = tag_type.tag_type_id " +
                "WHERE user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
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
    public List<Tag> searchLikeTags(int userId, String search) {
        search = "%" + search.toLowerCase() + "%";
        List<Tag> taglist = new ArrayList<>();
        String sql = "SELECT * FROM tags " +
                "LEFT JOIN tag_type on tags.tag_type_id = tag_type.tag_type_id " +
                "WHERE LOWER (tag_name) LIKE ? " +
                "AND user_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, search, userId);
            while(rowSet.next()) {
                taglist.add(mapRowToTag(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return taglist;
    }

    @Override
    public Tag createTag(Tag tag, int userId) {

        Integer tagTypeId = null;
        if (tag.getTagType() != null) {
            if (tag.getTagType().getTagTypeId() != 0) {
                tagTypeId = tag.getTagType().getTagTypeId();
            }
        }

        String sql = "INSERT INTO tags (tag_name, user_id, tag_type_id) " +
                "VALUES (?, ?, ?) " +
                "RETURNING tag_id;";

        try {
            int tagId = jdbcTemplate.queryForObject(sql, int.class, tag.getTagName(), userId, tagTypeId);
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

        Integer tagTypeId = null;
        if (tag.getTagType() != null) {
            if (tag.getTagType().getTagTypeId() != null) {
                if (tag.getTagType().getTagTypeId() != 0) {
                    tagTypeId = tag.getTagType().getTagTypeId();
                }
            }
        }
        Tag updateTag = null;
        String sql = "UPDATE tags SET tag_name = ?, tag_type_id = ? WHERE tag_id = ?;";

        try {
            jdbcTemplate.update(sql, tag.getTagName(), tagTypeId, tag.getTagId());
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
    public List<Tag> updateMealTagList(int mealId, List<Tag> tags) {
        List<Tag> updatedTags = new ArrayList<>();
        String sql = "DELETE FROM tags_meal " +
                "WHERE meal_id = ?; ";
        if (!tags.isEmpty()) {
            String sql2 = "INSERT INTO tags_meal (tag_id, meal_id) " +
                    "VALUES ";
            for (Tag tag : tags) {
                sql2 += "(" + tag.getTagId() + ", " + mealId + "), ";
            }
            sql2 = sql2.substring(0, sql2.length() - 2) + ";";
            sql += sql2;
        }


        try {
            jdbcTemplate.update(sql, mealId);
            updatedTags = getTagsByMealId(mealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedTags;
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

    @Override
    public List<TagCategory> getTagCategories() {
        List<TagCategory> tagCategories = new ArrayList<>();
        String sql = "SELECT * FROM tag_type;";

        try {
            String currentCategory = "";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            while (rowSet.next()) {
                if (currentCategory.equals(rowSet.getString("tag_category"))) {
                    TagType tagType = new TagType(rowSet.getInt("tag_type_id"), rowSet.getString("tag_type_name"), currentCategory);
                    tagCategories.get(tagCategories.size() - 1).getTagTypes().add(tagType);
                } else {
                    currentCategory = rowSet.getString("tag_category");
                    TagCategory tagCategory = new TagCategory();
                    tagCategory.setTagCategoryName(currentCategory);
                    tagCategory.setTagTypes(new ArrayList<>());
                    tagCategories.add(tagCategory);

                    TagType tagType = new TagType(rowSet.getInt("tag_type_id"), rowSet.getString("tag_type_name"), currentCategory);
                    tagCategories.get(tagCategories.size() - 1).getTagTypes().add(tagType);
                }
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return tagCategories;
    }

    private Tag mapRowToTag(SqlRowSet rs) {
        Tag tag = new Tag();

        tag.setTagId(rs.getInt("tag_id"));
        tag.setTagName(rs.getString("tag_name"));
        tag.setUserId(rs.getInt("user_id"));
        if (rs.getString("tag_type_name") != null) {
            TagType tagType = new TagType();
            tagType.setTagTypeId(rs.getInt("tag_type_id"));
            tagType.setTagTypeName(rs.getString("tag_type_name"));
            tagType.setTagCategory(rs.getString("tag_category"));
            tag.setTagType(tagType);
        }

        return tag;
    }
}