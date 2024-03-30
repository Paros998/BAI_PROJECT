package org.bai.security.library.datasource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class DataSourceConfig {
    private final EntityManagerFactory emf;

    public DataSourceConfig() {
        emf = Persistence.createEntityManagerFactory("default");
    }

    public EntityManager getDefaultEM() {
        return emf.createEntityManager();
    }
}