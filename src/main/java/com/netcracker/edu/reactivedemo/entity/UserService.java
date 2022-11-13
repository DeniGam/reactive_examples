package com.netcracker.edu.reactivedemo.entity;

import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("user_service")
public class UserService {
    UUID userId;
    UUID serviceId;
}
