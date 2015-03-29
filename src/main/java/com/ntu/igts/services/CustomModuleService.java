package com.ntu.igts.services;

import java.util.List;

import com.ntu.igts.model.CustomModule;

public interface CustomModuleService {

    public CustomModule create(CustomModule customModule);

    public CustomModule update(CustomModule customModule);

    public boolean delete(String customModuleId);

    public CustomModule getById(String customModuleId);

    public List<CustomModule> getCustomModules();
}
