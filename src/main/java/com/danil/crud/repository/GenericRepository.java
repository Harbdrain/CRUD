package com.danil.crud.repository;

import java.util.HashMap;

public interface GenericRepository<T, ID> {
    void save(T t);
    void saveAll(HashMap<ID, T> map);

    HashMap<ID, T> getAll();

    HashMap<ID, T> getAllExceptDeleted();
    T getById(ID id);

    void update(T t);

    void deleteById(ID id);

    int getMaxId();
}
