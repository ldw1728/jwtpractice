package com.example.demojwt.repository;

import com.example.demojwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
