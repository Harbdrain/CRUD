package com.danil.crud.repository;

import java.util.HashMap;

public interface GenericRepository<T, ID> {
    void save(T t);
    HashMap<ID, T> getAll();
    void update(T t);
    void deleteById(ID id);
}
