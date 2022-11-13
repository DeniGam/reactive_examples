package com.netcracker.edu.reactivedemo.mapper;

import com.netcracker.edu.reactivedemo.dao.ServiceTypeDto;
import com.netcracker.edu.reactivedemo.entity.ServiceType;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper implements Mapper<ServiceTypeDto, ServiceType> {
    @Override
    public ServiceTypeDto toDto(ServiceType entity) {
        return ServiceTypeDto.builder().id(entity.getId()).name(entity.getName()).build();
    }

    @Override
    public ServiceType toEntity(ServiceTypeDto dao) {
        return ServiceType.builder().id(dao.getId()).name(dao.getName()).build();
    }
}
