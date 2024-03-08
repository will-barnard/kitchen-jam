package com.barnard.dao;


import com.barnard.model.Tag;

import java.util.List;

public interface TagsDao {

    Tag getTag(int tagId);
    List<Tag> getTagsByMealId(int mealId);
    Tag createTag(Tag tag);
    Tag updateTag(Tag tag);
    void addTagToMeal(int tagId, int mealId);
    void removeTagFromMeal(int tagId, int mealId);
    void deleteTag(int tagId);

}