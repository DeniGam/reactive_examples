package com.netcracker.edu.reactivedemo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_account")
public class User {
    @Id
    private UUID id;
    @Column("user_name")
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdWhen;
    private LocalDateTime updatedWhen;
}
