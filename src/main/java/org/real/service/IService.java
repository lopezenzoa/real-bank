package org.real.service;

import java.util.List;
import java.util.Optional;

// the following methods define a basic CRUD behaviour over the entity
// the returns type are not wrapped wit optionals because the service is in charge of handling exceptions
public interface IService<T> {
    T getById(int id);
    List<T> getAll();
    T save(T type);
    T update(T newType);
    boolean deleteById(int id);
}
