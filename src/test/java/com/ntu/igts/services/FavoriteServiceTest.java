package com.ntu.igts.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Favorite;
import com.ntu.igts.model.Tag;
import com.ntu.igts.test.Order;

public class FavoriteServiceTest extends TestBase {

    @Resource
    private FavoriteService favoriteService;
    @Resource
    private CommodityService commodityService;
    @Resource
    private TagService tagService;
    private static Favorite favorite;
    private static Commodity commodity;
    private static Tag tag;

    @Test
    @Order(0)
    public void testCreate() {
        mockUpUser();
        mockUpCommodity();

        Favorite testFavorite = new Favorite();
        testFavorite.setCommodityId(commodity.getId());
        testFavorite.setUserId(user.getId());
        Favorite insertedFavorite = favoriteService.create(testFavorite);

        assertNotNull("Create favorite failed", insertedFavorite);
        favorite = insertedFavorite;
    }

    @Test
    @Order(10)
    public void testUpdate() {
        Favorite updatedFavorite = favoriteService.update(favorite);
        assertNotNull("Update favorite failed", updatedFavorite);
    }

    @Test
    @Order(20)
    public void testGetById() {
        Favorite returnFavorite = favoriteService.getById(favorite.getId());
        assertNotNull("Get favorite by id failed", returnFavorite);
    }

    @Test
    @Order(30)
    public void testGetByUserId() {
        List<Favorite> favoriteList = favoriteService.getByUserId(user.getId());
        assertNotNull("Get favorite list by user id failed", favoriteList);
        assertTrue("Get favorite list by user id failed", favoriteList.size() > 0);
    }

    @Test
    @Order(31)
    public void testGetPaginatedFavoritesByUserId() {
        Page<Favorite> page = favoriteService.getPaginatedFavoritesByUserId(0, 10, user.getId());
        assertNotNull("Get paginated favorite by user id failed", page);
        assertTrue("Get paginated favorite by user id failed", page.getContent().size() > 0);
    }

    @Test
    @Order(40)
    public void testDelete() {
        boolean tagDeleteFlag = tagService.delete(tag.getId());
        assertTrue("Delete tag failed", tagDeleteFlag);

        boolean userDeleteFlag = userService.delete(user.getId());
        assertTrue("Delete user failed", userDeleteFlag);

        boolean commodityDeleteFlag = commodityService.delete(commodity.getId());
        assertTrue("Delete commodity failed", commodityDeleteFlag);

        boolean favoriteDeleteFlag = favoriteService.delete(favorite.getId());
        assertTrue("Delete favorite failed", favoriteDeleteFlag);
    }

    private void mockUpCommodity() {
        if (commodity == null) {
            mockUpUser();
            mockUpTag();

            Commodity testCommodity = new Commodity();
            testCommodity.setTitle("瑞特斯波德 进口巧克力 rittersport运动巧克力7口味进口食品零食");
            testCommodity.setDescription("瑞特斯波德 进口巧克力 rittersport运动巧克力7口味进口食品零食");
            testCommodity.setPrice(68.6);
            testCommodity.setCarriage(3);
            testCommodity.setCollectionNumber(34);
            testCommodity.setDistrict("浙江衢州");
            testCommodity.setUserId(user.getId());
            List<Tag> tags = new ArrayList<Tag>();
            tags.add(tag);
            testCommodity.setTags(tags);
            Commodity insertedCommodity = commodityService.create(testCommodity);
            assertNotNull("Create commodity failed", insertedCommodity);

            commodity = insertedCommodity;
        }
    }

    private void mockUpTag() {
        if (tag == null) {
            String randomNumber = UUID.randomUUID().toString().replace("-", "");

            Tag testTag = new Tag();
            testTag.setName("食品");
            testTag.setStandardName("FOOD" + randomNumber);
            Tag insertedTag = tagService.create(testTag);
            assertNotNull("Create tag failed", insertedTag);

            tag = insertedTag;
        }
    }
}
