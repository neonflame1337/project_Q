package com.neonflame.projectQ.dto.queue;

import lombok.Data;

@Data
public class QueueActionResponse {
    private Long queueId;
    private String username;
    private String action;
}
