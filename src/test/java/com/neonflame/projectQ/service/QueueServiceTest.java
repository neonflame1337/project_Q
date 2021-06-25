package com.neonflame.projectQ.service;

import com.neonflame.projectQ.model.Queue;
import com.neonflame.projectQ.model.User;
import com.neonflame.projectQ.repository.QueueRepo;
import com.neonflame.projectQ.repository.UserRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class QueueServiceTest {
    @Autowired
    private QueueService queueService;

    @MockBean
    private QueueRepo queueRepo;
    @MockBean
    private UserService userService;

    private static User user;
    private static User anotherUser;
    private static Queue queue;

    @BeforeAll
    static void setUp() {
        user = new User();
        user.setUsername("testUser");

        anotherUser = new User();
        user.setUsername("anotherUser");

        queue = new Queue();
        queue.setId(1337L);
        queue.setSize(15);
        queue.setCreator(user);
    }

    @Test
    void create() {
        queueService.create(user.getUsername(), queue.getSize());
        verify(queueRepo, times(1)).save(ArgumentMatchers.any(Queue.class));
    }

    @Test
    void createFailInvalidSize() {
        assertThrows(RuntimeException.class, () -> {
            queueService.create(user.getUsername(), 0);
        });
        verify(queueRepo, times(0)).save(ArgumentMatchers.any(Queue.class));
    }

    @Test
    void delete() {
        doReturn(Optional.of(queue))
                .when(queueRepo)
                .findById(queue.getId());

        queueService.delete(queue.getId(), user.getUsername());
        verify(queueRepo, times(1)).delete(ArgumentMatchers.any(Queue.class));
    }

    @Test
    void deleteFailUserIsNotCreator() {
        doReturn(Optional.of(queue))
                .when(queueRepo)
                .findById(queue.getId());

        doReturn(anotherUser)
                .when(userService)
                .findUser(anotherUser.getUsername());

        assertThrows(RuntimeException.class, () -> {
            queueService.delete(queue.getId(), anotherUser.getUsername());
        });
        verify(queueRepo, times(0)).delete(ArgumentMatchers.any(Queue.class));

    }
}