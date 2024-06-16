package com.barnard.controller;

import com.barnard.dao.ImageDao;
import com.barnard.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
@RequestMapping(path = "/image")
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageDao imageDao;
    private final String imageDirectory = "/Users/will/Desktop";

    @PostMapping(path = "/create")
    public int createImage(@RequestParam("image") MultipartFile[] image) {
        String imageString = null;

        for (MultipartFile imageFile : image) {
            try {
                imageString = imageService.saveImageToStorage(imageDirectory, imageFile);
            } catch (Exception e) {
                System.out.println("Something went wrong");
            }
        }
        int imageId = 0;
        try {
            imageId = imageDao.createImage(imageString);
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }

        return imageId;
    }

    @GetMapping(path = "/{imageId}")
    public byte[] getImage(@PathVariable int imageId) {
        String imageName = imageDao.getImagePathById(imageId);
        byte[] imageBytes = null;
        try {
            imageBytes = imageService.getImage(imageDirectory, imageName);
        } catch (Exception e) {
            System.out.println("Something went wrong retrieving an image");
        }
        return imageBytes;
    }

    @DeleteMapping(path = "/delete/{imageId}")
    public void deleteImage(@PathVariable int imageId) {
        String imageName = imageDao.getImagePathById(imageId);
        try {
            imageService.deleteImage(imageDirectory, imageName);
        } catch (Exception e) {
            System.out.println("Something went wrong deleting an image");
        }
    }

    @PostMapping(path = "/meal/{mealId}/{imageId}")
    public byte[] addImageToMeal(@PathVariable int imageId, @PathVariable int mealId) {
        imageDao.addImageToMeal(mealId, imageId);
        byte[] image = null;
        try {
            image = imageService.getImage(imageDirectory, imageDao.getImagePathById(imageId));
        } catch (Exception e) {
            System.out.println("Something went wrong adding an image to meal");
        }
        return image;
    }

    @PostMapping(path = "/recipe/{recipeId}/{imageId}")
    public byte[] addImageToRecipe(@PathVariable int imageId, @PathVariable int recipeId) {
        imageDao.addImageToRecipe(recipeId, imageId);
        byte[] image = null;
        try {
            image = imageService.getImage(imageDirectory, imageDao.getImagePathById(imageId));
        } catch (Exception e) {
            System.out.println("Something went wrong adding an image to recipe");
        }
        return image;
    }

    @PutMapping(path = "/meal/{mealId}/{imageId}")
    public byte[] updateMealImage(@PathVariable int imageId, @PathVariable int mealId) {
        imageDao.updateMealImage(mealId, imageId);
        byte[] image = null;
        try {
            image = imageService.getImage(imageDirectory, imageDao.getImagePathById(imageId));
        } catch (Exception e) {
            System.out.println("Something went wrong updating an image to meal");
        }
        return image;
    }

    @PutMapping(path = "/recipe/{recipeId}/{imageId}")
    public byte[] updateRecipeImage(@PathVariable int imageId, @PathVariable int recipeId) {
        imageDao.updateRecipeImage(recipeId, imageId);
        byte[] image = null;
        try {
            image = imageService.getImage(imageDirectory, imageDao.getImagePathById(imageId));
        } catch (Exception e) {
            System.out.println("Something went wrong updating an image to recipe");
        }
        return image;
    }

    @DeleteMapping(path = "/meal/{mealId}/{imageId}")
    public void removeImageFromMeal(@PathVariable int imageId, @PathVariable int mealId) {
        imageDao.removeImageFromMeal(mealId);
    }

    @DeleteMapping(path = "/recipe/{recipeId}/{imageId}")
    public void removeImageFromRecipe(@PathVariable int imageId, @PathVariable int recipeId) {
        imageDao.removeImageFromRecipe(recipeId);
    }
}
