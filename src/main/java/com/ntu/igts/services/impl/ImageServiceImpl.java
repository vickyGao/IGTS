package com.ntu.igts.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.model.Image;
import com.ntu.igts.repository.ImageRepository;
import com.ntu.igts.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

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

}
