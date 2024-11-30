package ar.edu.utn.dds.k3003.persistance.repos.incidentes;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.Incidente;
import ar.edu.utn.dds.k3003.model.Temperatura;
import ar.edu.utn.dds.k3003.persistance.utils.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.List;

public class IncidenteRepositoryImpl implements IncidenteRepository{

    private final static EntityManagerFactory emf =  PersistenceUtils.createEntityManagerFactory();

    @Override
    public void save(Incidente incidente) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Heladera heladera = em.find(Heladera.class, incidente.getHeladeraId());
            if (heladera == null) {
                throw new NoResultException("No se encontró la heladera con ID: " + incidente.getHeladeraId());
            }

            heladera.agregarIncidente(incidente);

            em.persist(incidente);
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

    @Override
    public Incidente getById(Integer incidenteId){
        EntityManager em = emf.createEntityManager();
        try {
            Incidente incidente = em.find(Incidente.class, incidenteId);
                if (incidente == null) {
                    throw new NoResultException("No se encontró un incidente con ID: " + incidenteId);
                }
                return incidente;
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }

    @Override
    public List<Incidente> getIncidentesDeHeladera(Integer heladeraId) {
        EntityManager em = emf.createEntityManager();
        try {

            return em.createQuery(
                            "SELECT t FROM Incidente t WHERE t.heladera.id = :heladeraId ORDER BY t.timestamp DESC", Incidente.class)
                    .setParameter("heladeraId", heladeraId)
                    .getResultList();
        }catch(NoResultException e){
            throw new NoResultException("No se encontraron incidentes para la heladera con Id: " + heladeraId);
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
            em.createQuery("DELETE FROM Incidente").executeUpdate();
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