package com.ntu.igts.dbinit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ntu.igts.constants.Constants;
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

    public void createStandardData() {
        LOGGER.info("Start to init standard data");
        createRole();
        createUser();
        createAdmin();
        LOGGER.info("Init standard data finished");
    }

    public void createSampleData() {
        LOGGER.info("Start to init sample data");
        createSampleImages();
        createSampleTags();
        createSampleCommodities();
        createSampleSlices();
        createSampleHots();
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
        List<Tag> subTags = new ArrayList<Tag>();
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
        subTags.add(tagService.create(chocolateTag));

        Tag cakeTag = new Tag();
        cakeTag.setName("蛋糕");
        cakeTag.setStandardName("CAKE");
        cakeTag.setParentId(insertedFoodTag.getId());
        subTags.add(tagService.create(cakeTag));

        insertedFoodTag.setTags(subTags);
        categoryTags.add(insertedFoodTag);

        subTags.clear();
        // Generate application Tag with sub-Tags
        Tag applicationTag = new Tag();
        applicationTag.setName("家电");
        applicationTag.setStandardName("APPLICATION");
        applicationTag.setParentId(null);
        Tag insertedApplicationTag = tagService.create(applicationTag);

        Tag tvTag = new Tag();
        tvTag.setName("电视机");
        tvTag.setStandardName("TV");
        tvTag.setParentId(insertedApplicationTag.getId());
        subTags.add(tagService.create(tvTag));

        insertedApplicationTag.setTags(subTags);
        categoryTags.add(insertedApplicationTag);

        subTags.clear();
        // Generate clothing Tag with sub-Tags
        Tag clothingTag = new Tag();
        clothingTag.setName("服装");
        clothingTag.setStandardName("CLOTHING");
        clothingTag.setParentId(null);
        Tag insertedClothingTag = tagService.create(clothingTag);

        Tag shirtTag = new Tag();
        shirtTag.setName("衬衫");
        shirtTag.setStandardName("HIRT");
        shirtTag.setParentId(insertedClothingTag.getId());
        subTags.add(tagService.create(shirtTag));

        insertedClothingTag.setTags(subTags);
        categoryTags.add(insertedClothingTag);

        subTags.clear();
        // Generate shoe Tag with sub-Tags
        Tag shoesTag = new Tag();
        shoesTag.setName("鞋子");
        shoesTag.setStandardName("SHOE");
        shoesTag.setParentId(null);
        Tag insertedShoesTag = tagService.create(shoesTag);

        Tag slippersTag = new Tag();
        slippersTag.setName("拖鞋");
        slippersTag.setStandardName("SLIPPER");
        slippersTag.setParentId(insertedShoesTag.getId());
        subTags.add(tagService.create(slippersTag));

        insertedShoesTag.setTags(subTags);
        categoryTags.add(insertedShoesTag);

        subTags.clear();
        // Generate bag Tag with sub-Tags
        Tag bagTag = new Tag();
        bagTag.setName("箱包");
        bagTag.setStandardName("BAG");
        bagTag.setParentId(null);
        Tag insertedBagTag = tagService.create(bagTag);

        Tag lvTag = new Tag();
        lvTag.setName("LV");
        lvTag.setStandardName("LV");
        lvTag.setParentId(insertedBagTag.getId());
        subTags.add(tagService.create(lvTag));

        insertedBagTag.setTags(subTags);
        categoryTags.add(insertedBagTag);

        subTags.clear();
        // Generate book Tag with sub-Tags
        Tag bookTag = new Tag();
        bookTag.setName("图书影像");
        bookTag.setStandardName("BOOK");
        bookTag.setParentId(null);
        Tag insertedBookTag = tagService.create(bookTag);

        Tag storyTag = new Tag();
        storyTag.setName("故事书");
        storyTag.setStandardName("STORY");
        storyTag.setParentId(insertedBookTag.getId());
        subTags.add(tagService.create(storyTag));

        insertedBookTag.setTags(subTags);
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
        foodItem01.setDistrict("浙江衢州");
        List<Tag> foodItem01Tags = new ArrayList<Tag>();
        foodItem01Tags.add(categoryTags.get(0));
        foodItem01.setTags(foodItem01Tags);
        foodItem01 = commodityService.create(foodItem01);
        createCoversForCommodity(foodItem01.getId());
        createSampleMessagesForCommodity(foodItem01.getId());
        sampleCommodities.add(foodItem01);

        Commodity foodItem02 = new Commodity();
        foodItem02.setTitle("韩国进口零食品乐天红加纳牛奶巧克力块90g 情人节喜糖年货糖果");
        foodItem02.setDescription("丝丝香浓，入口即化！纯可可脂巧克力，口感秒杀国内代可可脂巧克力!好吃不胖的巧克力 可做年货、喜糖，让喜事更加喜庆~");
        foodItem02.setPrice(432);
        foodItem02.setCarriage(20);
        foodItem02.setCollectionNumber(5);
        foodItem02.setDistrict("上海");
        List<Tag> foodItem02Tags = new ArrayList<Tag>();
        foodItem02Tags.add(categoryTags.get(0));
        foodItem02.setTags(foodItem02Tags);
        foodItem02 = commodityService.create(foodItem02);
        createCoversForCommodity(foodItem02.getId());
        createSampleMessagesForCommodity(foodItem02.getId());
        sampleCommodities.add(foodItem02);

        Commodity foodItem03 = new Commodity();
        foodItem03.setTitle("意大利费列罗巧克力威化果仁金莎巧克力礼盒装费雷罗进口零食24粒");
        foodItem03.setDescription("【顺风包邮】【拍下99元】【买就送手提袋】【可代写贺卡】。【费列罗官方正品发售】喜迎情人节，疯狂放价，原价288，现价99元。只有五天的时间！结束马上涨！【费列罗巧克力，让您的每一个重要场合都变成金色时刻】高端大气上档次，送老婆，送情人，送朋友必备礼盒！代购神马的都是浮云，省下代购钱，自己买正品！");
        foodItem03.setPrice(67.9);
        foodItem03.setCarriage(0);
        foodItem03.setCollectionNumber(99);
        foodItem03.setDistrict("河北廊坊");
        List<Tag> foodItem03Tags = new ArrayList<Tag>();
        foodItem03Tags.add(categoryTags.get(0));
        foodItem03.setTags(foodItem03Tags);
        foodItem03 = commodityService.create(foodItem03);
        createCoversForCommodity(foodItem03.getId());
        createSampleMessagesForCommodity(foodItem03.getId());
        sampleCommodities.add(foodItem03);

        Commodity foodItem04 = new Commodity();
        foodItem04.setTitle("泰国进口小老板即食海苔烤紫菜卷原味辣味套餐36g袋装9gX6条");
        foodItem04.setDescription("精选泰国进口海藻，新鲜零污染原料，不添加防腐剂，不添加人工色素，烤出来的海苔卷，香脆无比！开袋即食！");
        foodItem04.setPrice(22.9);
        foodItem04.setCarriage(0);
        foodItem04.setCollectionNumber(10);
        foodItem04.setDistrict("广东广州");
        List<Tag> foodItem04Tags = new ArrayList<Tag>();
        foodItem04Tags.add(categoryTags.get(0));
        foodItem04.setTags(foodItem04Tags);
        foodItem04 = commodityService.create(foodItem04);
        createCoversForCommodity(foodItem04.getId());
        createSampleMessagesForCommodity(foodItem04.getId());
        sampleCommodities.add(foodItem04);

        Commodity foodItem05 = new Commodity();
        foodItem05.setTitle("意大利原装进口 可意奇 奶油圈饼干300g 营养健康 早餐必备");
        foodItem05.setDescription("自然发酵，烘培适中、香气浓郁、营养价值高、富含纤维利于消化，低热量，有益健康，方便易取。");
        foodItem05.setPrice(5.6);
        foodItem05.setCarriage(1);
        foodItem05.setCollectionNumber(0);
        foodItem05.setDistrict("天津");
        List<Tag> foodItem05Tags = new ArrayList<Tag>();
        foodItem05Tags.add(categoryTags.get(0));
        foodItem05.setTags(foodItem05Tags);
        foodItem05 = commodityService.create(foodItem05);
        createCoversForCommodity(foodItem05.getId());
        createSampleMessagesForCommodity(foodItem05.getId());

        Commodity foodItem06 = new Commodity();
        foodItem06.setTitle("进口乐事薯片 墨西哥原装 Lay's原味/咸酸味薯片163g罐装进口薯片");
        foodItem06.setDescription("墨西哥原装进口罐装乐事原味薯片 办公室零食小吃 美味休闲食品 享受乐事好滋味～有原味和咸酸味可以选择。另注：现反海关正规渠道进口的乐事薯片只有原味和咸酸味符合国家检疫标准，其他口味均属于非正规渠道。亲慎选。我店的宝贝都是正规渠道进口，谢谢。");
        foodItem06.setPrice(16.8);
        foodItem06.setCarriage(3);
        foodItem06.setCollectionNumber(2);
        foodItem06.setDistrict("上海");
        List<Tag> foodItem06Tags = new ArrayList<Tag>();
        foodItem06Tags.add(categoryTags.get(0));
        foodItem06.setTags(foodItem06Tags);
        foodItem06 = commodityService.create(foodItem06);
        createCoversForCommodity(foodItem06.getId());
        createSampleMessagesForCommodity(foodItem06.getId());

        // insert the application Commodity
        Commodity appItem01 = new Commodity();
        appItem01.setTitle("[预售 年前发货]HTC D826w Desire 双4G 8核安卓智能手机");
        appItem01.setDescription("超强前置 甩掉单反 合二为一！节日自拍、合拍、送礼首选。前1300万或Ultra Pixel高感光+后1300万超大广角后摄像头；双撞色时尚外观+亲肤美学防滑侧边；双前扬声器+杜比环绕; 高通8核64位极速芯片+全新安卓5.0系统+1080p 5.5 全高清屏 官方正品 工厂直发 顺丰包邮 全网最快");
        appItem01.setPrice(2288);
        appItem01.setCarriage(10);
        appItem01.setCollectionNumber(7);
        appItem01.setDistrict("上海");
        List<Tag> appItem01Tags = new ArrayList<Tag>();
        appItem01Tags.add(categoryTags.get(1));
        appItem01.setTags(appItem01Tags);
        appItem01 = commodityService.create(appItem01);
        createCoversForCommodity(appItem01.getId());
        createSampleMessagesForCommodity(appItem01.getId());

        Commodity appItem02 = new Commodity();
        appItem02.setTitle("小米官方旗舰店MIUI/小米 小米手机4 小米4代 MI4智能4G手机包邮");
        appItem02.setDescription("工艺和手感，超乎想象。不锈钢金属边框，5英寸屏超窄边。 【移动4G版请选择：TD-LTE（4G）/TD-SCDMA/GSM】 【联通4G版请选择：FDD-LTE/TD-LTE/WCDMA/GSM】 【电信4G版请选择：TD-LTE/FDD-LTE/CDMA2000/GSM】");
        appItem02.setPrice(1999);
        appItem02.setCarriage(30);
        appItem02.setCollectionNumber(20);
        appItem02.setDistrict("北京");
        List<Tag> appItem02Tags = new ArrayList<Tag>();
        appItem02Tags.add(categoryTags.get(1));
        appItem02.setTags(appItem02Tags);
        appItem02 = commodityService.create(appItem02);
        createCoversForCommodity(appItem02.getId());
        createSampleMessagesForCommodity(appItem02.getId());

        Commodity appItem03 = new Commodity();
        appItem03.setTitle("小米官方旗舰店MIUI/小米 红米Note 4G增强版移动版大屏智能手机");
        appItem03.setDescription("高通骁龙400 四核1.6GHz 2GB内存+8GB闪存，5.5吋全贴合大屏 联通4G版 请选择“TD-LTE/FDD-LTE/ WCDMA/GSM” 移动4G版 请选择“TD-LTE(4G)/TD-SCDMA/GSM” 小米官方旗舰店未开通港澳台及海外的配送，请填写正确的大陆收货地址");
        appItem03.setPrice(899);
        appItem03.setCarriage(23);
        appItem03.setCollectionNumber(15);
        appItem03.setDistrict("北京");
        List<Tag> appItem03Tags = new ArrayList<Tag>();
        appItem03Tags.add(categoryTags.get(1));
        appItem03.setTags(appItem03Tags);
        appItem03 = commodityService.create(appItem03);
        createCoversForCommodity(appItem03.getId());
        createSampleMessagesForCommodity(appItem03.getId());

        Commodity appItem04 = new Commodity();
        appItem04.setTitle("【新品首发】skullcandy LOWRIDER 2.0 骷髅头潮流头戴式带麦耳机");
        appItem04.setDescription("【授权店】--品牌商授权经销商，官方正品行货 可查询真伪！！！！！！！电器城首选！！！！【换新服务】--1年内非人为损坏免费换新！我公司天猫提供延保保修服务，务必包装盒+防伪码+配件保留好！");
        appItem04.setPrice(2444);
        appItem04.setCarriage(65);
        appItem04.setCollectionNumber(23);
        appItem04.setDistrict("北京");
        List<Tag> appItem04Tags = new ArrayList<Tag>();
        appItem04Tags.add(categoryTags.get(1));
        appItem04.setTags(appItem04Tags);
        appItem04 = commodityService.create(appItem04);
        createCoversForCommodity(appItem04.getId());
        createSampleMessagesForCommodity(appItem04.getId());

        Commodity appItem05 = new Commodity();
        appItem05.setTitle("Toshiba/东芝 48L3350C 48英寸3D网络WiFi高清液晶电视平板电视");
        appItem05.setDescription("【迎新特惠】仅需3799元，新年好评再返20元红包，相当于3779元。");
        appItem05.setPrice(5632);
        appItem05.setCarriage(25);
        appItem05.setCollectionNumber(31);
        appItem05.setDistrict("电器城上海仓");
        List<Tag> appItem05Tags = new ArrayList<Tag>();
        appItem05Tags.add(categoryTags.get(1));
        appItem05.setTags(appItem05Tags);
        appItem05 = commodityService.create(appItem05);
        createCoversForCommodity(appItem05.getId());
        createSampleMessagesForCommodity(appItem05.getId());

        Commodity appItem06 = new Commodity();
        appItem06.setTitle("Haier/海尔 BC/BD-102HT/家用小冰柜 冷柜/大冷冻/冷藏冷冻省电");
        appItem06.setDescription("【疯狂钜惠】2月3日00:00-2月10日上午10:00 活动价949元!");
        appItem06.setPrice(643);
        appItem06.setCarriage(23);
        appItem06.setCollectionNumber(54);
        appItem06.setDistrict("山东青岛");
        List<Tag> appItem06Tags = new ArrayList<Tag>();
        appItem06Tags.add(categoryTags.get(1));
        appItem06.setTags(appItem06Tags);
        appItem06 = commodityService.create(appItem06);
        createCoversForCommodity(appItem06.getId());
        createSampleMessagesForCommodity(appItem06.getId());

        // insert the clothing Commodity
        Commodity clothingItem01 = new Commodity();
        clothingItem01.setTitle("千仞岗正品冬装新款羽绒服女2014潮加厚修身中长款大毛领冬衣1868");
        clothingItem01.setDescription("【领50元券实付仅需549】全场5折封顶，满100-10，满299减20，满399减50，满699减100，顺丰包邮！实付499送时尚围巾一条（花色随机发送，送完为止）");
        clothingItem01.setPrice(584);
        clothingItem01.setCarriage(26);
        clothingItem01.setCollectionNumber(14);
        clothingItem01.setDistrict("江苏苏州");
        List<Tag> clothingItem01Tags = new ArrayList<Tag>();
        clothingItem01Tags.add(categoryTags.get(2));
        clothingItem01.setTags(clothingItem01Tags);
        clothingItem01 = commodityService.create(clothingItem01);
        createCoversForCommodity(clothingItem01.getId());
        createSampleMessagesForCommodity(clothingItem01.getId());

        Commodity clothingItem02 = new Commodity();
        clothingItem02.setTitle("2014冬装新款羽绒服女卡伦艾莎中长款修身韩版加厚毛领女装外套潮");
        clothingItem02.setDescription("【不定时上架库存，敬请期待】此款羽绒服源自2014米兰国际时尚羽绒发布会。精致的修身裁剪，不挑身材!适合人群广泛。四层锁绒工艺，简洁、大方。【毛领可脱卸】【不定时上架库存，敬请期待】【不定时上架库存，敬请期待】【不定时上架库存，敬请期待】");
        clothingItem02.setPrice(642);
        clothingItem02.setCarriage(13);
        clothingItem02.setCollectionNumber(3);
        clothingItem02.setDistrict("浙江杭州");
        List<Tag> clothingItem02Tags = new ArrayList<Tag>();
        clothingItem02Tags.add(categoryTags.get(2));
        clothingItem02.setTags(clothingItem02Tags);
        clothingItem02 = commodityService.create(clothingItem02);
        createCoversForCommodity(clothingItem02.getId());
        createSampleMessagesForCommodity(clothingItem02.getId());

        Commodity clothingItem03 = new Commodity();
        clothingItem03.setTitle("苏醒的乐园 羽绒服女中长款加厚2015新款韩范潮奢华羽绒衣YRF859");
        clothingItem03.setDescription("领100元优惠券　享459元！！高端原创新品　三个可脱卸超大真貉子毛毛领　苏醒家坚持使用90%白鸭绒");
        clothingItem03.setPrice(444);
        clothingItem03.setCarriage(44);
        clothingItem03.setCollectionNumber(44);
        clothingItem03.setDistrict("上海");
        List<Tag> clothingItem03Tags = new ArrayList<Tag>();
        clothingItem03Tags.add(categoryTags.get(2));
        clothingItem03.setTags(clothingItem03Tags);
        clothingItem03 = commodityService.create(clothingItem03);
        createCoversForCommodity(clothingItem03.getId());
        createSampleMessagesForCommodity(clothingItem03.getId());

        Commodity clothingItem04 = new Commodity();
        clothingItem04.setTitle("tune tune春装新款2015淑女波点提花圆领荷叶边A字裙九分袖连衣裙");
        clothingItem04.setDescription("2015商场同款特供新品，手机专享8折390.4元！");
        clothingItem04.setPrice(333);
        clothingItem04.setCarriage(33);
        clothingItem04.setCollectionNumber(13);
        clothingItem04.setDistrict("广东深圳");
        List<Tag> clothingItem04Tags = new ArrayList<Tag>();
        clothingItem04Tags.add(categoryTags.get(2));
        clothingItem04.setTags(clothingItem04Tags);
        clothingItem04 = commodityService.create(clothingItem04);
        createCoversForCommodity(clothingItem04.getId());
        createSampleMessagesForCommodity(clothingItem04.getId());

        Commodity clothingItem05 = new Commodity();
        clothingItem05.setTitle("逸阳女裤2015新款春季女士黑色牛仔裤女小脚裤大码铅笔裤长裤0680");
        clothingItem05.setDescription("以前的自己，任性，喜欢花哨，喜欢逛地摊货、更喜欢打折的时候买一大堆没用的囤着！");
        clothingItem05.setPrice(456);
        clothingItem05.setCarriage(21);
        clothingItem05.setCollectionNumber(3);
        clothingItem05.setDistrict("河南郑州");
        List<Tag> clothingItem05Tags = new ArrayList<Tag>();
        clothingItem05Tags.add(categoryTags.get(2));
        clothingItem05.setTags(clothingItem05Tags);
        clothingItem05 = commodityService.create(clothingItem05);
        createCoversForCommodity(clothingItem05.getId());
        createSampleMessagesForCommodity(clothingItem05.getId());

        Commodity clothingItem06 = new Commodity();
        clothingItem06.setTitle("布依美2015春季新款长袖衬衫韩版时尚宽松简约中长款白色衬衫女春");
        clothingItem06.setDescription("手机下单更优惠");
        clothingItem06.setPrice(123);
        clothingItem06.setCarriage(10);
        clothingItem06.setCollectionNumber(6);
        clothingItem06.setDistrict("浙江杭州");
        List<Tag> clothingItem06Tags = new ArrayList<Tag>();
        clothingItem06Tags.add(categoryTags.get(2));
        clothingItem06.setTags(clothingItem06Tags);
        clothingItem06 = commodityService.create(clothingItem06);
        createCoversForCommodity(clothingItem06.getId());
        createSampleMessagesForCommodity(clothingItem06.getId());

        // insert the shoes Commodity
        Commodity shoesItem01 = new Commodity();
        shoesItem01.setTitle("康莉comely羊皮磨砂水钻方扣优雅女鞋 圆头低跟浅口套脚职业单鞋");
        shoesItem01.setDescription("『 康莉·专柜·正品 』　源自台湾的设计理念，演绎浪漫、高雅的生活格调~");
        shoesItem01.setPrice(324);
        shoesItem01.setCarriage(43);
        shoesItem01.setCollectionNumber(9);
        shoesItem01.setDistrict("广东佛山");
        List<Tag> shoesItem01Tags = new ArrayList<Tag>();
        shoesItem01Tags.add(categoryTags.get(3));
        shoesItem01.setTags(shoesItem01Tags);
        shoesItem01 = commodityService.create(shoesItem01);
        createCoversForCommodity(shoesItem01.getId());
        createSampleMessagesForCommodity(shoesItem01.getId());

        Commodity shoesItem02 = new Commodity();
        shoesItem02.setTitle("康莉comely2014年秋季新款低跟水钻单鞋 时尚浅口金色休闲女鞋");
        shoesItem02.setDescription("『 优雅·时尚·特惠 』　１００％官方正品保证！春单必败！");
        shoesItem02.setPrice(215);
        shoesItem02.setCarriage(12);
        shoesItem02.setCollectionNumber(3);
        shoesItem02.setDistrict("广东佛山");
        List<Tag> shoesItem02Tags = new ArrayList<Tag>();
        shoesItem02Tags.add(categoryTags.get(3));
        shoesItem02.setTags(shoesItem02Tags);
        shoesItem02 = commodityService.create(shoesItem02);
        createCoversForCommodity(shoesItem02.getId());
        createSampleMessagesForCommodity(shoesItem02.getId());

        Commodity shoesItem03 = new Commodity();
        shoesItem03.setTitle("exull依思q2015春季新款真皮拼色高跟内增高高帮鞋女鞋子15155144");
        shoesItem03.setDescription("1.魔术贴片，便捷穿脱；2.率性内增，从容洒脱");
        shoesItem03.setPrice(289);
        shoesItem03.setCarriage(67);
        shoesItem03.setCollectionNumber(8);
        shoesItem03.setDistrict("福建泉州");
        List<Tag> shoesItem03Tags = new ArrayList<Tag>();
        shoesItem03Tags.add(categoryTags.get(3));
        shoesItem03.setTags(shoesItem03Tags);
        shoesItem03 = commodityService.create(shoesItem03);
        createCoversForCommodity(shoesItem03.getId());
        createSampleMessagesForCommodity(shoesItem03.getId());

        Commodity shoesItem04 = new Commodity();
        shoesItem04.setTitle("低调女人2014秋季纯色休闲鞋女板鞋韩版潮流透气运动女鞋内增高鞋");
        shoesItem04.setDescription("采用优质帆布 更加透气 甜美拼色 更显粉嫩青春气息");
        shoesItem04.setPrice(120);
        shoesItem04.setCarriage(10);
        shoesItem04.setCollectionNumber(7);
        shoesItem04.setDistrict("浙江温州");
        List<Tag> shoesItem04Tags = new ArrayList<Tag>();
        shoesItem04Tags.add(categoryTags.get(3));
        shoesItem04.setTags(shoesItem04Tags);
        shoesItem04 = commodityService.create(shoesItem04);
        createCoversForCommodity(shoesItem04.getId());
        createSampleMessagesForCommodity(shoesItem04.getId());

        Commodity shoesItem05 = new Commodity();
        shoesItem05.setTitle("2015春季新款honeyGIRL甜美女鞋蝴蝶结高跟鞋女细跟尖头浅口单鞋");
        shoesItem05.setDescription("2015春季单鞋第一波大促，标记区内100多款美鞋任选第2双半价，加入购物车单笔下单有效，现货包邮直达，两双其中最低价那双半价作为差价返现；");
        shoesItem05.setPrice(169);
        shoesItem05.setCarriage(3);
        shoesItem05.setCollectionNumber(1);
        shoesItem05.setDistrict("浙江温州");
        List<Tag> shoesItem05Tags = new ArrayList<Tag>();
        shoesItem05Tags.add(categoryTags.get(3));
        shoesItem05.setTags(shoesItem05Tags);
        shoesItem05 = commodityService.create(shoesItem05);
        createCoversForCommodity(shoesItem05.getId());
        createSampleMessagesForCommodity(shoesItem05.getId());

        Commodity shoesItem06 = new Commodity();
        shoesItem06.setTitle("2015春秋新款潮女鞋手工鞋子真皮休闲平底皮鞋英伦大头鞋平跟单鞋");
        shoesItem06.setDescription("古奇天伦天猫专营店，专柜正品，假一赔十，亲们放心选购");
        shoesItem06.setPrice(218);
        shoesItem06.setCarriage(20);
        shoesItem06.setCollectionNumber(23);
        shoesItem06.setDistrict("浙江温州");
        List<Tag> shoesItem06Tags = new ArrayList<Tag>();
        shoesItem06Tags.add(categoryTags.get(3));
        shoesItem06.setTags(shoesItem06Tags);
        shoesItem06 = commodityService.create(shoesItem06);
        createCoversForCommodity(shoesItem06.getId());
        createSampleMessagesForCommodity(shoesItem06.getId());

        // insert the bag Commodity
        Commodity bagItem01 = new Commodity();
        bagItem01.setTitle("朱尔波士顿牛皮女包2015新款时尚皮包手提包包蛇纹单肩袋女士包女");
        bagItem01.setDescription("温馨提示:春节将至,发货安排通知:1发货时间（2月7-14日17点前的订单）;2只接单不发货(2月15日-23日);3恢复正常发货(2月24日开始)朱尔专柜正品　1月新品");
        bagItem01.setPrice(329.9);
        bagItem01.setCarriage(36);
        bagItem01.setCollectionNumber(11);
        bagItem01.setDistrict("广东广州");
        List<Tag> bagItem01Tags = new ArrayList<Tag>();
        bagItem01Tags.add(categoryTags.get(4));
        bagItem01.setTags(bagItem01Tags);
        bagItem01 = commodityService.create(bagItem01);
        createCoversForCommodity(bagItem01.getId());

        Commodity bagItem02 = new Commodity();
        bagItem02.setTitle("朱尔原创新款包包手提包 女士时尚牛皮女包 波士顿枕头包单肩斜跨");
        bagItem02.setDescription("朱尔原创新款包包手提包 女士时尚牛皮女包 波士顿枕头包单肩斜跨");
        bagItem02.setPrice(376.9);
        bagItem02.setCarriage(21);
        bagItem02.setCollectionNumber(8);
        bagItem02.setDistrict("广东广州");
        List<Tag> bagItem02Tags = new ArrayList<Tag>();
        bagItem02Tags.add(categoryTags.get(4));
        bagItem02.setTags(bagItem02Tags);
        bagItem02 = commodityService.create(bagItem02);
        createCoversForCommodity(bagItem02.getId());
        createSampleMessagesForCommodity(bagItem02.getId());

        Commodity bagItem03 = new Commodity();
        bagItem03.setTitle("朱尔鳄鱼纹真皮女包大包 时尚头层牛皮女士包包 新款女单肩手提包");
        bagItem03.setDescription("温馨提示:春节将至,发货安排通知:1发货时间（2月7-14日17点前的订单）;2只接单不发货(2月15日-23日);3恢复正常发货(2月24日开始)朱尔正品此包的设计理念是：自信、内涵、气质优雅、尊贵、价值、有见识，专为成熟女性量身订做的气质款包包！");
        bagItem03.setPrice(472.5);
        bagItem03.setCarriage(100);
        bagItem03.setCollectionNumber(78);
        bagItem03.setDistrict("广东广州");
        List<Tag> bagItem03Tags = new ArrayList<Tag>();
        bagItem03Tags.add(categoryTags.get(4));
        bagItem03.setTags(bagItem03Tags);
        bagItem03 = commodityService.create(bagItem03);
        createCoversForCommodity(bagItem03.getId());
        createSampleMessagesForCommodity(bagItem03.getId());

        Commodity bagItem04 = new Commodity();
        bagItem04.setTitle("袋黛安正品万向轮行李箱20/24/26拉杆箱包旅行箱28寸托运箱子男女");
        bagItem04.setDescription("袋黛安官方零利润促销！热销164373件！品质保证！只为品牌做人气！升级第四代实心静音万向轮!第二代内壁加厚铝合金拉杆!全新材质！绝无回料！天猫正品!");
        bagItem04.setPrice(367.9);
        bagItem04.setCarriage(342);
        bagItem04.setCollectionNumber(45);
        bagItem04.setDistrict("上海");
        List<Tag> bagItem04Tags = new ArrayList<Tag>();
        bagItem04Tags.add(categoryTags.get(4));
        bagItem04.setTags(bagItem04Tags);
        bagItem04 = commodityService.create(bagItem04);
        createCoversForCommodity(bagItem04.getId());
        createSampleMessagesForCommodity(bagItem04.getId());

        Commodity bagItem05 = new Commodity();
        bagItem05.setTitle("袋黛安万向轮PC拉杆箱20寸旅行箱密码箱29寸行李箱包男女托运箱子");
        bagItem05.setDescription("袋黛安最新款高级旅行箱.只为少数人而生！ TSA密码锁.深框铝框！【专柜正品】【超静音万向轮】【高端大气上档次】");
        bagItem05.setPrice(427.5);
        bagItem05.setCarriage(271);
        bagItem05.setCollectionNumber(20);
        bagItem05.setDistrict("上海");
        List<Tag> bagItem05Tags = new ArrayList<Tag>();
        bagItem05Tags.add(categoryTags.get(4));
        bagItem05.setTags(bagItem05Tags);
        bagItem05 = commodityService.create(bagItem05);
        createCoversForCommodity(bagItem05.getId());
        createSampleMessagesForCommodity(bagItem05.getId());

        Commodity bagItem06 = new Commodity();
        bagItem06.setTitle("F瑞士军刀威戈wenger双肩背包男女电脑背包商务背包旅行学生背包");
        bagItem06.setDescription("F瑞士军刀威戈wenger双肩背包男女电脑背包商务背包旅行学生背包");
        bagItem06.setPrice(217.5);
        bagItem06.setCarriage(129);
        bagItem06.setCollectionNumber(10);
        bagItem06.setDistrict("上海");
        List<Tag> bagItem06Tags = new ArrayList<Tag>();
        bagItem06Tags.add(categoryTags.get(4));
        bagItem06.setTags(bagItem06Tags);
        bagItem06 = commodityService.create(bagItem06);
        createCoversForCommodity(bagItem06.getId());
        createSampleMessagesForCommodity(bagItem06.getId());

        // insert the book Commodity
        Commodity bookItem01 = new Commodity();
        bookItem01.setTitle("【购书送福袋】当当网 狼图腾 正版现货 小说 姜戎 研究狼的旷世奇书 现代文学 书籍");
        bookItem01.setDescription("F瑞士军刀威戈wenger双肩背包男女电脑背包商务背包旅行学生背包");
        bookItem01.setPrice(18.9);
        bookItem01.setCarriage(58);
        bookItem01.setCollectionNumber(6);
        bookItem01.setDistrict("北京");
        List<Tag> bookItem01Tags = new ArrayList<Tag>();
        bookItem01Tags.add(categoryTags.get(5));
        bookItem01.setTags(bookItem01Tags);
        bookItem01 = commodityService.create(bookItem01);
        createCoversForCommodity(bookItem01.getId());
        createSampleMessagesForCommodity(bookItem01.getId());

        Commodity bookItem02 = new Commodity();
        bookItem02.setTitle("【当当网】正版包邮 追风筝的人 胡塞尼 小说 首部中文畅销书籍 两千万读者口耳相传 快乐大本营 高圆圆力荐 当当网旗舰店");
        bookItem02.setDescription("【70%城市，次日达】");
        bookItem02.setPrice(14.9);
        bookItem02.setCarriage(39);
        bookItem02.setCollectionNumber(8);
        bookItem02.setDistrict("北京");
        List<Tag> bookItem02Tags = new ArrayList<Tag>();
        bookItem02Tags.add(categoryTags.get(5));
        bookItem02.setTags(bookItem02Tags);
        bookItem02 = commodityService.create(bookItem02);
        createCoversForCommodity(bookItem02.getId());
        createSampleMessagesForCommodity(bookItem02.getId());

        Commodity bookItem03 = new Commodity();
        bookItem03.setTitle("包邮正版现货！101个有科学根据的减肥小偏方 邱正宏教你越吃越瘦的101个减肥的秘密！");
        bookItem03.setDescription("台湾诚品、金石堂、博客来 生活类畅销排行榜第一名！");
        bookItem03.setPrice(23.7);
        bookItem03.setCarriage(15);
        bookItem03.setCollectionNumber(5);
        bookItem03.setDistrict("北京");
        List<Tag> bookItem03Tags = new ArrayList<Tag>();
        bookItem03Tags.add(categoryTags.get(5));
        bookItem03.setTags(bookItem03Tags);
        bookItem03 = commodityService.create(bookItem03);
        createCoversForCommodity(bookItem03.getId());
        createSampleMessagesForCommodity(bookItem03.getId());

        Commodity bookItem04 = new Commodity();
        bookItem04.setTitle("拍下8.8赠书 现货星火英语四级真题/CET4级 大学英语四级 (10套真题详解+标准预测4级)英语四级真题试卷 四级英语听力作文词汇翻译");
        bookItem04.setDescription("史销二十万册，星火四级试卷， 10套真题+5套预测，25篇作文和词汇卡片！①独家赠新题型翻译强化(30篇翻译+2000翻译词汇)PDF版1份、②赠6月专用【阅读写作听力强化】实物一份；(10篇阅读+30篇作文)1份！③超赞的电子赠品(听力词汇真题视频)联系客服【活动期间拍下8.8元】");
        bookItem04.setPrice(12.7);
        bookItem04.setCarriage(4);
        bookItem04.setCollectionNumber(1);
        bookItem04.setDistrict("江苏南京");
        List<Tag> bookItem04Tags = new ArrayList<Tag>();
        bookItem04Tags.add(categoryTags.get(5));
        bookItem04.setTags(bookItem04Tags);
        bookItem04 = commodityService.create(bookItem04);
        createCoversForCommodity(bookItem04.getId());
        createSampleMessagesForCommodity(bookItem04.getId());

        Commodity bookItem05 = new Commodity();
        bookItem05.setTitle("星火备考2015年6月大学英语四级/CET4级考试改革新题型全真试题+标准模拟(12套真题+5套模拟)词汇写作文听力英语四级真题卷预测");
        bookItem05.setDescription("正版现货，品质保证！12套历年真题(含改革后新题)+5套模拟预测(新题型版)、30篇作文、15套听力题和2000翻译词，现独家赠：①06月专用阅读写作强化实物书1本、2.10元抵用卷1张②翻译强化PDF一份、还有海量电子资料，数量有限先到先得哦~（");
        bookItem05.setPrice(17.7);
        bookItem05.setCarriage(3);
        bookItem05.setCollectionNumber(7);
        bookItem05.setDistrict("江苏南京");
        List<Tag> bookItem05Tags = new ArrayList<Tag>();
        bookItem05Tags.add(categoryTags.get(5));
        bookItem05.setTags(bookItem05Tags);
        bookItem05 = commodityService.create(bookItem05);
        createCoversForCommodity(bookItem05.getId());
        createSampleMessagesForCommodity(bookItem05.getId());

        Commodity bookItem06 = new Commodity();
        bookItem06.setTitle("现货星火大学英语四级/CET4级考试改革新题型点评历年真题试卷(10套真题+30篇作文+15套听力+50篇翻译+2000词汇)");
        bookItem06.setDescription("本书适用于2015年06月考试");
        bookItem06.setPrice(6.7);
        bookItem06.setCarriage(2);
        bookItem06.setCollectionNumber(2);
        bookItem06.setDistrict("上海");
        List<Tag> bookItem06Tags = new ArrayList<Tag>();
        bookItem06Tags.add(categoryTags.get(5));
        bookItem06.setTags(bookItem06Tags);
        bookItem06 = commodityService.create(bookItem06);
        createCoversForCommodity(bookItem06.getId());
        createSampleMessagesForCommodity(bookItem06.getId());
    }

    private void createSampleImages() {
        String path = ConfigManagmentUtil.getConfigProperties(Constants.IMAGE_STORAGE_BASE_PATH);
        Image image1 = new Image();
        image1.setUri(path + "/01.jpg");
        image1.setTitle("Test1");
        image1.setDescription("for test - 1");
        image1.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image1));

        Image image2 = new Image();
        image2.setUri(path + "/02.jpg");
        image2.setTitle("Test2");
        image2.setDescription("for test - 2");
        image2.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image2));

        Image image3 = new Image();
        image3.setUri(path + "/03.jpg");
        image3.setTitle("Test3");
        image3.setDescription("for test - 3");
        image3.setUserId(standardAdmin.getId());
        sampleImageList.add(imageService.create(image3));
    }

    private void createCoversForCommodity(String commodityId) {
        Cover cover1 = new Cover();
        cover1.setCommodityId(commodityId);
        cover1.setImageId(sampleImageList.get(0).getId());
        cover1.setMainCoverYN("Y");
        cover1.setDisplaySequence(0);
        coverService.create(cover1);

        Cover cover2 = new Cover();
        cover2.setCommodityId(commodityId);
        cover2.setImageId(sampleImageList.get(1).getId());
        cover2.setDisplaySequence(1);
        coverService.create(cover2);

        Cover cover3 = new Cover();
        cover3.setCommodityId(commodityId);
        cover3.setImageId(sampleImageList.get(2).getId());
        cover3.setDisplaySequence(2);
        coverService.create(cover3);
    }

    private void createSampleMessagesForCommodity(String commodityId) {
        Message message1 = new Message();
        message1.setCommodityId(commodityId);
        message1.setContent("测试信息1");
        message1.setFloor(1);
        message1.setUserId(standardUser.getId());
        message1.setUserName(standardUser.getUserName());
        message1 = messageService.create(message1);

        Message message1r = new Message();
        message1r.setCommodityId(commodityId);
        message1r.setContent("测试信息1-回复");
        message1r.setFloor(1);
        message1r.setUserId(standardUser.getId());
        message1r.setUserName(standardUser.getUserName());
        message1r.setParentId(message1.getId());
        messageService.create(message1r);

        Message message2 = new Message();
        message2.setCommodityId(commodityId);
        message2.setContent("测试信息2");
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
        slice2.setImageId(sampleImageList.get(1).getId());
        slice2.setCommodityId(sampleCommodities.get(1).getId());
        slice2.setDisplaySequence(1);
        slice2.setDescription("test slice 2");
        sliceService.create(slice2);

        Slice slice3 = new Slice();
        slice3.setImageId(sampleImageList.get(2).getId());
        slice3.setCommodityId(sampleCommodities.get(2).getId());
        slice3.setDisplaySequence(2);
        slice3.setDescription("test slice 3");
        sliceService.create(slice3);
    }

    private void createSampleHots() {
        Hot hot1 = new Hot();
        hot1.setCommodityId(sampleCommodities.get(0).getId());
        hot1.setImageId(sampleImageList.get(0).getId());
        hot1.setDisplaySequence(0);
        hotService.create(hot1);

        Hot hot2 = new Hot();
        hot2.setCommodityId(sampleCommodities.get(1).getId());
        hot2.setImageId(sampleImageList.get(1).getId());
        hot2.setDisplaySequence(1);
        hotService.create(hot2);

        Hot hot3 = new Hot();
        hot3.setCommodityId(sampleCommodities.get(2).getId());
        hot3.setImageId(sampleImageList.get(2).getId());
        hot3.setDisplaySequence(2);
        hotService.create(hot3);
    }
}
