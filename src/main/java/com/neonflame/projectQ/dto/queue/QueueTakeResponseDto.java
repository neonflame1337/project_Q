package com.neonflame.projectQ.dto.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueTakeResponseDto {
    private Long queueId;
    private String username;
    private int placeNumber;
    private String action;
}
