package com.netcracker.edu.reactivedemo.mapper;

import com.netcracker.edu.reactivedemo.dao.UserDto;
import com.netcracker.edu.reactivedemo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Date;

@Slf4j
@Component
public class UserMapper implements Mapper<UserDto, User> {

    @Override
    public UserDto toDto(User entity) {
        log.info("Convert user {} to dto", entity.getUserName());
        return UserDto.builder()
                .updatedWhen(entity.getUpdatedWhen() == null ? null : Date.from(entity.getUpdatedWhen().toInstant(ZoneOffset.UTC)))
                .createdWhen(entity.getCreatedWhen() == null ? null : Date.from(entity.getCreatedWhen().toInstant(ZoneOffset.UTC)))
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .id(entity.getId())
                .lastName(entity.getLastName())
                .userName(entity.getUserName())
                .build();
    }

    @Override
    public User toEntity(UserDto dao) {
        log.info("Convert Dto to user {}", dao.getUserName());
        return User.builder()
                .id(dao.getId())
                .userName(dao.getUserName())
                .lastName(dao.getLastName())
                .firstName(dao.getFirstName())
                .createdWhen(dao.getCreatedWhen() == null ? null : dao.getCreatedWhen().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime())
                .updatedWhen(dao.getUpdatedWhen() == null ? null : dao.getUpdatedWhen().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime())
                .email(dao.getEmail())
                .build();
    }
}
