package org.real.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    Optional<T> findById(int id);
    List<T> findAll();
    int save(T type);
    void update(T newType);
    void deleteById(int id);
}
