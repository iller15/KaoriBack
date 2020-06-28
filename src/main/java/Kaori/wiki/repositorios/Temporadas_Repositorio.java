package Kaori.wiki.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import Kaori.wiki.entidades.Temporada;

public interface Temporadas_Repositorio extends CrudRepository<Temporada, String>{
	@Override
	List<Temporada> findAll();
	List<Temporada> findAllBySerie_idSerie(String idSerie);
}
