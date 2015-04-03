package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.Hot;

@JsonRootName("hotcommodities")
public class HotList extends ArrayList<Hot> {

    private static final long serialVersionUID = 5186869508892367384L;

    public HotList(Collection<? extends Hot> h) {
        super(h);
    }

    public HotList() {

    }
}
