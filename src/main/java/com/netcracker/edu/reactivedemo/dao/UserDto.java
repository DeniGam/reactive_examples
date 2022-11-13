package com.netcracker.edu.reactivedemo.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private Date createdWhen;
    private Date updatedWhen;
}
