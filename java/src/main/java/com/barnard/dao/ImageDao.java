package com.barnard.dao;


import com.barnard.model.Image;

public interface ImageDao {

    Image createImage(Image image);
    Image getImageById(int imageId);
    Image updateImage(Image image);
    int deleteImageByImageId(int imageId);

}
