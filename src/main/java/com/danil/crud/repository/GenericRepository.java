package com.danil.crud.repository;

import java.util.HashMap;

public interface GenericRepository<T, ID> {
    void create(T t);
    void createAndUpdateAll(HashMap<ID, T> map);

    HashMap<ID, T> getAll();
    T getById(ID id);

    void update(T t);

    void deleteById(ID id);

    int getMaxId();
}
