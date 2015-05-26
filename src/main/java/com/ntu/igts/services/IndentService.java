package com.ntu.igts.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ntu.igts.enums.IndentStatusEnum;
import com.ntu.igts.enums.PayTypeEnum;
import com.ntu.igts.model.Indent;

public interface IndentService {

    public Indent create(Indent indent);

    public Indent update(Indent indent);

    public boolean delete(String indentId);

    public List<Indent> getByUserId(String userId);

    public Indent getById(String indentId);

    public Page<Indent> getPaginatedIndentByBuyerId(int currentPage, int pageSize, String userId);

    public Indent getByCommodityId(String commodityId);

    public Indent dealComplete(Indent indent, String buyerId);

    public Indent purchase(Indent indent, String buyerId, PayTypeEnum payTypeEnum);

    public Indent cancelDeal(Indent indent, String userId);

    public Indent returnDeal(Indent indent, String buyerId);

    public Indent returnComplete(Indent indent, String buyerId);

    public Page<Indent> getPaginatedIndentBySellerId(int currentPage, int pageSize, String userId);

    public Page<Indent> getPaginatedSpecifiedIndentBySellerId(IndentStatusEnum indentStatusEnum, int currentPage,
                    int pageSize, String userId);
}
