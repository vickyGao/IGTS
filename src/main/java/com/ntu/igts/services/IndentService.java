package com.ntu.igts.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ntu.igts.model.Indent;

public interface IndentService {

    public Indent create(Indent indent);

    public Indent update(Indent indent);

    public boolean delete(String indentId);

    public List<Indent> getByUserId(String userId);

    public Indent getById(String indentId);

    public Page<Indent> getPaginatedIndentByUserId(int currentPage, int pageSize, String userId);
}
