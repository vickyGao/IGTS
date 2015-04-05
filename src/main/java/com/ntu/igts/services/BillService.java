package com.ntu.igts.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ntu.igts.model.Bill;

public interface BillService {

    public Bill create(Bill bill);

    public Bill update(Bill bill);

    public boolean delete(String billId);

    public Bill getById(String billId);

    public List<Bill> getByUserId(String userId);

    public Page<Bill> getPaginatedBillsByUserId(int currentPage, int pageSize, String userId);
}
