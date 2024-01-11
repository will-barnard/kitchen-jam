package com.barnard.recipecatalog.dao;

import com.barnard.recipecatalog.model.Image;

public interface ImageDao {

    Image createImage(Image image);
    Image getImageById(int imageId);
    Image updateImage(Image image);
    int deleteImageByImageId(int imageId);

}
