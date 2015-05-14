package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.model.Image;
import com.ntu.igts.repository.ImageRepository;
import com.ntu.igts.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger LOGGER = Logger.getLogger(ImageServiceImpl.class);

    @Resource
    private ImageRepository imageRepository;

    @Override
    @Transactional
    public Image create(Image image) {
        return imageRepository.create(image);
    }

    @Override
    @Transactional
    public Image update(Image image) {
        return imageRepository.update(image);
    }

    @Override
    public Image getById(String imageId) {
        return imageRepository.findById(imageId);
    }

    @Override
    @Transactional
    public boolean delete(String imageId) {
        imageRepository.delete(imageId);
        Image image = imageRepository.findById(imageId);
        if (image == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Image> getImagesByUserId(String userId) {
        return imageRepository.getImagesByUserId(userId);
    }

    @Override
    public Image getImageByUri(String uri) {
        List<Image> images = imageRepository.getImagesByUri(uri);
        if (images.size() == 0) {
            return null;
        } else if (images.size() == 1) {
            return images.get(0);
        } else {
            LOGGER.warn("Duplicate image uri exists");
            return images.get(0);
        }
    }

}
