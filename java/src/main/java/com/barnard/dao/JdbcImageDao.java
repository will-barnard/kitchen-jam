package com.barnard.dao;

import com.barnard.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcImageDao implements ImageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public Image createImage(Image image) {
        return null;
    }

    @Override
    public Image getImageById(int imageId) {
        return null;
    }

    @Override
    public Image updateImage(Image image) {
        return null;
    }

    @Override
    public int deleteImageByImageId(int imageId) {
        return 0;
    }
}
