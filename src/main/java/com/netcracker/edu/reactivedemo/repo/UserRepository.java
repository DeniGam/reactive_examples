package com.netcracker.edu.reactivedemo.repo;

import com.netcracker.edu.reactivedemo.entity.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

    @Modifying
    @Query("INSERT INTO user_service (user_id, service_id) values (:userId, :serviceId)")
    Mono<Void> addServiceToUser(UUID userId, UUID serviceId);

    Mono<User> getUserByUserName(String userName);
}
