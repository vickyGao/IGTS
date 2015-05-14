package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.Image;

@JsonRootName("images")
public class ImageList extends ArrayList<Image> {

    private static final long serialVersionUID = 512563009396845707L;

    public ImageList(Collection<? extends Image> i) {
        super(i);
    }

    public ImageList() {

    }
}
