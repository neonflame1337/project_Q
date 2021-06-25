package com.neonflame.projectQ.service;

import com.neonflame.projectQ.model.Queue;
import com.neonflame.projectQ.model.User;
import com.neonflame.projectQ.repository.QueueRepo;
import com.neonflame.projectQ.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QueueServiceTest {
    @Autowired
    private QueueService queueService;

    @MockBean
    private QueueRepo queueRepo;
    @MockBean
    private UserRepo userRepo;

    private static User user;
    private static Queue queue;

    @Test
    void create() {
    }

    @Test
    void delete() {
    }

    @Test
    void activate() {
    }

    @Test
    void takePlace() {
    }
}