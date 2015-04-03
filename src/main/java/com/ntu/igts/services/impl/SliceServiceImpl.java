package com.ntu.igts.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntu.igts.enums.OrderByEnum;
import com.ntu.igts.enums.SortByEnum;
import com.ntu.igts.model.Slice;
import com.ntu.igts.repository.SliceRepository;
import com.ntu.igts.services.SliceService;

@Service
public class SliceServiceImpl implements SliceService {

    @Resource
    private SliceRepository sliceRepository;

    @Override
    @Transactional
    public Slice create(Slice slice) {
        return sliceRepository.create(slice);
    }

    @Override
    @Transactional
    public Slice update(Slice slice) {
        return sliceRepository.update(slice);
    }

    @Override
    @Transactional
    public boolean delete(String sliceId) {
        sliceRepository.delete(sliceId);
        Slice slice = sliceRepository.findById(sliceId);
        if (slice == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Slice getById(String sliceId) {
        return sliceRepository.findById(sliceId);
    }

    @Override
    public List<Slice> getAll() {
        return sliceRepository.findAll(SortByEnum.DISPLAY_SEQUENCE, OrderByEnum.ASC);
    }

}
