package com.hrushko.util.repair;

public interface Recover<T> {
    T recover(T entity);
}
