package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.Indent;

@JsonRootName("indents")
public class IndentList extends ArrayList<Indent> {

    private static final long serialVersionUID = -3815826881319550865L;

    public IndentList(Collection<? extends Indent> i) {
        super(i);
    }

    public IndentList() {

    }
}
