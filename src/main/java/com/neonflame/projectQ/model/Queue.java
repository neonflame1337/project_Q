package com.neonflame.projectQ.model;

import com.neonflame.projectQ.excrptions.queue.QueueIndexErrorException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
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
        if (index < 1 || index > this.size)
            throw new QueueIndexErrorException("Index is out of range [1, " + this.size + "]");

        for (Place place: this.places) {
            if (place.getIndex() == index)
                return false;
        }
        return true;
    }
}
