package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.Commodity;

@JsonRootName("commodities")
public class CommodityList extends ArrayList<Commodity> {

    private static final long serialVersionUID = 1068957189165330552L;

    public CommodityList(Collection<? extends Commodity> b) {
        super(b);
    }

    public CommodityList() {

    }
}
