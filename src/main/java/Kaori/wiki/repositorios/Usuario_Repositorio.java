package Kaori.wiki.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import Kaori.wiki.entidades.Usuario;

public interface Usuario_Repositorio extends CrudRepository<Usuario, Long> {
	@Override
    List<Usuario> findAll();
}
