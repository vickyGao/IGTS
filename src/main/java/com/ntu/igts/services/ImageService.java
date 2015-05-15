package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.Image;

public interface ImageService {

    public Image create(Image image);

    public Image update(Image image);

    public Image getById(String imageId);

    public boolean delete(String imageId);

    public List<Image> getImagesByUserId(String userId);

    public Image getImageByUri(String uri);

    public int getTotalCount();

    public List<Image> getAll();
}
