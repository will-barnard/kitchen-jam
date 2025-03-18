package com.barnard.dao;


import com.barnard.model.Tag;
import com.barnard.model.TagCategory;

import java.util.List;

public interface TagsDao {

    Tag getTag(int tagId);
    List<Tag> getTagsByMealId(int mealId);
    List<Tag> getTagsByUserId(int userId);
    List<Tag> searchLikeTags(int userId, String search);
    Tag createTag(Tag tag, int userId);
    Tag updateTag(Tag tag);
    List<Tag> updateMealTagList(int mealId, List<Tag> tags);
    void deleteTag(int tagId);

    List<TagCategory> getTagCategories();

}