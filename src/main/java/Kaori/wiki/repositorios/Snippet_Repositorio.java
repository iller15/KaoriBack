package Kaori.wiki.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import Kaori.wiki.entidades.Snippet;

public interface Snippet_Repositorio extends CrudRepository<Snippet, String>{
	@Override
	List<Snippet> findAll();
	List<Snippet> findAllByCapitulo_idCapitulo(String idCapitulo);
}
