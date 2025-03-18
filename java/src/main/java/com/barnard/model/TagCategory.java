package com.barnard.model;

import java.util.List;

public class TagCategory {

    private String tagCategoryName;
    private List<TagType> tagTypes;

    public String getTagCategoryName() {
        return tagCategoryName;
    }

    public void setTagCategoryName(String TagCategoryName) {
        this.tagCategoryName = TagCategoryName;
    }

    public List<TagType> getTagTypes() {
        return tagTypes;
    }

    public void setTagTypes(List<TagType> tagTypes) {
        this.tagTypes = tagTypes;
    }
}
