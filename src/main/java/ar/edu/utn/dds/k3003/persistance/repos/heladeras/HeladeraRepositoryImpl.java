package ar.edu.utn.dds.k3003.persistance.repos.heladeras;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.errors.ErrorTipo;
import ar.edu.utn.dds.k3003.model.errors.OperacionInvalidaException;
import ar.edu.utn.dds.k3003.model.estados.Estado;
import ar.edu.utn.dds.k3003.persistance.utils.PersistenceUtils;
import org.hibernate.boot.model.naming.IllegalIdentifierException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;


public class HeladeraRepositoryImpl implements HeladeraRepository {

    private final static EntityManagerFactory emf =  PersistenceUtils.createEntityManagerFactory();

    @Override
    public void save(Heladera heladera) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            if (heladera.getHeladeraId() == null) {
                em.persist(heladera);
            } else {
                if (em.find(Heladera.class, heladera.getHeladeraId()) == null) {
                    throw new IllegalArgumentException("No se puede almacenar una heladera con id asignado por el usuario ");
                } else {
                    throw new IllegalIdentifierException("Ya existe la heladera con el id: " + heladera.getHeladeraId());
                }
            }
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
    public void modify(Heladera heladera) {
        if (heladera.getHeladeraId() == null) {
            throw new IllegalArgumentException("La heladera debe tener un ID para ser modificada.");
        }

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Heladera existingHeladera = em.find(Heladera.class, heladera.getHeladeraId());
            if (existingHeladera == null) {
                throw new NoResultException("No se encontró una heladera con ID: " + heladera.getHeladeraId());
            }

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
    public void clear() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Heladera").executeUpdate();
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

    @Override
    public Heladera getById(Integer heladeraId) {
        EntityManager em = emf.createEntityManager();
        try {
            Heladera heladera = em.find(Heladera.class, heladeraId);
            if (heladera == null) {
                throw new NoResultException("No se encontró una heladera con ID: " + heladeraId);
            }
            return heladera;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Integer getCantidadViandas(Integer heladeraId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT h.cantidadDeViandas FROM Heladera h WHERE h.id = :heladeraId", Integer.class)
                    .setParameter("heladeraId", heladeraId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("No existe una heladera con ese id: " + heladeraId);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void open(Integer heladeraId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Heladera heladera = em.find(Heladera.class, heladeraId);
            if (heladera == null) {
                throw new NoResultException("No existe una heladera con el id: " + heladeraId);
            }

            if (heladera.getEstadoApertura() == Estado.ABIERTA){
                throw new OperacionInvalidaException(ErrorTipo.HELADERA_ABIERTA, "La heladera ya se encuentra abierta");
            }

            heladera.setEstadoApertura(Estado.ABIERTA);

            em.getTransaction().commit();
        } catch (NoResultException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al abrir la heladera", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void close(Integer heladeraId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Heladera heladera = em.find(Heladera.class, heladeraId);
            if (heladera == null) {
                throw new NoResultException("No existe una heladera con el id: " + heladeraId);
            }

            if (heladera.getEstadoApertura() == Estado.CERRADA){
                throw new OperacionInvalidaException(ErrorTipo.HELADERA_CERRADA, "La heladera ya se encuentra cerrada");
            }

            heladera.setEstadoApertura(Estado.CERRADA);

            em.getTransaction().commit();
        } catch (NoResultException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al cerrar la heladera", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

}
