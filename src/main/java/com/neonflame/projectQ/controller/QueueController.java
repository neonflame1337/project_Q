package com.neonflame.projectQ.controller;

import com.neonflame.projectQ.model.Queue;
import com.neonflame.projectQ.service.QueueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api/v1/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping()
    public ResponseEntity<Collection<Queue>> showAll() {
        List<Queue> queues = queueService.findAllQueues();
        return new ResponseEntity<>(queues, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Queue> show(@PathVariable("id") Long id) {
        Queue queue = queueService.findQueue(id);
        return new ResponseEntity<>(queue, HttpStatus.OK);
    }

    @GetMapping("create")
    public ResponseEntity<?> create(Principal principal, @RequestParam("size") int size) {
        queueService.create(principal.getName(), size);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}/take")
    public ResponseEntity<?> takePlace(Principal principal,
                                       @PathVariable("id") Long queueId,
                                       @RequestParam("index") int index) {
        queueService.takePlace(principal.getName(), queueId, index);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
