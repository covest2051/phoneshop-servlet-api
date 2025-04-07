package com.es.phoneshop.dao;

import com.es.phoneshop.model.GenericEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ArrayListGenericDao<T extends GenericEntity & Copyable<T>> implements GenericDao<T> {
    protected AtomicLong entityId = new AtomicLong(1);
    protected List<T> entities;
    protected final ReadWriteLock lock = new ReentrantReadWriteLock();

    public ArrayListGenericDao() {
        this.entities = new ArrayList<>();
    }

    @Override
    public Optional<T> getById(Long id) {
        lock.readLock().lock();
        try {
            return entities.stream()
                    .filter(entity -> id.equals(entity.getId()))
                    .findAny()
                    .map(T::copy);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void save(T entity) {
        lock.writeLock().lock();
        try {
            if (entity.getId() != null) {
                entities.set(entities.indexOf(entity), entity);
            } else {
                entity.setId(entityId.incrementAndGet());
                entities.add(entity);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
