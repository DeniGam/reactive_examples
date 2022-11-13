package com.netcracker.edu.reactivedemo.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "service_type")
@Builder
public class ServiceType {
    @Id
    UUID id;
    String name;
}

