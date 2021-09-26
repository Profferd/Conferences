package com.hrushko.validate;

public interface Validator<T> {
    String isValid(T entity);
}
