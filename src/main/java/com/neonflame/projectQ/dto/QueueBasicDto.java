package com.neonflame.projectQ.dto;

import com.neonflame.projectQ.model.Queue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.ListJoin;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueBasicDto {
    private Long id;
    private int size;
    private boolean active;
    private String creator;

    public  QueueBasicDto (Queue queue) {
        this.id = queue.getId();
        this.size = queue.getSize();
        this.active = queue.isActive();
        this.creator = queue.getCreator().getUsername();
    }
}
