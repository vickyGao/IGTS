package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.Slice;

public interface SliceService {

    public Slice create(Slice slice);

    public Slice update(Slice slice);

    public boolean delete(String sliceId);

    public Slice getById(String sliceId);

    public List<Slice> getAll();
}
