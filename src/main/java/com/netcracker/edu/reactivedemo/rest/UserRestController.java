package com.netcracker.edu.reactivedemo.rest;

import com.netcracker.edu.reactivedemo.dao.UserDto;
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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reactive/v1/user")
public class UserRestController {

    private final UserRepository repository;

    private final UserMapper userMapper;

    private final ServiceMapper serviceMapper;


    private final ServiceTypeRepository serviceTypeRepository;

    @GetMapping
    public Flux<UserDto> getAllUser() {
        return repository
                .findAll()
                .map(userMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> getOneUser(@PathVariable("id") UUID id) {
        log.info("Get User{}", id);
        return repository.findById(id) //Get one user from DB
                .map(userMapper::toDto) //Convert user entity to DTO
                .flatMap(userDto ->
                        serviceTypeRepository.getServiceTypeForUser(userDto.getId()) //get all services by user
                                .map(serviceMapper::toDto) //Convert service to DTO
                                .collect(Collectors.toCollection(userDto::getServices)) //collect services to collection in user DTO
                                .thenReturn(userDto))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("id") UUID id) {
        log.info("Get User{}", id);
        return repository.findById(id)
                .flatMap(repository::delete)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> addUser(@RequestBody UserDto user) {
        log.info("Add user: {}", user.getUserName());
        user.setCreatedWhen(new Date());
        return repository.save(userMapper.toEntity(user))
                .map(userMapper::toDto)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> updateUser(@RequestBody UserDto userDto, @PathVariable("id") UUID id) {
        log.info("Update User{}", id);
        return repository.findById(id)
                .map(user -> {
                    user.setUserName(userDto.getUserName());
                    user.setEmail(userDto.getEmail());
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(user.getLastName());
                    user.setUpdatedWhen(LocalDateTime.now());
                    return user;
                }).flatMap(repository::save)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/service")
    public Mono<Void> addServiceToUser(@PathVariable("userId") UUID userId, @RequestBody String serviceName) {
        return serviceTypeRepository.getServiceTypeByName(serviceName)
                .flatMap(serviceType -> repository.addServiceToUser(userId, serviceType.getId()));
    }


}
