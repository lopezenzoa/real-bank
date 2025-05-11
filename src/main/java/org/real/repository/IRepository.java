package org.real.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    Optional<T> findById(int id);
    List<T> findAll();
    Optional<T> save(T type);
    boolean update(T newType);
    boolean deleteById(int id);
}
