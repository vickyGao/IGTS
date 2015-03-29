package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.Favorite;

@JsonRootName("favorites")
public class FavoriteList extends ArrayList<Favorite> {

    private static final long serialVersionUID = 3835412561937681160L;

    public FavoriteList(Collection<? extends Favorite> f) {
        super(f);
    }

    public FavoriteList() {

    }
}
