package com.netcracker.edu.reactivedemo.repo;

import com.netcracker.edu.reactivedemo.entity.ServiceType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Repository
public interface ServiceTypeRepository extends ReactiveCrudRepository<ServiceType, UUID> {

    Mono<ServiceType> getServiceTypeByName(String name);

    @Query("SELECT st.* FROM service_type st left join user_service us on st.id = us.service_id where us.user_id = :serviceId")
    Flux<ServiceType> getServiceTypeForUser(UUID userId);
}
