package com.ntu.igts.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Favorite;
import com.ntu.igts.repository.FavoriteRepository;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Resource
    private FavoriteRepository favoriteRepository;
    @Resource
    private CommodityService commodityService;

    @Override
    public Favorite create(Favorite favorite) {
        return favoriteRepository.create(favorite);
    }

    @Override
    public Favorite update(Favorite favorite) {
        return favoriteRepository.update(favorite);
    }

    @Override
    public boolean delete(String favoriteId) {
        favoriteRepository.delete(favoriteId);
        Favorite favorite = favoriteRepository.findById(favoriteId);
        if (favorite == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Favorite getById(String favoriteId) {
        return favoriteRepository.findById(favoriteId);
    }

    @Override
    public List<Favorite> getByUserId(String userId) {
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(Constants.FIELD_USERID, userId);
        return favoriteRepository.findAll(SortByEnum.CREATED_TIME, OrderByEnum.DESC, criteriaMap);
    }

    @Override
    public Page<Favorite> getPaginatedFavoritesByUserId(int currentPage, int pageSize, String userId) {
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(Constants.FIELD_USERID, userId);
        Page<Favorite> favoritePage = favoriteRepository.findByPage(currentPage, pageSize, SortByEnum.CREATED_TIME,
                        OrderByEnum.DESC, criteriaMap);
        List<Favorite> favorites = favoritePage.getContent();
        if (favorites != null) {
            for (Favorite favorite : favorites) {
                favorite.setCommodity(commodityService.getCommodityWithDetailById(favorite.getCommodityId()));
            }
        }
        return favoritePage;
    }

}
