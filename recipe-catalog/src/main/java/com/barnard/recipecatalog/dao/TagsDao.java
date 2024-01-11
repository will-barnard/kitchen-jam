package com.barnard.recipecatalog.dao;

import com.barnard.recipecatalog.model.Meal;
import com.barnard.recipecatalog.model.Tag;

import java.util.List;

public interface TagsDao {

    Tag getTag(int tagId);
    Tag createTag(Tag tag);
    Tag updateTag(Tag tag);
    int deleteTag(int tagId);

}
