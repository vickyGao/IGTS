package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.CustomModule;

@JsonRootName("custommodules")
public class CustomModuleList extends ArrayList<CustomModule> {

    private static final long serialVersionUID = -8860990426892107103L;

    public CustomModuleList(Collection<? extends CustomModule> c) {
        super(c);
    }

    public CustomModuleList() {

    }
}
