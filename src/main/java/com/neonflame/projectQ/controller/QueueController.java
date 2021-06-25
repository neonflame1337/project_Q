package com.neonflame.projectQ.controller;

import com.neonflame.projectQ.dto.queue.QueueActionResponse;
import com.neonflame.projectQ.dto.queue.QueueAdvancedDto;
import com.neonflame.projectQ.dto.queue.QueueBasicDto;
import com.neonflame.projectQ.dto.queue.QueueTakeResponseDto;
import com.neonflame.projectQ.model.Queue;
import com.neonflame.projectQ.service.QueueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping()
    public ResponseEntity<List<QueueBasicDto>> showAll() {
        List<Queue> queues = queueService.findAllQueues();
        List<QueueBasicDto> queueBasicDtos = queues.stream().map(QueueBasicDto::new).collect(Collectors.toList());
        return new ResponseEntity<>(queueBasicDtos, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<QueueAdvancedDto> show(@PathVariable("id") Long id) {
        Queue queue = queueService.findQueue(id);
        return new ResponseEntity<>(new QueueAdvancedDto(queue), HttpStatus.OK);
    }

    @GetMapping("create")
    public ResponseEntity<QueueActionResponse> create(Principal principal, @RequestParam("size") int size) {
        QueueActionResponse response = new QueueActionResponse();
        response.setUsername(principal.getName());
        response.setAction("created");

        Long queueId = queueService.create(principal.getName(), size).getId();
        response.setQueueId(queueId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<QueueActionResponse> delete (Principal principal, @PathVariable("id") Long id) {
        queueService.delete(id, principal.getName());

        QueueActionResponse response = new QueueActionResponse();
        response.setQueueId(id);
        response.setUsername(principal.getName());
        response.setAction("deleted");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}/activate")
    public ResponseEntity<QueueActionResponse> activate(Principal principal, @PathVariable("id") Long id) {
        QueueActionResponse response = new QueueActionResponse();
        response.setUsername(principal.getName());
        response.setQueueId(id);

        if (queueService.activate(id, principal.getName()))
            response.setAction("activated");
        else
            response.setAction("deactivated");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}/take")
    public ResponseEntity<QueueTakeResponseDto> takePlace(Principal principal,
                                                          @PathVariable("id") Long queueId,
                                                          @RequestParam("index") int index) {
        QueueTakeResponseDto response = new QueueTakeResponseDto();
        response.setQueueId(queueId);
        response.setPlaceNumber(index);
        response.setUsername(principal.getName());

        if (queueService.takePlace(principal.getName(), queueId, index))
            response.setAction("taken");
        else
            response.setAction("vacated");

            return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
