package ar.edu.utn.dds.k3003.persistance;


import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.Temperatura;

public interface HeladerasRepository {

    public Heladera save(Heladera heladera);
    public Heladera getHeladeraById(Integer id);
    public void modifyHeladera(Heladera heladera);
    public void clear();
    public void agregarTemperatura(Temperatura temperatura);
}
