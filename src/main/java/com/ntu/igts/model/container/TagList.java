package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.Tag;

@JsonRootName("tags")
public class TagList extends ArrayList<Tag> {

    private static final long serialVersionUID = 60612903175324872L;

    public TagList(Collection<? extends Tag> t) {
        super(t);
    }

    public TagList() {

    }
}
