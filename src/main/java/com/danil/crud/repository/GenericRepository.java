package com.danil.crud.repository;

import java.util.List;

public interface GenericRepository<T, ID> {
    T create(T t);

    List<T> getAll();

    T getById(ID id);

    T update(T t);

    void deleteById(ID id);
}
