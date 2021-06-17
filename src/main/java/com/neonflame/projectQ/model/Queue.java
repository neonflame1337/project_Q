package com.neonflame.projectQ.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Queue {
    @Id
    private Long id;
    private int size;
    private boolean active;

    @ManyToOne
    private User creator;

    private LocalDateTime created;

    @OneToMany
    private Set<User> members;

    @OneToMany
    private List<User> places;

    public Queue() {
        this.created = LocalDateTime.now();
        this.members = new HashSet<>();
        this.places = new ArrayList<>();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public List<User> getPlaces() {
        return places;
    }

    public void setPlaces(List<User> places) {
        this.places = places;
    }
}
