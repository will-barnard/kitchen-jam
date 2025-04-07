package com.barnard.model;

public class TagType {

    private Integer tagTypeId;
    private String tagTypeName;
    private String tagCategory;

    public TagType() {
    }

    public TagType(int tagTypeId, String tagTypeName, String tagCategory) {
        this.tagTypeId = tagTypeId;
        this.tagTypeName = tagTypeName;
        this.tagCategory = tagCategory;
    }

    public Integer getTagTypeId() {
        return tagTypeId;
    }

    public void setTagTypeId(Integer tagTypeId) {
        this.tagTypeId = tagTypeId;
    }

    public String getTagTypeName() {
        return tagTypeName;
    }

    public void setTagTypeName(String tagTypeName) {
        this.tagTypeName = tagTypeName;
    }

    public String getTagCategory() {
        return tagCategory;
    }

    public void setTagCategory(String tagCategory) {
        this.tagCategory = tagCategory;
    }
}
