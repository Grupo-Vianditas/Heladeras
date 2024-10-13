package ar.edu.utn.dds.k3003.persistance.repos.temperaturas;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.Temperatura;
import ar.edu.utn.dds.k3003.persistance.utils.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.List;

public class TemperaturaRepositoryImpl implements TemperaturaRepository {

    private final static EntityManagerFactory emf =  PersistenceUtils.createEntityManagerFactory();

    @Override
    public void save(Temperatura temperatura) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Heladera heladera = em.find(Heladera.class, temperatura.getHeladeraId());
            if (heladera == null) {
                throw new NoResultException("No se encontró la heladera con ID: " + temperatura.getHeladeraId());
            }

            heladera.agregarTemperatura(temperatura);
            em.persist(temperatura);
            em.merge(heladera);
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

    public List<Temperatura> getTemperaturasDeHeladera(Integer heladeraId) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Temperatura> temperaturas = em.createQuery(
                            "SELECT t FROM Temperatura t WHERE t.heladera.id = :heladeraId ORDER BY t.fechaMedicion DESC", Temperatura.class)
                    .setParameter("heladeraId", heladeraId)
                    .getResultList();

            return temperaturas;
        }catch(NoResultException e){
            throw new NoResultException("No se encontraron temperaturas para la heladera con Id: " + heladeraId);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void clear() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Temperatura").executeUpdate();
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
