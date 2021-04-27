package com.umar.apps.hibernate.infra.dao;

import javax.persistence.EntityManager;
import java.util.function.Function;

/**
 * JPA transaction function
 *
 * @param <T> function result
 */
@FunctionalInterface
public interface JPATransactionFunction<T> extends Function<EntityManager, T> {
    /**
     * Before transaction completion function
     */
    default void beforeTransactionCompletion() {

    }

    /**
     * After transaction completion function
     */
    default void afterTransactionCompletion() {

    }
}
