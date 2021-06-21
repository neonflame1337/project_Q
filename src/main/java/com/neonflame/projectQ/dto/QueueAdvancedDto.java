package com.neonflame.projectQ.dto;

import com.neonflame.projectQ.model.Place;
import com.neonflame.projectQ.model.Queue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QueueAdvancedDto {
    private long id;
    private int size;
    private boolean active;
    private String creator;
    private String created;
    private String[] places;

    public QueueAdvancedDto(Queue queue) {
        this.id = queue.getId();
        this.size = queue.getSize();
        this.active = queue.isActive();
        this.creator = queue.getCreator().getUsername();
        this.created = queue.getCreated().toString();
        this.places = createPlaces(queue.getPlaces());
    }

    private String[] createPlaces(List<Place> places) {
        String[] result = new String[this.size];
        for (int i = 0; i < this.size; i++)
            for (Place place : places)
                if (place.getIndex() == i)
                    result[i] = place.getUser().getUsername();
        return result;
    }
}
