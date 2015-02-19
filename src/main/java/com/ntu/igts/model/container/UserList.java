package com.ntu.igts.model.container;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.ntu.igts.model.User;

@JsonRootName("users")
public class UserList extends ArrayList<User> {

    private static final long serialVersionUID = -2220327054422493413L;

    public UserList(Collection<? extends User> u) {
        super(u);
    }

    public UserList() {

    }

}
