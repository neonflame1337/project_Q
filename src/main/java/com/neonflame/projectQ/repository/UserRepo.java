package com.neonflame.projectQ.repository;

import com.neonflame.projectQ.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public User findByUsername (String username);
    public User findByEmail (String email);
}
