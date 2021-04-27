package com.umar.apps.hibernate.infra.dao.core;

import com.umar.apps.hibernate.common.WorkItem;
import com.umar.apps.hibernate.infra.dao.GenericDao;
import com.umar.apps.hibernate.infra.dao.JPATransactionFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public abstract class GenericJpaDao<MODEL extends WorkItem<ID>, ID extends Serializable> implements GenericDao<MODEL, ID> {

    private static final Logger log = LogManager.getLogger(GenericJpaDao.class);
    private final Class<MODEL> persistentClass;
    protected final EntityManagerFactory emf;

    public GenericJpaDao(final Class<MODEL> persistentClass, final String persistenceUnit) {
        this.persistentClass = persistentClass;
        this.emf = Persistence.createEntityManagerFactory(persistenceUnit);
    }

    public void executeInTransaction(Consumer<EntityManager> consumer) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        if(!transaction.isActive()) {
            transaction.begin();
        }
        consumer.accept(em);
        transaction.commit();
        em.close();
    }

    @Override
    public Optional<MODEL> findById(ID id) {
        log.debug("findById {} ", id);
        AtomicReference<MODEL> entity = new AtomicReference<>();
        executeInTransaction(entityManager -> {
            entity.set(entityManager.find(persistentClass, id));
        });
        return Optional.ofNullable(entity.get());
    }

    @Override
    public MODEL save(MODEL model) {
        log.debug("Persisting {} ", model.getClass());
        executeInTransaction(entityManager -> {
            entityManager.persist(model);
            entityManager.flush();
        });
        return model;
    }

    @Override
    public MODEL merge(MODEL model) {
        log.debug("Merging {} ", model.getClass());
        executeInTransaction( entityManager -> {
            entityManager.merge(model);
            entityManager.flush();
        });
        return model;
    }

    @Override
    public void makeTransient(MODEL entity) {
        log.debug("Making Transient {} ", entity.getClass());
        emf.createEntityManager().detach(entity);
    }

    @Override
    public void closeEntityManagerFactory() {
        log.debug("Closing Entity Manager Factory of PersistenceUnit");
        if(null != emf && emf.isOpen()) {
            emf.close();
        }
    }

    @Override
    public EntityManagerFactory getEMF() {
        return emf;
    }

    public void doInJPA(Consumer<EntityManager> consumer, GenericDao<MODEL, ID> dao){
        EntityManager entityManager = dao.getEMF().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        if(!transaction.isActive()) {
            transaction.begin();
        }
        consumer.accept(entityManager);
        transaction.commit();
        entityManager.close();
    }

    public <T> T doInJPA(JPATransactionFunction<T> function) {
        T result;
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        try {
            entityManager = getEMF().createEntityManager();
            function.beforeTransactionCompletion();
            txn = entityManager.getTransaction();
            txn.begin();
            result = function.apply(entityManager);
            if ( !txn.getRollbackOnly() ) {
                txn.commit();
            }
            else {
                try {
                    txn.rollback();
                }
                catch (Exception e) {
                    log.error( "Rollback failure", e );
                }
            }
        } catch (Throwable t) {
            if ( txn != null && txn.isActive() ) {
                try {
                    txn.rollback();
                }
                catch (Exception e) {
                    log.error( "Rollback failure", e );
                }
            }
            throw t;
        } finally {
            function.afterTransactionCompletion();
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return result;
    }
}

/*public static <T> T doInJPA(
            Supplier<EntityManagerFactory> factorySupplier,
            JPATransactionFunction<T> function,
            Map properties) {
        T result = null;
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        try {
            entityManager = properties == null ?
                    factorySupplier.get().createEntityManager():
                    factorySupplier.get().createEntityManager(properties);
            function.beforeTransactionCompletion();
            txn = entityManager.getTransaction();
            txn.begin();
            result = function.apply( entityManager );
            txn.commit();
        }
        catch ( Throwable e ) {
            if ( txn != null && txn.isActive() ) {
                txn.rollback();
            }
            throw e;
        }
        finally {
            function.afterTransactionCompletion();
            if ( entityManager != null ) {
                entityManager.close();
            }
        }
        return result;
    }*/

    /*protected void doInJPA(JPATransactionVoidFunction function) {
        EntityManager entityManager = null;
        EntityTransaction txn = null;
        try {
            entityManager = entityManagerFactory().createEntityManager();
            function.beforeTransactionCompletion();
            txn = entityManager.getTransaction();
            txn.begin();
            function.accept(entityManager);
            if ( !txn.getRollbackOnly() ) {
                txn.commit();
            }
            else {
                try {
                    txn.rollback();
                }
                catch (Exception e) {
                    LOGGER.error( "Rollback failure", e );
                }
            }
        } catch (Throwable t) {
            if ( txn != null && txn.isActive() ) {
                try {
                    txn.rollback();
                }
                catch (Exception e) {
                    LOGGER.error( "Rollback failure", e );
                }
            }
            throw t;
        } finally {
            function.afterTransactionCompletion();
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    protected <T> T doInJDBC(ConnectionCallable<T> callable) {
        AtomicReference<T> result = new AtomicReference<>();
        Session session = null;
        Transaction txn = null;
        try {
            session = sessionFactory().openSession();
            txn = session.beginTransaction();
            session.doWork(connection -> {
                result.set(callable.execute(connection));
            });
            if ( !txn.getRollbackOnly() ) {
                txn.commit();
            }
            else {
                try {
                    txn.rollback();
                }
                catch (Exception e) {
                    LOGGER.error( "Rollback failure", e );
                }
            }
        } catch (Throwable t) {
            if ( txn != null && txn.isActive() ) {
                try {
                    txn.rollback();
                }
                catch (Exception e) {
                    LOGGER.error( "Rollback failure", e );
                }
            }
            throw t;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result.get();
    }

    protected void doInJDBC(ConnectionVoidCallable callable) {
        Session session = null;
        Transaction txn = null;
        try {
            session = sessionFactory().openSession();
            txn = session.beginTransaction();
            session.doWork(callable::execute);
            if ( !txn.getRollbackOnly() ) {
                txn.commit();
            }
            else {
                try {
                    txn.rollback();
                }
                catch (Exception e) {
                    LOGGER.error( "Rollback failure", e );
                }
            }
        } catch (Throwable t) {
            if ( txn != null && txn.isActive() ) {
                try {
                    txn.rollback();
                }
                catch (Exception e) {
                    LOGGER.error( "Rollback failure", e );
                }
            }
            throw t;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }*/


