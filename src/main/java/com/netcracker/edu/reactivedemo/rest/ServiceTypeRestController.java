package com.netcracker.edu.reactivedemo.rest;


import com.netcracker.edu.reactivedemo.dao.ServiceTypeDto;
import com.netcracker.edu.reactivedemo.mapper.ServiceMapper;
import com.netcracker.edu.reactivedemo.mapper.UserMapper;
import com.netcracker.edu.reactivedemo.repo.ServiceTypeRepository;
import com.netcracker.edu.reactivedemo.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reactive/v1/service")
public class ServiceTypeRestController {

    private final ServiceTypeRepository repository;

    private final ServiceMapper mapper;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @GetMapping
    public Flux<ServiceTypeDto> getAll() {
        return repository.findAll().map(mapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ServiceTypeDto>> gretService(@PathVariable("id") UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userName}")
    public Flux<ServiceTypeDto> getAllServicesByUser(@PathVariable("userName") String userName) {
        log.info("Get All Services by user {}", userName);
        return userRepository.getUserByUserName(userName)
                .map(userMapper::toDto)
                .flatMapMany(user -> repository.getServiceTypeForUser(user.getId()))
                .map(mapper::toDto);
    }


    @PutMapping("/{id}")
    public Mono<ResponseEntity<ServiceTypeDto>> updateService(@PathVariable("id") UUID id, @RequestBody ServiceTypeDto serviceTypeDto) {
        return repository.findById(id)
                .map(serviceType -> {
                    serviceType.setName(serviceTypeDto.getName());
                    return serviceType;
                })
                .flatMap(repository::save)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ServiceTypeDto> addService(@RequestBody ServiceTypeDto serviceTypeDto) {
        return repository.save(mapper.toEntity(serviceTypeDto)).map(mapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteService(@PathVariable("id") UUID id) {
        return repository.findById(id).flatMap(repository::delete);
    }


}
