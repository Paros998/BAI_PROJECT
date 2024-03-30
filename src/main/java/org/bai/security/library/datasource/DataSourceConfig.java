package org.bai.security.library.datasource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Singleton
public class DataSourceConfig {
    public static final String UNIT_NAME = "default";
    private final EntityManagerFactory emf;

    public DataSourceConfig() {
        this.emf = Persistence.createEntityManagerFactory(UNIT_NAME);
    }

    @Produces
    @RequestScoped
    @Default
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }
}