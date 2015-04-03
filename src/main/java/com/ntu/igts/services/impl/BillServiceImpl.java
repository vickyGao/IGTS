package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.model.Bill;
import com.ntu.igts.repository.BillRepository;
import com.ntu.igts.services.BillService;

@Service
public class BillServiceImpl implements BillService {

    @Resource
    private BillRepository billRepository;

    @Override
    @Transactional
    public Bill create(Bill bill) {
        return billRepository.create(bill);
    }

    @Override
    @Transactional
    public Bill update(Bill bill) {
        return billRepository.update(bill);
    }

    @Override
    @Transactional
    public boolean delete(String billId) {
        billRepository.delete(billId);
        Bill bill = billRepository.findById(billId);
        if (bill == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Bill getById(String billId) {
        return billRepository.findById(billId);
    }

    @Override
    public List<Bill> getByUserId(String userId) {
        return billRepository.getByUserId(userId);
    }

}
