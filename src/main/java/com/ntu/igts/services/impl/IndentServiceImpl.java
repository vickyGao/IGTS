package com.ntu.igts.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.constants.Constants;
import com.ntu.igts.enums.ActiveStateEnum;
import com.ntu.igts.enums.IndentStatusEnum;
import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.PayTypeEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.exception.ServiceWarningException;
import com.ntu.igts.i18n.MessageKeys;
import com.ntu.igts.model.Bill;
import com.ntu.igts.model.Commodity;
import com.ntu.igts.model.Indent;
import com.ntu.igts.model.User;
import com.ntu.igts.repository.IndentRepository;
import com.ntu.igts.services.BillService;
import com.ntu.igts.services.CommodityService;
import com.ntu.igts.services.IndentService;
import com.ntu.igts.services.UserService;

@Service
public class IndentServiceImpl implements IndentService {

    @Resource
    private IndentRepository indentRepository;
    @Resource
    private UserService userService;
    @Resource
    private CommodityService commodityService;
    @Resource
    private BillService billService;

    @Override
    public Indent create(Indent indent) {
        return indentRepository.create(indent);
    }

    @Override
    public Indent update(Indent indent) {
        return indentRepository.update(indent);
    }

    @Override
    public boolean delete(String indentId) {
        indentRepository.delete(indentId);
        Indent indent = indentRepository.findById(indentId);
        if (indent == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Indent> getByUserId(String userId) {
        return indentRepository.getByUserId(userId);
    }

    @Override
    public Indent getById(String indentId) {
        return indentRepository.findById(indentId);
    }

    @Override
    public Page<Indent> getPaginatedIndentByBuyerId(int currentPage, int pageSize, String userId) {
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(Constants.FIELD_USERID, userId);
        return indentRepository.findByPage(currentPage, pageSize, SortByEnum.CREATED_TIME, OrderByEnum.DESC,
                        criteriaMap);
    }

    @Override
    public Indent getByCommodityId(String commodityId) {
        List<Indent> indentList = indentRepository.getByCommodityId(commodityId);
        if (indentList.size() >= 1) {
            return indentList.get(0);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Indent dealComplete(Indent indent, String buyerId) {
        Indent updatedIndent = null;
        if (indent != null) {
            // Only Indent with delivered status can be updated to completed
            if (!IndentStatusEnum.DELIVERED.value().equals(indent.getStatus())) {
                throw new ServiceWarningException("Cannot complete the deal as seller has not delivered goods",
                                MessageKeys.CANNOT_COMPLETE_DEAL_AS_SELLER_NOT_DELEVERED);
            }

            // Check whether the commodity exists
            Commodity commodity = commodityService.getById(indent.getCommodityId());
            if (commodity == null) {
                String[] param = { indent.getCommodityId() };
                throw new ServiceWarningException("Cannot find commodity for id " + indent.getCommodityId(),
                                MessageKeys.COMMODITY_NOT_FOUND_FOR_ID, param);
            }
            // Check whether the seller exists
            User seller = userService.getUserById(commodity.getUserId());
            if (seller == null) {
                String[] param = { commodity.getUserId() };
                throw new ServiceWarningException("Cannot find user for id " + commodity.getUserId(),
                                MessageKeys.USER_NOT_FOUND_FOR_ID, param);
            }
            // Check whether the buyer exists
            User buyer = userService.getUserById(buyerId);
            if (buyer == null) {
                String[] param = { buyerId };
                throw new ServiceWarningException("Cannot find user for id " + buyerId,
                                MessageKeys.USER_NOT_FOUND_FOR_ID, param);
            }

            // Start to complete the deal
            // Update the buyer's and the seller's money
            double totalDealMoney = indent.getIndentPrice();
            buyer.setLockedMoney(seller.getLockedMoney() - totalDealMoney);
            seller.setMoney(seller.getMoney() + totalDealMoney);
            userService.update(buyer);
            userService.update(seller);

            // Update indent's state
            indent.setStatus(IndentStatusEnum.COMPLETE.value());
            indent.setDealCompleteTime(new Date());
            updatedIndent = update(indent);

            // Create bill for buyer & seller
            Bill buyerBill = new Bill();
            buyerBill.setUserId(buyer.getId());
            buyerBill.setContent("支出");
            buyerBill.setAmount(-totalDealMoney);
            billService.create(buyerBill);

            Bill sellerBill = new Bill();
            sellerBill.setUserId(seller.getId());
            sellerBill.setContent("收入");
            sellerBill.setAmount(totalDealMoney);
            billService.create(sellerBill);
        }
        return updatedIndent;
    }

    @Override
    @Transactional
    public Indent purchase(Indent indent, String buyerId, PayTypeEnum payTypeEnum) {
        Indent updatedIndent = null;
        if (indent != null) {
            // Only Indent with un-paid status can be updated to paid
            if (!IndentStatusEnum.UNPAID.value().equals(indent.getStatus())) {
                throw new ServiceWarningException("The indent has not been created", MessageKeys.INDENT_NOT_CREATED);
            }

            // Check whether the commodity exists
            Commodity commodity = commodityService.getById(indent.getCommodityId());
            if (commodity == null) {
                String[] param = { indent.getCommodityId() };
                throw new ServiceWarningException("Cannot find commodity for id " + indent.getCommodityId(),
                                MessageKeys.COMMODITY_NOT_FOUND_FOR_ID, param);
            }
            // Check whether the seller exists
            User seller = userService.getUserById(commodity.getUserId());
            if (seller == null) {
                String[] param = { commodity.getUserId() };
                throw new ServiceWarningException("Cannot find user for id " + commodity.getUserId(),
                                MessageKeys.USER_NOT_FOUND_FOR_ID, param);
            }
            // Check whether the buyer exists
            User buyer = userService.getUserById(buyerId);
            if (buyer == null) {
                String[] param = { buyerId };
                throw new ServiceWarningException("Cannot find user for id " + buyerId,
                                MessageKeys.USER_NOT_FOUND_FOR_ID, param);
            }
            // Check whether the user has enough money
            double totalUserMoney = buyer.getMoney();
            double totalIndentMoney = indent.getIndentPrice();
            if (totalUserMoney < totalIndentMoney) {
                throw new ServiceWarningException("Do not have enough money", MessageKeys.MONEY_NOT_ENOUGH);
            }

            // Start purchase progress
            // First under carriage the commodity
            commodity.setActiveYN(ActiveStateEnum.NEGATIVE.value());
            commodityService.update(commodity);

            // Update the buyer's money
            buyer.setMoney(buyer.getMoney() - indent.getIndentPrice());
            buyer.setLockedMoney(buyer.getLockedMoney() + indent.getIndentPrice());
            userService.update(buyer);

            // Update indent's status
            indent.setStatus(IndentStatusEnum.PAID.value());
            indent.setPayTime(new Date());
            indent.setPayType(payTypeEnum.value());
            updatedIndent = update(indent);
        }
        return updatedIndent;
    }

    @Override
    public Indent cancelDeal(Indent indent, String buyerId) {
        Indent updatedIndent = null;
        if (indent != null) {
            if (!IndentStatusEnum.UNPAID.value().equals(indent.getStatus())) {
                throw new ServiceWarningException("Can only cancel un-paid indent",
                                MessageKeys.CAN_ONLY_CANCEL_UNPAID_INDENT);
            }
            if (!indent.getUserId().equals(buyerId)) {
                throw new ServiceWarningException("Can only cancel your own indent",
                                MessageKeys.CAN_ONLY_CANCEL_YOUR_OWN_INDENT);
            }
            indent.setStatus(IndentStatusEnum.CANCELLED.value());
            updatedIndent = update(indent);
        }
        return updatedIndent;
    }

    @Override
    public Indent returnDeal(Indent indent, String buyerId) {
        Indent updatedIndent = null;
        if (indent != null) {
            if (IndentStatusEnum.PAID.value().equals(indent.getStatus())
                            || IndentStatusEnum.DELIVERED.value().equals(indent.getStatus())) {
                indent.setStatus(IndentStatusEnum.RETURNING.value());
                updatedIndent = update(indent);
            }
        }
        return updatedIndent;
    }

    @Override
    @Transactional
    public Indent returnComplete(Indent indent, String buyerId) {
        Indent updatedIndent = null;
        if (indent != null) {
            // Only allow to complete a returning indent
            if (!IndentStatusEnum.RETURNING.value().equals(indent.getStatus())) {
                throw new ServiceWarningException("Cannot complete deal as its not in Returning status",
                                MessageKeys.CANNOT_COMPLETE_DEAL_AS_ITS_NOT_IN_RETURNING_STATUS);
            }
            // Check whether the commodity exists
            Commodity commodity = commodityService.getById(indent.getCommodityId());
            if (commodity == null) {
                String[] param = { indent.getCommodityId() };
                throw new ServiceWarningException("Cannot find commodity for id " + indent.getCommodityId(),
                                MessageKeys.COMMODITY_NOT_FOUND_FOR_ID, param);
            }
            // Check whether the buyer exists
            User buyer = userService.getUserById(buyerId);
            if (buyer == null) {
                String[] param = { buyerId };
                throw new ServiceWarningException("Cannot find user for id " + buyerId,
                                MessageKeys.USER_NOT_FOUND_FOR_ID, param);
            }

            // Start the deal complete progress
            double totalDealMoney = indent.getIndentPrice();
            buyer.setMoney(buyer.getMoney() + totalDealMoney);
            buyer.setLockedMoney(buyer.getLockedMoney() - totalDealMoney);
            userService.update(buyer);

            // Update indent's state
            indent.setStatus(IndentStatusEnum.COMPLETE.value());
            indent.setDealCompleteTime(new Date());
            updatedIndent = update(indent);
        }
        return updatedIndent;
    }

    @Override
    public Page<Indent> getPaginatedIndentBySellerId(int currentPage, int pageSize, String userId) {
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(Constants.FIELD_SELLER_ID, userId);
        return indentRepository.findByPage(currentPage, pageSize, SortByEnum.CREATED_TIME, OrderByEnum.DESC,
                        criteriaMap);
    }

}
