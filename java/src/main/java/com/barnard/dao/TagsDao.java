package com.barnard.dao;


import com.barnard.model.Tag;

import java.util.List;

public interface TagsDao {

    Tag getTag(int tagId);
    List<Tag> getTagsByMealId(int mealId);
    Tag createTag(Tag tag, int userId);
    Tag updateTag(Tag tag);
    List<Tag> addTagToMeal(int tagId, int mealId);
    List<Tag> deleteTagFromMeal(int tagId, int mealId);
    void deleteTag(int tagId);

}