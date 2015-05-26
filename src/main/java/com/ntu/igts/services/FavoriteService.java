package com.ntu.igts.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ntu.igts.model.Favorite;

public interface FavoriteService {

    public Favorite create(Favorite favorite);

    public Favorite update(Favorite favorite);

    public boolean delete(String favoriteId);

    public Favorite getById(String favoriteId);

    public List<Favorite> getByUserId(String userId);

    public Page<Favorite> getPaginatedFavoritesByUserId(int currentPage, int pageSize, String userId);

    public List<Favorite> getByCommodityIdAndUserId(String commodityId, String userId);
}
