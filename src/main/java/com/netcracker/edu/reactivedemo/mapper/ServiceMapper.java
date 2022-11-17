package com.netcracker.edu.reactivedemo.mapper;

import com.netcracker.edu.reactivedemo.dao.ServiceTypeDto;
import com.netcracker.edu.reactivedemo.entity.ServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceMapper implements Mapper<ServiceTypeDto, ServiceType> {
    @Override
    public ServiceTypeDto toDto(ServiceType entity) {
        log.info("Convert service {} to dto", entity.getName());
        return ServiceTypeDto.builder().id(entity.getId()).name(entity.getName()).build();
    }

    @Override
    public ServiceType toEntity(ServiceTypeDto dao) {
        log.info("Convert service dto {} to Service", dao.getName());
        return ServiceType.builder().id(dao.getId()).name(dao.getName()).build();
    }
}
