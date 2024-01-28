package com.barnard.dao;


import com.barnard.model.Tag;

public interface TagsDao {

    Tag getTag(int tagId);
    Tag createTag(Tag tag);
    Tag updateTag(Tag tag);
    int deleteTag(int tagId);

}
