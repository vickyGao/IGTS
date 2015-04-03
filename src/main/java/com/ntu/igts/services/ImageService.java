package com.ntu.igts.services;

import com.ntu.igts.model.Image;

public interface ImageService {

    public Image create(Image image);

    public Image update(Image image);

    public Image getById(String imageId);

    public boolean delete(String imageId);
}
