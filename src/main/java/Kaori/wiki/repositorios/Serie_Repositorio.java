package Kaori.wiki.repositorios;

import org.springframework.data.repository.CrudRepository;

import Kaori.wiki.entidades.Serie;

public interface Serie_Repositorio extends CrudRepository<Serie, String>{
  Serie findByNombre(String nombre);
}
