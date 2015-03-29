package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.Favorite;

public interface FavoriteService {

    public Favorite create(Favorite favorite);

    public Favorite update(Favorite favorite);

    public boolean delete(String favoriteId);

    public Favorite getById(String favoriteId);

    public List<Favorite> getByUserId(String userId);
}
