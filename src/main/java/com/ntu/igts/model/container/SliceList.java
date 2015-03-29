package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.Slice;

@JsonRootName("slices")
public class SliceList extends ArrayList<Slice> {

    private static final long serialVersionUID = -196140752831920428L;

    public SliceList(Collection<? extends Slice> s) {
        super(s);
    }

    public SliceList() {

    }
}
