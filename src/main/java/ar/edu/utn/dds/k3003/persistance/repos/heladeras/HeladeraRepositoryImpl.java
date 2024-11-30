package ar.edu.utn.dds.k3003.persistance.repos.heladeras;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.heladera.HabilitacionEnum;
import ar.edu.utn.dds.k3003.persistance.utils.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;


public class HeladeraRepositoryImpl implements HeladeraRepository {

    private final static EntityManagerFactory emf =  PersistenceUtils.createEntityManagerFactory();

    @Override
    public void save(Heladera heladera) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            if (heladera.getHeladeraId() == null) {
                Heladera existingHeladera = em.createQuery("SELECT h FROM Heladera h WHERE h.nombre = :nombre", Heladera.class)
                        .setParameter("nombre", heladera.getNombre())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);

                if (existingHeladera != null) {
                    throw new IllegalArgumentException("Ya existe una heladera con el nombre: " + heladera.getNombre());
                }

                em.persist(heladera);
            } else {
                throw new IllegalArgumentException("No podes asignar un id.");
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

    public List<Heladera> getHeladerasDesconectadas(int minutes) {
        EntityManager em = emf.createEntityManager();
        try {
            LocalDateTime limite = LocalDateTime.now().minusMinutes(minutes);

            return em.createQuery("SELECT h FROM Heladera h WHERE h.horarioultimatemperatura < :limite AND h.habilitacion = :habilitado", Heladera.class)
                    .setParameter("limite", limite)
                    .setParameter("habilitado", HabilitacionEnum.HABILITADA)
                    .getResultList();
        } catch (NoResultException e) {
            throw new NoResultException("No se encontraron heladeras habilitadas y desconectadas en los últimos " + minutes + " minutos.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }


    public List<Heladera> getHeladerasTemperaturas(int minutes) {
        EntityManager em = emf.createEntityManager();
        try {
            LocalDateTime limite = LocalDateTime.now().minusMinutes(minutes);

            return em.createQuery(
                            "SELECT h FROM Heladera h " +
                                    "WHERE (h.ultimaTemperatura < h.minimoTemperatura OR h.ultimaTemperatura > h.maximoTemperatura) " +
                                    "AND h.horarioultimatemperatura < :limite " +
                                    "AND h.habilitacion = :habilitado", Heladera.class)
                    .setParameter("limite", limite)
                    .setParameter("habilitado", HabilitacionEnum.HABILITADA)
                    .getResultList();
        } catch (NoResultException e) {
            throw new NoResultException("No se encontraron heladeras fuera del rango de temperatura en los últimos " + minutes + " minutos.");
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }


}
