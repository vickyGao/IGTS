package com.ntu.igts.services;

import javax.annotation.Resource;

import org.junit.Test;

import com.ntu.igts.model.Image;
import com.ntu.igts.test.Order;

public class ImageServiceTest extends TestBase {

    @Resource
    private ImageService imageService;
    private static Image image;

    @Test
    @Order(0)
    public void testCreate() {
        Image testImage = new Image();
        testImage.setTitle("test image");
        testImage.setUri("D:/image/001.jpg");
        Image insertedImage = imageService.create(testImage);
        assertNotNull("Create image failed", insertedImage);

        image = insertedImage;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        image.setDescription("test update");
        Image updatedImage = imageService.update(image);
        assertNotNull("Update image failed", updatedImage);
    }

    @Test
    @Order(20)
    public void testGetById() {
        Image returnImage = imageService.getById(image.getId());
        assertNotNull("Get image failed", returnImage);
    }

    @Test
    @Order(30)
    public void testDelete() {
        boolean flag = imageService.delete(image.getId());
        assertTrue("Delete image failed", flag);
    }
}
