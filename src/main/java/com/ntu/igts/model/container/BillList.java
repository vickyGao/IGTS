package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.Bill;

@JsonRootName("bills")
public class BillList extends ArrayList<Bill> {

    private static final long serialVersionUID = 3134494049370988008L;

    public BillList(Collection<? extends Bill> b) {
        super(b);
    }

    public BillList() {

    }
}
