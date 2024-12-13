package ar.edu.utn.dds.k3003.persistance.repos.retiros;

import ar.edu.utn.dds.k3003.model.Retiro;
import ar.edu.utn.dds.k3003.persistance.utils.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RetirosRepositoryImpl {

    private final static EntityManagerFactory emf =  PersistenceUtils.createEntityManagerFactory();

    public void save(Retiro retiro) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(retiro);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public Integer getDailyRetirosByHeladeraId(Integer heladeraId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

            Query query = em.createQuery(
                    "SELECT COUNT(r) " +
                            "FROM Retiro r " +
                            "WHERE r.heladeraId = :heladeraId " +
                            "AND r.fecha BETWEEN :startOfDay AND :endOfDay"
            );
            query.setParameter("heladeraId", heladeraId);
            query.setParameter("startOfDay", startOfDay);
            query.setParameter("endOfDay", endOfDay);

            Long count = (Long) query.getSingleResult();

            em.getTransaction().commit();
            return count.intValue();

        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    public void clear() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Retiros").executeUpdate();
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
