package com.example.demojwt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String roles; // User, Admin

    // role 이 복수일경우 필
    public List<String> getRoleList(){
        if (this.roles.length() > 0) {
            return Arrays.asList(roles.split(","));
        }

        return new ArrayList<>();

    }

}
