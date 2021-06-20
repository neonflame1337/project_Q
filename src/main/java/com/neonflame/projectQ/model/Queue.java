package com.neonflame.projectQ.model;

import com.neonflame.projectQ.excrptions.QueueIndexErrorException;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Queue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int size;
    private boolean active;

    @CreatedBy
    @ManyToOne
    private User creator;

    @CreatedDate
    private LocalDateTime created;

    @ElementCollection(targetClass = Place.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "place", joinColumns = @JoinColumn(name = "queue_id"))
    private List<Place> places = new ArrayList<>();

    public Place findPlaceByUser(User user) {
        for (Place place: this.places) {
            if (place.getUser() == user)
                return place;
        }
        return null;
    }

    public boolean isFree (int index) {
        for (Place place: this.places) {
            if (place.getIndex() == index)
                return false;
        }
        return true;
    }

    public Queue() {
        this.created = LocalDateTime.now();
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

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
