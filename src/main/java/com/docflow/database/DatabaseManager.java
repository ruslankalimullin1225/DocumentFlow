package com.docflow.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseManager {

    private static DatabaseManager instance;
    private final EntityManagerFactory entityManagerFactory;

    private DatabaseManager() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("DocumentFlowPU");
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    public static EntityManager createTestEntityManager() {
        EntityManagerFactory testFactory = Persistence.createEntityManagerFactory("DocumentFlowTestPU");
        return testFactory.createEntityManager();
    }
}
