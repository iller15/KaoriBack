package Kaori.wiki.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import Kaori.wiki.entidades.Articulo;

public interface Articulo_Repositorio  extends CrudRepository<Articulo, Long>{
	@Override
    List<Articulo> findAll();
	
}
