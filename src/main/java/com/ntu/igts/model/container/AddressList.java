package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.Address;

@JsonRootName("addresses")
public class AddressList extends ArrayList<Address> {

    private static final long serialVersionUID = 7511807283019245453L;

    public AddressList(Collection<? extends Address> a) {
        super(a);
    }

    public AddressList() {

    }
}
