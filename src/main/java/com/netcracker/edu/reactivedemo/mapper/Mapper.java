package com.netcracker.edu.reactivedemo.mapper;

public interface Mapper<D, E> {
    D toDto(E entity);

    E toEntity(D dao);
}
