package Kaori.wiki.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import Kaori.wiki.entidades.Capitulo;

public interface Capitulo_Repositorio extends CrudRepository<Capitulo, String>{
  
  List<Capitulo> findAllByTemporada_idTemporada(String idTemporada);
  List<Capitulo> findAllByidCapitulo(String idCapitulo);

}
