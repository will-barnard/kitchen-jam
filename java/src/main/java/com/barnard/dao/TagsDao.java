package com.barnard.dao;


import com.barnard.model.Tag;

import java.util.List;

public interface TagsDao {

    Tag getTag(int tagId);
    List<Tag> getTagsByMealId(int mealId);
    List<Tag> getTagsByUserId(int userId);
    List<Tag> searchLikeTags(int userId, String search);
    Tag createTag(Tag tag, int userId);
    Tag updateTag(Tag tag);

    // todo remove these methods
    List<Tag> addTagToMeal(int tagId, int mealId);
    List<Tag> deleteTagFromMeal(int tagId, int mealId);

    List<Tag> updateMealTagList(int mealId, List<Tag> tags);


    void deleteTag(int tagId);

}