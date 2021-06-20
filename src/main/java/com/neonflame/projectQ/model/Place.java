package com.neonflame.projectQ.model;

import javax.persistence.*;
import java.io.PrintStream;

@Embeddable
public class Place {

    @ManyToOne(optional = false)
    private User user;

    private int index;

    public Place() {
    }

    public Place(User user, int index) {
        this.user = user;
        this.index = index;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
