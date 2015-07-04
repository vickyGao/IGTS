package com.ntu.igts.dbinit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.model.Address;
import com.ntu.igts.model.Admin;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Cover;
import com.ntu.igts.model.Hot;
import com.ntu.igts.model.Image;
import com.ntu.igts.model.Message;
import com.ntu.igts.model.Role;
import com.ntu.igts.model.Slice;
import com.ntu.igts.model.Tag;
import com.ntu.igts.model.User;
import com.ntu.igts.services.AddressService;
import com.ntu.igts.services.AdminService;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.CoverService;
import com.ntu.igts.services.HotService;
import com.ntu.igts.services.ImageService;
import com.ntu.igts.services.MessageService;
import com.ntu.igts.services.RoleService;
import com.ntu.igts.services.SensitiveWordService;
import com.ntu.igts.services.SliceService;
import com.ntu.igts.services.TagService;
import com.ntu.igts.services.UserService;
import com.ntu.igts.utils.ConfigManagmentUtil;

@Component
public class InitData {

    private static final Logger LOGGER = Logger.getLogger(InitData.class);

    private Role roleUser;
    private Role roleAdmin;
    private User standardUser;
    private Admin standardAdmin;
    private User sampleBuyer;
    private User sampleSeller;

    private List<Tag> categoryTags = new ArrayList<Tag>();
    private List<Image> sampleImageList = new ArrayList<Image>();
    private List<Commodity> sampleCommodities = new ArrayList<Commodity>();

    @Resource
    private UserService userService;
    @Resource
    private AdminService adminService;
    @Resource
    private RoleService roleService;
    @Resource
    private TagService tagService;
    @Resource
    private CommodityService commodityService;
    @Resource
    private ImageService imageService;
    @Resource
    private CoverService coverService;
    @Resource
    private MessageService messageService;
    @Resource
    private SensitiveWordService sensitiveWordService;
    @Resource
    private SliceService sliceService;
    @Resource
    private HotService hotService;
    @Resource
    private AddressService addressService;

    public void createStandardData() {
        LOGGER.info("Start to init standard data");
        createRole();
        createUser();
        createAdmin();
        LOGGER.info("Init standard data finished");
    }

    public void createSampleData() {
        LOGGER.info("Start to init sample data");
        createSampleUser();
        updateSampleUserDetail();
        createSampleImages();
        createSampleTags();
        createSampleCommodities();
        createSampleSlices();
        createSampleHots();
        createSampleAddress();
        LOGGER.info("Sample standard data finished");
    }

    private void createUser() {
        User user = new User();
        user.setUserName("user");
        user.setPassword("password");
        List<Role> roles = new ArrayList<Role>();
        roles.add(roleUser);
        user.setRoles(roles);
        standardUser = userService.create(user);
        LOGGER.info("Created user " + standardUser.getUserName());
    }

    private void createAdmin() {
        Admin admin = new Admin();
        admin.setAdminName("user");
        admin.setAdminPassword("password");
        List<Role> roles = new ArrayList<Role>();
        roles.add(roleAdmin);
        admin.setRoles(roles);
        standardAdmin = adminService.create(admin);
        LOGGER.info("Created admin " + standardAdmin.getAdminName());
    }

    private void createSampleUser() {

        User buyer = new User();
        buyer.setUserName("buyer");
        buyer.setPassword("password");
        List<Role> buyerRoles = new ArrayList<Role>();
        buyerRoles.add(roleUser);
        buyer.setRoles(buyerRoles);
        buyer.setMoney(99999);
        sampleBuyer = userService.create(buyer);

        User seller = new User();
        seller.setUserName("seller");
        seller.setPassword("password");
        List<Role> sellerRoles = new ArrayList<Role>();
        sellerRoles.add(roleUser);
        seller.setRoles(sellerRoles);
        buyer.setMoney(99999);
        sampleSeller = userService.create(seller);
    }

    private void createRole() {
        Role role1 = new Role();
        role1.setRoleName("user");
        role1.setRoleStandardName("USER");
        roleUser = roleService.create(role1);
        LOGGER.info("Created role " + roleUser.getRoleName());

        Role role2 = new Role();
        role2.setRoleName("admin");
        role2.setRoleStandardName("ADMIN");
        roleAdmin = roleService.create(role2);
        LOGGER.info("Created role " + roleAdmin.getRoleName());
    }

    private void createSampleTags() {
        List<Tag> foodSubTags = new ArrayList<Tag>();
        // Generate Food Tag with sub-Tags
        Tag foodTag = new Tag();
        foodTag.setName("零食");
        foodTag.setStandardName("FOOD");
        foodTag.setParentId(null);
        Tag insertedFoodTag = tagService.create(foodTag);

        Tag chocolateTag = new Tag();
        chocolateTag.setName("巧克力");
        chocolateTag.setStandardName("CHOCOLATE");
        chocolateTag.setParentId(insertedFoodTag.getId());
        foodSubTags.add(tagService.create(chocolateTag));

        Tag cakeTag = new Tag();
        cakeTag.setName("饼干");
        cakeTag.setStandardName("COOKIE");
        cakeTag.setParentId(insertedFoodTag.getId());
        foodSubTags.add(tagService.create(cakeTag));

        insertedFoodTag.setTags(foodSubTags);
        categoryTags.add(insertedFoodTag);

        // Generate application Tag with sub-Tags
        List<Tag> applicationSubTags = new ArrayList<Tag>();
        Tag applicationTag = new Tag();
        applicationTag.setName("家电");
        applicationTag.setStandardName("APPLICATION");
        applicationTag.setParentId(null);
        Tag insertedApplicationTag = tagService.create(applicationTag);

        Tag tvTag = new Tag();
        tvTag.setName("电视机");
        tvTag.setStandardName("TV");
        tvTag.setParentId(insertedApplicationTag.getId());
        applicationSubTags.add(tagService.create(tvTag));

        Tag iceTag = new Tag();
        iceTag.setName("冰箱");
        iceTag.setStandardName("ICE");
        iceTag.setParentId(insertedApplicationTag.getId());
        applicationSubTags.add(tagService.create(iceTag));

        Tag phoneTag = new Tag();
        phoneTag.setName("手机");
        phoneTag.setStandardName("PHONE");
        phoneTag.setParentId(insertedApplicationTag.getId());
        applicationSubTags.add(tagService.create(phoneTag));

        Tag computerTag = new Tag();
        computerTag.setName("电脑");
        computerTag.setStandardName("COMPUTER");
        computerTag.setParentId(insertedApplicationTag.getId());
        applicationSubTags.add(tagService.create(computerTag));

        Tag otherAppTag = new Tag();
        otherAppTag.setName("其他");
        otherAppTag.setStandardName("OTHERS");
        otherAppTag.setParentId(insertedApplicationTag.getId());
        applicationSubTags.add(tagService.create(otherAppTag));

        insertedApplicationTag.setTags(applicationSubTags);
        categoryTags.add(insertedApplicationTag);

        // Generate clothing Tag with sub-Tags
        List<Tag> clothingSubTags = new ArrayList<Tag>();
        Tag clothingTag = new Tag();
        clothingTag.setName("服装");
        clothingTag.setStandardName("CLOTHING");
        clothingTag.setParentId(null);
        Tag insertedClothingTag = tagService.create(clothingTag);

        Tag womenUpTag = new Tag();
        womenUpTag.setName("女装上衣");
        womenUpTag.setStandardName("WOMENUP");
        womenUpTag.setParentId(insertedClothingTag.getId());
        clothingSubTags.add(tagService.create(womenUpTag));

        Tag womenDownTag = new Tag();
        womenDownTag.setName("女装下衣");
        womenDownTag.setStandardName("WOMENDOWN");
        womenDownTag.setParentId(insertedClothingTag.getId());
        clothingSubTags.add(tagService.create(womenDownTag));

        insertedClothingTag.setTags(clothingSubTags);
        categoryTags.add(insertedClothingTag);

        // Generate shoe Tag with sub-Tags
        List<Tag> shoesSubTags = new ArrayList<Tag>();
        Tag shoesTag = new Tag();
        shoesTag.setName("鞋子");
        shoesTag.setStandardName("SHOE");
        shoesTag.setParentId(null);
        Tag insertedShoesTag = tagService.create(shoesTag);

        Tag slippersTag = new Tag();
        slippersTag.setName("女鞋");
        slippersTag.setStandardName("SLIPPER");
        slippersTag.setParentId(insertedShoesTag.getId());
        shoesSubTags.add(tagService.create(slippersTag));

        insertedShoesTag.setTags(shoesSubTags);
        categoryTags.add(insertedShoesTag);

        // Generate bag Tag with sub-Tags
        List<Tag> bagSubTags = new ArrayList<Tag>();
        Tag bagTag = new Tag();
        bagTag.setName("箱包");
        bagTag.setStandardName("BAG");
        bagTag.setParentId(null);
        Tag insertedBagTag = tagService.create(bagTag);

        Tag womenHandsTag = new Tag();
        womenHandsTag.setName("女款手提包");
        womenHandsTag.setStandardName("WOMENHANDS");
        womenHandsTag.setParentId(insertedBagTag.getId());
        bagSubTags.add(tagService.create(womenHandsTag));

        Tag backBagTag = new Tag();
        backBagTag.setName("背包");
        backBagTag.setStandardName("BACKBAG");
        backBagTag.setParentId(insertedBagTag.getId());
        bagSubTags.add(tagService.create(backBagTag));

        Tag lvTag = new Tag();
        lvTag.setName("拉箱");
        lvTag.setStandardName("LA");
        lvTag.setParentId(insertedBagTag.getId());
        bagSubTags.add(tagService.create(lvTag));

        insertedBagTag.setTags(bagSubTags);
        categoryTags.add(insertedBagTag);

        // Generate book Tag with sub-Tags
        List<Tag> bookSubTags = new ArrayList<Tag>();
        Tag bookTag = new Tag();
        bookTag.setName("图书影像");
        bookTag.setStandardName("BOOK");
        bookTag.setParentId(null);
        Tag insertedBookTag = tagService.create(bookTag);

        Tag storyTag = new Tag();
        storyTag.setName("图书");
        storyTag.setStandardName("STORY");
        storyTag.setParentId(insertedBookTag.getId());
        bookSubTags.add(tagService.create(storyTag));

        insertedBookTag.setTags(bookSubTags);
        categoryTags.add(insertedBookTag);
    }

    private void createSampleCommodities() {
        // insert the food Commodity
        Commodity foodItem01 = new Commodity();
        foodItem01.setTitle("瑞特斯波德 进口巧克力 rittersport运动巧克力7口味进口食品零食");
        foodItem01.setDescription("瑞特斯波德 进口巧克力 rittersport运动巧克力7口味进口食品零食");
        foodItem01.setPrice(68.6);
        foodItem01.setCarriage(3);
        foodItem01.setCollectionNumber(34);
        foodItem01.setUserId(sampleSeller.getId());
        foodItem01.setDistrict("浙江衢州");
        foodItem01.setReleaseDate(new Date());
        List<Tag> foodItem01Tags = new ArrayList<Tag>();
        foodItem01Tags.add(categoryTags.get(0));
        foodItem01Tags.add(categoryTags.get(0).getTags().get(0));
        foodItem01.setTags(foodItem01Tags);
        foodItem01 = commodityService.create(foodItem01);
        createCoversForCommodity(foodItem01.getId(), 0);
        createSampleMessagesForCommodity(foodItem01.getId());
        sampleCommodities.add(foodItem01);

        Commodity foodItem02 = new Commodity();
        foodItem02.setTitle("韩国进口零食品乐天红加纳牛奶巧克力块90g 情人节喜糖年货糖果");
        foodItem02.setDescription("丝丝香浓，入口即化！纯可可脂巧克力，口感秒杀国内代可可脂巧克力!好吃不胖的巧克力 可做年货、喜糖，让喜事更加喜庆~");
        foodItem02.setPrice(432);
        foodItem02.setCarriage(20);
        foodItem02.setCollectionNumber(5);
        foodItem02.setDistrict("上海");
        foodItem02.setUserId(sampleSeller.getId());
        foodItem02.setReleaseDate(new Date());
        List<Tag> foodItem02Tags = new ArrayList<Tag>();
        foodItem02Tags.add(categoryTags.get(0));
        foodItem02Tags.add(categoryTags.get(0).getTags().get(0));
        foodItem02.setTags(foodItem02Tags);
        foodItem02 = commodityService.create(foodItem02);
        createCoversForCommodity(foodItem02.getId(), 1);
        createSampleMessagesForCommodity(foodItem02.getId());

        Commodity foodItem03 = new Commodity();
        foodItem03.setTitle("意大利费列罗巧克力威化果仁金莎巧克力礼盒装费雷罗进口零食24粒");
        foodItem03.setDescription("【自己买正品！");
        foodItem03.setPrice(67.9);
        foodItem03.setCarriage(0);
        foodItem03.setCollectionNumber(99);
        foodItem03.setDistrict("河北廊坊");
        foodItem03.setReleaseDate(new Date());
        foodItem03.setUserId(sampleSeller.getId());
        List<Tag> foodItem03Tags = new ArrayList<Tag>();
        foodItem03Tags.add(categoryTags.get(0));
        foodItem03Tags.add(categoryTags.get(0).getTags().get(0));
        foodItem03.setTags(foodItem03Tags);
        foodItem03 = commodityService.create(foodItem03);
        createCoversForCommodity(foodItem03.getId(), 2);
        createSampleMessagesForCommodity(foodItem03.getId());

        Commodity foodItem04 = new Commodity();
        foodItem04.setTitle("泰国进口小老板即食海苔烤紫菜卷原味辣味套餐36g袋装9gX6条");
        foodItem04.setDescription("精选泰国进口海藻，新鲜零污染原料，不添加防腐剂，不添加人工色素，烤出来的海苔卷，香脆无比！开袋即食！");
        foodItem04.setPrice(22.9);
        foodItem04.setCarriage(0);
        foodItem04.setCollectionNumber(10);
        foodItem04.setDistrict("广东广州");
        foodItem04.setReleaseDate(new Date());
        foodItem04.setUserId(sampleSeller.getId());
        List<Tag> foodItem04Tags = new ArrayList<Tag>();
        foodItem04Tags.add(categoryTags.get(0));
        foodItem04Tags.add(categoryTags.get(0).getTags().get(1));
        foodItem04.setTags(foodItem04Tags);
        foodItem04.setReleaseDate(new Date());
        foodItem04 = commodityService.create(foodItem04);
        createCoversForCommodity(foodItem04.getId(), 3);
        createSampleMessagesForCommodity(foodItem04.getId());

        Commodity foodItem05 = new Commodity();
        foodItem05.setTitle("意大利原装进口 可意奇 奶油圈饼干300g 营养健康 早餐必备");
        foodItem05.setDescription("自然发酵，烘培适中、香气浓郁、营养价值高、富含纤维利于消化，低热量，有益健康，方便易取。");
        foodItem05.setPrice(5.6);
        foodItem05.setCarriage(1);
        foodItem05.setCollectionNumber(0);
        foodItem05.setDistrict("天津");
        foodItem05.setReleaseDate(new Date());
        foodItem05.setUserId(sampleSeller.getId());
        List<Tag> foodItem05Tags = new ArrayList<Tag>();
        foodItem05Tags.add(categoryTags.get(0));
        foodItem05Tags.add(categoryTags.get(0).getTags().get(1));
        foodItem05.setTags(foodItem05Tags);
        foodItem05 = commodityService.create(foodItem05);
        createCoversForCommodity(foodItem05.getId(), 4);
        createSampleMessagesForCommodity(foodItem05.getId());

        Commodity foodItem06 = new Commodity();
        foodItem06.setTitle("进口乐事薯片 墨西哥原装 Lay's原味/咸酸味薯片163g罐装进口薯片");
        foodItem06.setDescription("墨西哥原装进口罐装乐事原味薯片 办公室零食小吃 美味休闲食品 享受乐事好滋味～");
        foodItem06.setPrice(16.8);
        foodItem06.setCarriage(3);
        foodItem06.setCollectionNumber(2);
        foodItem06.setDistrict("上海");
        foodItem06.setReleaseDate(new Date());
        foodItem06.setUserId(sampleSeller.getId());
        List<Tag> foodItem06Tags = new ArrayList<Tag>();
        foodItem06Tags.add(categoryTags.get(0));
        foodItem06Tags.add(categoryTags.get(0).getTags().get(1));
        foodItem06.setTags(foodItem06Tags);
        foodItem06 = commodityService.create(foodItem06);
        createCoversForCommodity(foodItem06.getId(), 5);
        createSampleMessagesForCommodity(foodItem06.getId());

        // insert the application Commodity
        Commodity appItem01 = new Commodity();
        appItem01.setTitle("[预售 年前发货]HTC D826w Desire 双4G 8核安卓智能手机");
        appItem01.setDescription("超强前置 甩掉单反 合二为一！");
        appItem01.setPrice(2288);
        appItem01.setCarriage(10);
        appItem01.setCollectionNumber(7);
        appItem01.setDistrict("上海");
        appItem01.setUserId(sampleSeller.getId());
        List<Tag> appItem01Tags = new ArrayList<Tag>();
        appItem01Tags.add(categoryTags.get(1));
        appItem01Tags.add(categoryTags.get(1).getTags().get(2));
        appItem01.setTags(appItem01Tags);
        appItem01.setReleaseDate(new Date());
        appItem01 = commodityService.create(appItem01);
        createCoversForCommodity(appItem01.getId(), 6);
        createSampleMessagesForCommodity(appItem01.getId());
        sampleCommodities.add(appItem01);

        Commodity appItem02 = new Commodity();
        appItem02.setTitle("小米官方旗舰店MIUI/小米 小米手机4 小米4代 MI4智能4G手机");
        appItem02.setDescription("工艺和手感，超乎想象");
        appItem02.setPrice(1999);
        appItem02.setCarriage(30);
        appItem02.setCollectionNumber(20);
        appItem02.setDistrict("北京");
        appItem02.setReleaseDate(new Date());
        appItem02.setUserId(sampleSeller.getId());
        List<Tag> appItem02Tags = new ArrayList<Tag>();
        appItem02Tags.add(categoryTags.get(1));
        appItem02Tags.add(categoryTags.get(1).getTags().get(2));
        appItem02.setTags(appItem02Tags);
        appItem02 = commodityService.create(appItem02);
        createCoversForCommodity(appItem02.getId(), 7);
        createSampleMessagesForCommodity(appItem02.getId());

        Commodity appItem03 = new Commodity();
        appItem03.setTitle("小米官方旗舰店MIUI/小米 红米Note 4G增强版移动版大屏智能手机");
        appItem03.setDescription("那时候找了很多人帮忙抢到的");
        appItem03.setPrice(899);
        appItem03.setCarriage(23);
        appItem03.setCollectionNumber(15);
        appItem03.setDistrict("北京");
        appItem03.setReleaseDate(new Date());
        appItem03.setUserId(sampleSeller.getId());
        List<Tag> appItem03Tags = new ArrayList<Tag>();
        appItem03Tags.add(categoryTags.get(1));
        appItem03Tags.add(categoryTags.get(1).getTags().get(2));
        appItem03.setTags(appItem03Tags);
        appItem03 = commodityService.create(appItem03);
        createCoversForCommodity(appItem03.getId(), 8);
        createSampleMessagesForCommodity(appItem03.getId());

        Commodity appItem04 = new Commodity();
        appItem04.setTitle("【新品首发】skullcandy LOWRIDER 2.0 骷髅头潮流头戴式带麦耳机");
        appItem04.setDescription("都没怎么用过");
        appItem04.setPrice(2444);
        appItem04.setCarriage(65);
        appItem04.setCollectionNumber(23);
        appItem04.setDistrict("北京");
        appItem04.setReleaseDate(new Date());
        appItem04.setUserId(sampleSeller.getId());
        List<Tag> appItem04Tags = new ArrayList<Tag>();
        appItem04Tags.add(categoryTags.get(1));
        appItem04Tags.add(categoryTags.get(1).getTags().get(4));
        appItem04.setTags(appItem04Tags);
        appItem04 = commodityService.create(appItem04);
        createCoversForCommodity(appItem04.getId(), 9);
        createSampleMessagesForCommodity(appItem04.getId());

        Commodity appItem05 = new Commodity();
        appItem05.setTitle("Toshiba/东芝 48L3350C 48英寸3D网络WiFi高清液晶电视平板电视");
        appItem05.setDescription("好心疼");
        appItem05.setPrice(5632);
        appItem05.setCarriage(25);
        appItem05.setReleaseDate(new Date());
        appItem05.setCollectionNumber(31);
        appItem05.setDistrict("电器城上海仓");
        appItem05.setUserId(sampleSeller.getId());
        List<Tag> appItem05Tags = new ArrayList<Tag>();
        appItem05Tags.add(categoryTags.get(1));
        appItem05Tags.add(categoryTags.get(1).getTags().get(0));
        appItem05.setTags(appItem05Tags);
        appItem05 = commodityService.create(appItem05);
        createCoversForCommodity(appItem05.getId(), 10);
        createSampleMessagesForCommodity(appItem05.getId());
        sampleCommodities.add(appItem05);

        Commodity appItem06 = new Commodity();
        appItem06.setTitle("Haier/海尔 BC/BD-102HT/家用小冰柜 冷柜/大冷冻/冷藏冷冻省电");
        appItem06.setDescription("赔本大甩卖");
        appItem06.setPrice(643);
        appItem06.setCarriage(23);
        appItem06.setCollectionNumber(54);
        appItem06.setDistrict("山东青岛");
        appItem06.setReleaseDate(new Date());
        List<Tag> appItem06Tags = new ArrayList<Tag>();
        appItem06Tags.add(categoryTags.get(1));
        appItem06Tags.add(categoryTags.get(1).getTags().get(1));
        appItem06.setTags(appItem06Tags);
        appItem06.setUserId(sampleSeller.getId());
        appItem06 = commodityService.create(appItem06);
        createCoversForCommodity(appItem06.getId(), 11);
        createSampleMessagesForCommodity(appItem06.getId());

        // insert the clothing Commodity
        Commodity clothingItem01 = new Commodity();
        clothingItem01.setTitle("千仞岗正品冬装新款羽绒服女2014潮加厚修身中长款大毛领冬衣1868");
        clothingItem01.setDescription("先到先得");
        clothingItem01.setPrice(584);
        clothingItem01.setCarriage(26);
        clothingItem01.setCollectionNumber(14);
        clothingItem01.setDistrict("江苏苏州");
        clothingItem01.setReleaseDate(new Date());
        clothingItem01.setUserId(sampleSeller.getId());
        List<Tag> clothingItem01Tags = new ArrayList<Tag>();
        clothingItem01Tags.add(categoryTags.get(2));
        clothingItem01Tags.add(categoryTags.get(2).getTags().get(0));
        clothingItem01.setTags(clothingItem01Tags);
        clothingItem01 = commodityService.create(clothingItem01);
        createCoversForCommodity(clothingItem01.getId(), 12);
        createSampleMessagesForCommodity(clothingItem01.getId());

        Commodity clothingItem02 = new Commodity();
        clothingItem02.setTitle("2014冬装新款羽绒服女卡伦艾莎中长款修身韩版加厚毛领女装外套潮");
        clothingItem02.setDescription("价格优惠还不抢");
        clothingItem02.setPrice(642);
        clothingItem02.setCarriage(13);
        clothingItem02.setCollectionNumber(3);
        clothingItem02.setDistrict("浙江杭州");
        clothingItem02.setReleaseDate(new Date());
        clothingItem02.setUserId(sampleSeller.getId());
        List<Tag> clothingItem02Tags = new ArrayList<Tag>();
        clothingItem02Tags.add(categoryTags.get(2));
        clothingItem02Tags.add(categoryTags.get(2).getTags().get(0));
        clothingItem02.setTags(clothingItem02Tags);
        clothingItem02 = commodityService.create(clothingItem02);
        createCoversForCommodity(clothingItem02.getId(), 13);
        createSampleMessagesForCommodity(clothingItem02.getId());

        Commodity clothingItem03 = new Commodity();
        clothingItem03.setTitle("苏醒的乐园 羽绒服女中长款加厚2015新款韩范潮奢华羽绒衣YRF859");
        clothingItem03.setDescription("90%白鸭绒呀亲");
        clothingItem03.setPrice(444);
        clothingItem03.setCarriage(44);
        clothingItem03.setCollectionNumber(44);
        clothingItem03.setDistrict("上海");
        clothingItem03.setReleaseDate(new Date());
        clothingItem03.setUserId(sampleSeller.getId());
        List<Tag> clothingItem03Tags = new ArrayList<Tag>();
        clothingItem03Tags.add(categoryTags.get(2));
        clothingItem03Tags.add(categoryTags.get(2).getTags().get(0));
        clothingItem03.setTags(clothingItem03Tags);
        clothingItem03 = commodityService.create(clothingItem03);
        createCoversForCommodity(clothingItem03.getId(), 14);
        createSampleMessagesForCommodity(clothingItem03.getId());

        Commodity clothingItem04 = new Commodity();
        clothingItem04.setTitle("tune tune春装新款2015淑女波点提花圆领荷叶边A字裙九分袖连衣裙");
        clothingItem04.setDescription("2015商场同款！");
        clothingItem04.setPrice(333);
        clothingItem04.setCarriage(33);
        clothingItem04.setCollectionNumber(13);
        clothingItem04.setDistrict("广东深圳");
        clothingItem04.setReleaseDate(new Date());
        clothingItem04.setUserId(sampleSeller.getId());
        List<Tag> clothingItem04Tags = new ArrayList<Tag>();
        clothingItem04Tags.add(categoryTags.get(2));
        clothingItem04Tags.add(categoryTags.get(2).getTags().get(0));
        clothingItem04.setTags(clothingItem04Tags);
        clothingItem04 = commodityService.create(clothingItem04);
        createCoversForCommodity(clothingItem04.getId(), 15);
        createSampleMessagesForCommodity(clothingItem04.getId());

        Commodity clothingItem05 = new Commodity();
        clothingItem05.setTitle("逸阳女裤2015新款春季女士黑色牛仔裤女小脚裤大码铅笔裤长裤0680");
        clothingItem05.setDescription("任性！");
        clothingItem05.setPrice(456);
        clothingItem05.setCarriage(21);
        clothingItem05.setCollectionNumber(3);
        clothingItem05.setDistrict("河南郑州");
        clothingItem05.setReleaseDate(new Date());
        clothingItem05.setUserId(sampleSeller.getId());
        List<Tag> clothingItem05Tags = new ArrayList<Tag>();
        clothingItem05Tags.add(categoryTags.get(2));
        clothingItem05Tags.add(categoryTags.get(2).getTags().get(1));
        clothingItem05.setTags(clothingItem05Tags);
        clothingItem05 = commodityService.create(clothingItem05);
        createCoversForCommodity(clothingItem05.getId(), 16);
        createSampleMessagesForCommodity(clothingItem05.getId());

        Commodity clothingItem06 = new Commodity();
        clothingItem06.setTitle("布依美2015春季新款长袖衬衫韩版时尚宽松简约中长款白色衬衫女春");
        clothingItem06.setDescription("美美哒");
        clothingItem06.setPrice(123);
        clothingItem06.setCarriage(10);
        clothingItem06.setCollectionNumber(6);
        clothingItem06.setDistrict("浙江杭州");
        clothingItem06.setReleaseDate(new Date());
        clothingItem06.setUserId(sampleSeller.getId());
        List<Tag> clothingItem06Tags = new ArrayList<Tag>();
        clothingItem06Tags.add(categoryTags.get(2));
        clothingItem06Tags.add(categoryTags.get(2).getTags().get(0));
        clothingItem06.setTags(clothingItem06Tags);
        clothingItem06 = commodityService.create(clothingItem06);
        createCoversForCommodity(clothingItem06.getId(), 17);
        createSampleMessagesForCommodity(clothingItem06.getId());

        // insert the shoes Commodity
        Commodity shoesItem01 = new Commodity();
        shoesItem01.setTitle("康莉comely羊皮磨砂水钻方扣优雅女鞋 圆头低跟浅口套脚职业单鞋");
        shoesItem01.setDescription("高雅的生活格调~");
        shoesItem01.setPrice(324);
        shoesItem01.setCarriage(43);
        shoesItem01.setCollectionNumber(9);
        shoesItem01.setDistrict("广东佛山");
        shoesItem01.setReleaseDate(new Date());
        shoesItem01.setUserId(sampleSeller.getId());
        List<Tag> shoesItem01Tags = new ArrayList<Tag>();
        shoesItem01Tags.add(categoryTags.get(3));
        shoesItem01Tags.add(categoryTags.get(3).getTags().get(0));
        shoesItem01.setTags(shoesItem01Tags);
        shoesItem01 = commodityService.create(shoesItem01);
        createCoversForCommodity(shoesItem01.getId(), 18);
        createSampleMessagesForCommodity(shoesItem01.getId());
        sampleCommodities.add(shoesItem01);

        Commodity shoesItem02 = new Commodity();
        shoesItem02.setTitle("康莉comely2014年秋季新款低跟水钻单鞋 时尚浅口金色休闲女鞋");
        shoesItem02.setDescription(" 优雅·时尚·特惠 ");
        shoesItem02.setPrice(215);
        shoesItem02.setCarriage(12);
        shoesItem02.setCollectionNumber(3);
        shoesItem02.setDistrict("广东佛山");
        shoesItem02.setReleaseDate(new Date());
        shoesItem02.setUserId(sampleSeller.getId());
        List<Tag> shoesItem02Tags = new ArrayList<Tag>();
        shoesItem02Tags.add(categoryTags.get(3));
        shoesItem02Tags.add(categoryTags.get(3).getTags().get(0));
        shoesItem02.setTags(shoesItem02Tags);
        shoesItem02 = commodityService.create(shoesItem02);
        createCoversForCommodity(shoesItem02.getId(), 19);
        createSampleMessagesForCommodity(shoesItem02.getId());

        Commodity shoesItem03 = new Commodity();
        shoesItem03.setTitle("exull依思q2015春季新款真皮拼色高跟内增高高帮鞋女鞋子15155144");
        shoesItem03.setDescription("1.魔术贴片，便捷穿脱；2.率性内增，从容洒脱");
        shoesItem03.setPrice(289);
        shoesItem03.setCarriage(67);
        shoesItem03.setCollectionNumber(8);
        shoesItem03.setDistrict("福建泉州");
        shoesItem03.setReleaseDate(new Date());
        shoesItem03.setUserId(sampleSeller.getId());
        List<Tag> shoesItem03Tags = new ArrayList<Tag>();
        shoesItem03Tags.add(categoryTags.get(3));
        shoesItem03Tags.add(categoryTags.get(3).getTags().get(0));
        shoesItem03.setTags(shoesItem03Tags);
        shoesItem03 = commodityService.create(shoesItem03);
        createCoversForCommodity(shoesItem03.getId(), 20);
        createSampleMessagesForCommodity(shoesItem03.getId());

        Commodity shoesItem04 = new Commodity();
        shoesItem04.setTitle("低调女人2014秋季纯色休闲鞋女板鞋韩版潮流透气运动女鞋内增高鞋");
        shoesItem04.setDescription("采用优质帆布 更加透气 甜美拼色 更显粉嫩青春气息");
        shoesItem04.setPrice(120);
        shoesItem04.setCarriage(10);
        shoesItem04.setCollectionNumber(7);
        shoesItem04.setDistrict("浙江温州");
        shoesItem04.setReleaseDate(new Date());
        shoesItem04.setUserId(sampleSeller.getId());
        List<Tag> shoesItem04Tags = new ArrayList<Tag>();
        shoesItem04Tags.add(categoryTags.get(3));
        shoesItem04Tags.add(categoryTags.get(3).getTags().get(0));
        shoesItem04.setTags(shoesItem04Tags);
        shoesItem04 = commodityService.create(shoesItem04);
        createCoversForCommodity(shoesItem04.getId(), 21);
        createSampleMessagesForCommodity(shoesItem04.getId());

        Commodity shoesItem05 = new Commodity();
        shoesItem05.setTitle("2015春季新款honeyGIRL甜美女鞋蝴蝶结高跟鞋女细跟尖头浅口单鞋");
        shoesItem05.setDescription("快点下手吧");
        shoesItem05.setPrice(169);
        shoesItem05.setCarriage(3);
        shoesItem05.setCollectionNumber(1);
        shoesItem05.setDistrict("浙江温州");
        shoesItem05.setReleaseDate(new Date());
        shoesItem05.setUserId(sampleSeller.getId());
        List<Tag> shoesItem05Tags = new ArrayList<Tag>();
        shoesItem05Tags.add(categoryTags.get(3));
        shoesItem05Tags.add(categoryTags.get(3).getTags().get(0));
        shoesItem05.setTags(shoesItem05Tags);
        shoesItem05 = commodityService.create(shoesItem05);
        createCoversForCommodity(shoesItem05.getId(), 22);
        createSampleMessagesForCommodity(shoesItem05.getId());
        sampleCommodities.add(shoesItem05);

        Commodity shoesItem06 = new Commodity();
        shoesItem06.setTitle("2015春秋新款潮女鞋手工鞋子真皮休闲平底皮鞋英伦大头鞋平跟单鞋");
        shoesItem06.setDescription("假一赔十，亲们放心选购");
        shoesItem06.setPrice(218);
        shoesItem06.setCarriage(20);
        shoesItem06.setCollectionNumber(23);
        shoesItem06.setDistrict("浙江温州");
        shoesItem06.setReleaseDate(new Date());
        shoesItem06.setUserId(sampleSeller.getId());
        List<Tag> shoesItem06Tags = new ArrayList<Tag>();
        shoesItem06Tags.add(categoryTags.get(3));
        shoesItem06Tags.add(categoryTags.get(3).getTags().get(0));
        shoesItem06.setTags(shoesItem06Tags);
        shoesItem06 = commodityService.create(shoesItem06);
        createCoversForCommodity(shoesItem06.getId(), 23);
        createSampleMessagesForCommodity(shoesItem06.getId());

        // insert the bag Commodity
        Commodity bagItem01 = new Commodity();
        bagItem01.setTitle("朱尔波士顿牛皮女包2015新款时尚皮包手提包包蛇纹单肩袋女士包女");
        bagItem01.setDescription("不买就后悔，物美价廉");
        bagItem01.setPrice(329.9);
        bagItem01.setCarriage(36);
        bagItem01.setCollectionNumber(11);
        bagItem01.setDistrict("广东广州");
        bagItem01.setReleaseDate(new Date());
        List<Tag> bagItem01Tags = new ArrayList<Tag>();
        bagItem01Tags.add(categoryTags.get(4));
        bagItem01Tags.add(categoryTags.get(4).getTags().get(0));
        bagItem01.setTags(bagItem01Tags);
        bagItem01.setUserId(sampleSeller.getId());
        bagItem01 = commodityService.create(bagItem01);
        createCoversForCommodity(bagItem01.getId(), 24);
        sampleCommodities.add(bagItem01);

        Commodity bagItem02 = new Commodity();
        bagItem02.setTitle("朱尔原创新款包包手提包 女士时尚牛皮女包 波士顿枕头包单肩斜跨");
        bagItem02.setDescription("朱尔原创新款包包手提包 女士时尚牛皮女包 波士顿枕头包单肩斜跨");
        bagItem02.setPrice(376.9);
        bagItem02.setCarriage(21);
        bagItem02.setCollectionNumber(8);
        bagItem02.setDistrict("广东广州");
        bagItem02.setReleaseDate(new Date());
        bagItem02.setUserId(sampleSeller.getId());
        List<Tag> bagItem02Tags = new ArrayList<Tag>();
        bagItem02Tags.add(categoryTags.get(4));
        bagItem02Tags.add(categoryTags.get(4).getTags().get(0));
        bagItem02.setTags(bagItem02Tags);
        bagItem02 = commodityService.create(bagItem02);
        createCoversForCommodity(bagItem02.getId(), 25);
        createSampleMessagesForCommodity(bagItem02.getId());

        Commodity bagItem03 = new Commodity();
        bagItem03.setTitle("朱尔鳄鱼纹真皮女包大包 时尚头层牛皮女士包包 新款女单肩手提包");
        bagItem03.setDescription("气质款包包！");
        bagItem03.setPrice(472.5);
        bagItem03.setCarriage(100);
        bagItem03.setCollectionNumber(78);
        bagItem03.setDistrict("广东广州");
        bagItem03.setReleaseDate(new Date());
        bagItem03.setUserId(sampleSeller.getId());
        List<Tag> bagItem03Tags = new ArrayList<Tag>();
        bagItem03Tags.add(categoryTags.get(4));
        bagItem03Tags.add(categoryTags.get(4).getTags().get(0));
        bagItem03.setTags(bagItem03Tags);
        bagItem03 = commodityService.create(bagItem03);
        createCoversForCommodity(bagItem03.getId(), 26);
        createSampleMessagesForCommodity(bagItem03.getId());

        Commodity bagItem04 = new Commodity();
        bagItem04.setTitle("袋黛安正品万向轮行李箱20/24/26拉杆箱包旅行箱28寸托运箱子男女");
        bagItem04.setDescription("品质保证！");
        bagItem04.setPrice(367.9);
        bagItem04.setCarriage(342);
        bagItem04.setCollectionNumber(45);
        bagItem04.setDistrict("上海");
        bagItem04.setReleaseDate(new Date());
        bagItem04.setUserId(sampleSeller.getId());
        List<Tag> bagItem04Tags = new ArrayList<Tag>();
        bagItem04Tags.add(categoryTags.get(4));
        bagItem04Tags.add(categoryTags.get(4).getTags().get(2));
        bagItem04.setTags(bagItem04Tags);
        bagItem04 = commodityService.create(bagItem04);
        createCoversForCommodity(bagItem04.getId(), 27);
        createSampleMessagesForCommodity(bagItem04.getId());
        sampleCommodities.add(bagItem04);

        Commodity bagItem05 = new Commodity();
        bagItem05.setTitle("袋黛安万向轮PC拉杆箱20寸旅行箱密码箱29寸行李箱包男女托运箱子");
        bagItem05.setDescription("袋黛安最新款高级旅行箱.只为少数人而生！ TSA密码锁.深框铝框！");
        bagItem05.setPrice(427.5);
        bagItem05.setCarriage(271);
        bagItem05.setCollectionNumber(20);
        bagItem05.setDistrict("上海");
        bagItem05.setReleaseDate(new Date());
        bagItem05.setUserId(sampleSeller.getId());
        List<Tag> bagItem05Tags = new ArrayList<Tag>();
        bagItem05Tags.add(categoryTags.get(4));
        bagItem05Tags.add(categoryTags.get(4).getTags().get(2));
        bagItem05.setTags(bagItem05Tags);
        bagItem05 = commodityService.create(bagItem05);
        createCoversForCommodity(bagItem05.getId(), 28);
        createSampleMessagesForCommodity(bagItem05.getId());

        Commodity bagItem06 = new Commodity();
        bagItem06.setTitle("F瑞士军刀威戈wenger双肩背包男女电脑背包商务背包旅行学生背包");
        bagItem06.setDescription("F瑞士军刀威戈wenger双肩背包男女电脑背包商务背包旅行学生背包");
        bagItem06.setPrice(217.5);
        bagItem06.setCarriage(129);
        bagItem06.setCollectionNumber(10);
        bagItem06.setDistrict("上海");
        bagItem06.setReleaseDate(new Date());
        bagItem06.setUserId(sampleSeller.getId());
        List<Tag> bagItem06Tags = new ArrayList<Tag>();
        bagItem06Tags.add(categoryTags.get(4));
        bagItem06Tags.add(categoryTags.get(4).getTags().get(1));
        bagItem06.setTags(bagItem06Tags);
        bagItem06 = commodityService.create(bagItem06);
        createCoversForCommodity(bagItem06.getId(), 29);
        createSampleMessagesForCommodity(bagItem06.getId());

        // insert the book Commodity
        Commodity bookItem01 = new Commodity();
        bookItem01.setTitle(" 狼图腾 正版现货 小说 姜戎 研究狼的旷世奇书 现代文学 书籍");
        bookItem01.setDescription("高中生最爱读的");
        bookItem01.setPrice(18.9);
        bookItem01.setCarriage(58);
        bookItem01.setCollectionNumber(6);
        bookItem01.setDistrict("北京");
        bookItem01.setReleaseDate(new Date());
        bookItem01.setUserId(sampleSeller.getId());
        List<Tag> bookItem01Tags = new ArrayList<Tag>();
        bookItem01Tags.add(categoryTags.get(5));
        bookItem01Tags.add(categoryTags.get(5).getTags().get(0));
        bookItem01.setTags(bookItem01Tags);
        bookItem01 = commodityService.create(bookItem01);
        createCoversForCommodity(bookItem01.getId(), 30);
        createSampleMessagesForCommodity(bookItem01.getId());
        sampleCommodities.add(bookItem01);

        Commodity bookItem02 = new Commodity();
        bookItem02.setTitle("正版 追风筝的人 胡塞尼 小说 首部中文畅销书籍 两千万读者口耳相传");
        bookItem02.setDescription("从【当当网】买的");
        bookItem02.setPrice(14.9);
        bookItem02.setCarriage(39);
        bookItem02.setCollectionNumber(8);
        bookItem02.setDistrict("北京");
        bookItem02.setReleaseDate(new Date());
        bookItem02.setUserId(sampleSeller.getId());
        List<Tag> bookItem02Tags = new ArrayList<Tag>();
        bookItem02Tags.add(categoryTags.get(5));
        bookItem02Tags.add(categoryTags.get(5).getTags().get(0));
        bookItem02.setTags(bookItem02Tags);
        bookItem02 = commodityService.create(bookItem02);
        createCoversForCommodity(bookItem02.getId(), 31);
        createSampleMessagesForCommodity(bookItem02.getId());

        Commodity bookItem03 = new Commodity();
        bookItem03.setTitle("一本可以帮你省钱的，讲解治疗小病妙方的书籍！！");
        bookItem03.setDescription("王春全 主编！");
        bookItem03.setPrice(23.7);
        bookItem03.setCarriage(15);
        bookItem03.setCollectionNumber(5);
        bookItem03.setDistrict("北京");
        bookItem03.setUserId(sampleSeller.getId());
        bookItem03.setReleaseDate(new Date());
        List<Tag> bookItem03Tags = new ArrayList<Tag>();
        bookItem03Tags.add(categoryTags.get(5));
        bookItem03Tags.add(categoryTags.get(5).getTags().get(0));
        bookItem03.setTags(bookItem03Tags);
        bookItem03 = commodityService.create(bookItem03);
        createCoversForCommodity(bookItem03.getId(), 32);
        createSampleMessagesForCommodity(bookItem03.getId());

        Commodity bookItem04 = new Commodity();
        bookItem04.setTitle("星火英语四级真题/CET4级 大学英语四级 (10套真题详解+标准预测4级)");
        bookItem04.setDescription("史销最受欢迎的");
        bookItem04.setPrice(12.7);
        bookItem04.setCarriage(4);
        bookItem04.setCollectionNumber(1);
        bookItem04.setDistrict("江苏南京");
        bookItem04.setReleaseDate(new Date());
        bookItem04.setUserId(sampleSeller.getId());
        List<Tag> bookItem04Tags = new ArrayList<Tag>();
        bookItem04Tags.add(categoryTags.get(5));
        bookItem04Tags.add(categoryTags.get(5).getTags().get(0));
        bookItem04.setTags(bookItem04Tags);
        bookItem04 = commodityService.create(bookItem04);
        createCoversForCommodity(bookItem04.getId(), 33);
        createSampleMessagesForCommodity(bookItem04.getId());

        Commodity bookItem05 = new Commodity();
        bookItem05.setTitle("新东方 大学英语四级词汇书 新东方 大学英语四级词汇书新东方 大学英语四级词汇书");
        bookItem05.setDescription("品质保证，先到先得哦~（");
        bookItem05.setPrice(17.7);
        bookItem05.setCarriage(3);
        bookItem05.setCollectionNumber(7);
        bookItem05.setDistrict("江苏南京");
        bookItem05.setReleaseDate(new Date());
        List<Tag> bookItem05Tags = new ArrayList<Tag>();
        bookItem05Tags.add(categoryTags.get(5));
        bookItem05Tags.add(categoryTags.get(5).getTags().get(0));
        bookItem05.setTags(bookItem05Tags);
        bookItem05.setUserId(sampleSeller.getId());
        bookItem05 = commodityService.create(bookItem05);
        createCoversForCommodity(bookItem05.getId(), 34);
        createSampleMessagesForCommodity(bookItem05.getId());
        sampleCommodities.add(bookItem05);

        Commodity bookItem06 = new Commodity();
        bookItem06.setTitle("星火 张道真 高中英语语法 星火 张道真 高中英语语法星火 张道真 高中英语语法 星火");
        bookItem06.setDescription("本书适用于2015年06月考试");
        bookItem06.setPrice(6.7);
        bookItem06.setCarriage(2);
        bookItem06.setCollectionNumber(2);
        bookItem06.setDistrict("上海");
        bookItem06.setReleaseDate(new Date());
        bookItem06.setUserId(sampleSeller.getId());
        List<Tag> bookItem06Tags = new ArrayList<Tag>();
        bookItem06Tags.add(categoryTags.get(5));
        bookItem06Tags.add(categoryTags.get(5).getTags().get(0));
        bookItem06.setTags(bookItem06Tags);
        bookItem06 = commodityService.create(bookItem06);
        createCoversForCommodity(bookItem06.getId(), 35);
        createSampleMessagesForCommodity(bookItem06.getId());
    }

    private void createSampleImages() {
        String path = ConfigManagmentUtil.getConfigProperties(Constants.IMAGE_STORAGE_BASE_PATH);
        Image image1 = new Image();
        image1.setUri(path + "/cholate01.jpeg");
        image1.setTitle("chocolate01-Title");
        image1.setDescription("chocolate01-Description");
        image1.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image1));

        Image image2 = new Image();
        image2.setUri(path + "/cholate02.jpg");
        image2.setTitle("chocolate02-Title");
        image2.setDescription("chocolate02-Title");
        image2.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image2));

        Image image3 = new Image();
        image3.setUri(path + "/cholate03.jpg");
        image3.setTitle("chocolate03-Title");
        image3.setDescription("chocolate03-Title");
        image3.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image3));

        Image image4 = new Image();
        image4.setUri(path + "/zijuan01.jpg");
        image4.setTitle("Title");
        image4.setDescription("Description");
        image4.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image4));

        Image image5 = new Image();
        image5.setUri(path + "/binggan.jpg");
        image5.setTitle("Title");
        image5.setDescription("Title");
        image5.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image5));

        Image image6 = new Image();
        image6.setUri(path + "/leshi.jpg");
        image6.setTitle("Title");
        image6.setDescription("Title");
        image6.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image6));

        Image image7 = new Image();
        image7.setUri(path + "/htc.jpg");
        image7.setTitle("Title");
        image7.setDescription("Title");
        image7.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image7));

        Image image8 = new Image();
        image8.setUri(path + "/mi.jpg");
        image8.setTitle("Title");
        image8.setDescription("Title");
        image8.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image8));

        Image image9 = new Image();
        image9.setUri(path + "/hongmi.jpg");
        image9.setTitle("Title");
        image9.setDescription("Title");
        image9.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image9));

        Image image10 = new Image();
        image10.setUri(path + "/ears.jpg");
        image10.setTitle("Title");
        image10.setDescription("Title");
        image10.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image10));
        
        Image image11 = new Image();
        image11.setUri(path + "/tv.jpg");
        image11.setTitle("Title");
        image11.setDescription("Title");
        image11.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image11));
        
        Image image12= new Image();
        image12.setUri(path + "/harer.jpg");
        image12.setTitle("Title");
        image12.setDescription("Title");
        image12.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image12));

        Image image13= new Image();
        image13.setUri(path + "/qian.jpeg");
        image13.setTitle("Title");
        image13.setDescription("Title");
        image13.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image13));

        Image image14= new Image();
        image14.setUri(path + "/kalun.jpg");
        image14.setTitle("Title");
        image14.setDescription("Title");
        image14.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image14));

        Image image15= new Image();
        image15.setUri(path + "/suxing.jpg");
        image15.setTitle("Title");
        image15.setDescription("Title");
        image15.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image15));

        Image image16= new Image();
        image16.setUri(path + "/tune.jpeg");
        image16.setTitle("Title");
        image16.setDescription("Title");
        image16.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image16));

        Image image17= new Image();
        image17.setUri(path + "/yiyang.jpeg");
        image17.setTitle("Title");
        image17.setDescription("Title");
        image17.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image17));

        Image image18= new Image();
        image18.setUri(path + "/buyimei.jpg");
        image18.setTitle("Title");
        image18.setDescription("Title");
        image18.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image18));
        
        Image image19= new Image();
        image19.setUri(path + "/kangli.jpg");
        image19.setTitle("Title");
        image19.setDescription("Title");
        image19.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image19));
        
        Image image20 = new Image();
        image20.setUri(path + "/kangli02.jpg");
        image20.setTitle("Title");
        image20.setDescription("Title");
        image20.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image20));
        
        Image image21= new Image();
        image21.setUri(path + "/yisi.jpg");
        image21.setTitle("Title");
        image21.setDescription("Title");
        image21.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image21));

        Image image22= new Image();
        image22.setUri(path + "/nei.jpg");
        image22.setTitle("Title");
        image22.setDescription("Title");
        image22.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image22));

        Image image23= new Image();
        image23.setUri(path + "/hudie.jpeg");
        image23.setTitle("Title");
        image23.setDescription("Title");
        image23.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image23));

        Image image24= new Image();
        image24.setUri(path + "/yinglun.jpg");
        image24.setTitle("Title");
        image24.setDescription("Title");
        image24.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image24));

        Image image25= new Image();
        image25.setUri(path + "/zhuer.jpg");
        image25.setTitle("Title");
        image25.setDescription("Title");
        image25.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image25));

        Image image26= new Image();
        image26.setUri(path + "/zhuer2.jpg");
        image26.setTitle("Title");
        image26.setDescription("Title");
        image26.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image26));

        Image image27= new Image();
        image27.setUri(path + "/zhuer03.jpg");
        image27.setTitle("Title");
        image27.setDescription("Title");
        image27.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image27));

        Image image28= new Image();
        image28.setUri(path + "/dai01.jpg");
        image28.setTitle("Title");
        image28.setDescription("Title");
        image28.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image28));

        Image image29= new Image();
        image29.setUri(path + "/lun.jpg");
        image29.setTitle("Title");
        image29.setDescription("Title");
        image29.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image29));

        Image image30= new Image();
        image30.setUri(path + "/shu.jpg");
        image30.setTitle("Title");
        image30.setDescription("Title");
        image30.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image30));

        Image image31= new Image();
        image31.setUri(path + "/lang.jpg");
        image31.setTitle("Title");
        image31.setDescription("Title");
        image31.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image31));

        Image image32= new Image();
        image32.setUri(path + "/zhui.jpg");
        image32.setTitle("Title");
        image32.setDescription("Title");
        image32.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image32));
        
        Image image33= new Image();
        image33.setUri(path + "/jian.jpg");
        image33.setTitle("Title");
        image33.setDescription("Title");
        image33.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image33));
        
        Image image34= new Image();
        image34.setUri(path + "/xing01.jpg");
        image34.setTitle("Title");
        image34.setDescription("Title");
        image34.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image34));
        
        Image image35= new Image();
        image35.setUri(path + "/xing02.jpg");
        image35.setTitle("Title");
        image35.setDescription("Title");
        image35.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image35));

        Image image36= new Image();
        image36.setUri(path + "/xing03.jpg");
        image36.setTitle("Title");
        image36.setDescription("Title");
        image36.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image36));
    }

    private void createCoversForCommodity(String commodityId, int index) {
        Cover cover1 = new Cover();
        cover1.setCommodityId(commodityId);
        cover1.setImageId(sampleImageList.get(index).getId());
        cover1.setMainCoverYN("Y");
        cover1.setDisplaySequence(0);
        coverService.create(cover1);

/*        Cover cover2 = new Cover();
        cover2.setCommodityId(commodityId);
        cover2.setImageId(sampleImageList.get(1).getId());
        cover2.setDisplaySequence(1);
        coverService.create(cover2);

        Cover cover3 = new Cover();
        cover3.setCommodityId(commodityId);
        cover3.setImageId(sampleImageList.get(2).getId());
        cover3.setDisplaySequence(2);
        coverService.create(cover3);*/
    }

    private void createSampleMessagesForCommodity(String commodityId) {
        Message message1 = new Message();
        message1.setCommodityId(commodityId);
        message1.setContent("老板，可以给个电话号私聊吗？");
        message1.setFloor(1);
        message1.setUserId(standardUser.getId());
        message1.setUserName(standardUser.getUserName());
        message1 = messageService.create(message1);

        Message message1r = new Message();
        message1r.setCommodityId(commodityId);
        message1r.setContent("你说那？呵呵");
        message1r.setFloor(1);
        message1r.setUserId(standardUser.getId());
        message1r.setUserName(standardUser.getUserName());
        message1r.setParentId(message1.getId());
        messageService.create(message1r);

        Message message2 = new Message();
        message2.setCommodityId(commodityId);
        message2.setContent("同楼上");
        message2.setFloor(1);
        message2.setUserId(standardUser.getId());
        message2.setUserName(standardUser.getUserName());
        messageService.create(message2);
    }

    private void createSampleSlices() {
        Slice slice1 = new Slice();
        slice1.setImageId(sampleImageList.get(0).getId());
        slice1.setCommodityId(sampleCommodities.get(0).getId());
        slice1.setDisplaySequence(0);
        slice1.setDescription("test slice 1");
        sliceService.create(slice1);

        Slice slice2 = new Slice();
        slice2.setImageId(sampleImageList.get(6).getId());
        slice2.setCommodityId(sampleCommodities.get(1).getId());
        slice2.setDisplaySequence(1);
        slice2.setDescription("test slice 2");
        sliceService.create(slice2);

        Slice slice3 = new Slice();
        slice3.setImageId(sampleImageList.get(10).getId());
        slice3.setCommodityId(sampleCommodities.get(2).getId());
        slice3.setDisplaySequence(2);
        slice3.setDescription("test slice 3");
        sliceService.create(slice3);
    }

    private void createSampleHots() {
        Hot hot1 = new Hot();
        hot1.setCommodityId(sampleCommodities.get(3).getId());
        hot1.setImageId(sampleImageList.get(18).getId());
        hot1.setDisplaySequence(0);
        hotService.create(hot1);

        Hot hot2 = new Hot();
        hot2.setCommodityId(sampleCommodities.get(4).getId());
        hot2.setImageId(sampleImageList.get(22).getId());
        hot2.setDisplaySequence(1);
        hotService.create(hot2);

        Hot hot3 = new Hot();
        hot3.setCommodityId(sampleCommodities.get(5).getId());
        hot3.setImageId(sampleImageList.get(24).getId());
        hot3.setDisplaySequence(2);
        hotService.create(hot3);

        Hot hot4 = new Hot();
        hot4.setCommodityId(sampleCommodities.get(6).getId());
        hot4.setImageId(sampleImageList.get(27).getId());
        hot4.setDisplaySequence(3);
        hotService.create(hot4);

        Hot hot5 = new Hot();
        hot5.setCommodityId(sampleCommodities.get(7).getId());
        hot5.setImageId(sampleImageList.get(30).getId());
        hot5.setDisplaySequence(4);
        hotService.create(hot5);
    }

    private void updateSampleUserDetail() {
        sampleBuyer.setAge(20);
        sampleBuyer.setRealName("张三");
        sampleBuyer = userService.update(sampleBuyer);
    }

    private void createSampleAddress() {
        Address address1 = new Address();
        address1.setAddressCountry("中国");
        address1.setAddressProvince("江苏");
        address1.setAddressCity("无锡");
        address1.setAddressDetail("锡山区张三收");
        address1.setPhoneNumber("13287905894");
        address1.setPostcode("325104");
        address1.setUserId(sampleBuyer.getId());
        addressService.create(address1);

        Address address2 = new Address();
        address2.setAddressCountry("中国");
        address2.setAddressProvince("江苏");
        address2.setAddressCity("南通");
        address2.setAddressDetail("崇川区张三收");
        address2.setPhoneNumber("13290865378");
        address2.setPostcode("525104");
        address2.setUserId(sampleBuyer.getId());
        addressService.create(address2);

        Address address3 = new Address();
        address3.setAddressCountry("中国");
        address3.setAddressProvince("上海");
        address3.setAddressCity("上海");
        address3.setAddressDetail("浦东新区张三收");
        address3.setPhoneNumber("15902897846");
        address3.setPostcode("421104");
        address3.setUserId(sampleBuyer.getId());
        addressService.create(address3);
    }
}
