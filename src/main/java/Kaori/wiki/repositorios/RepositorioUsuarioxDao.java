package Kaori.wiki.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import Kaori.wiki.entidades.Usuario;
import Kaori.wiki.entidades.Usuariox;


//Repositorio
public interface RepositorioUsuarioxDao extends CrudRepository<Usuariox, Long>{
	
	public Usuariox findByUsername(String username);
	
	@Override
    List<Usuariox> findAll();
	
	@Query("select u from Usuariox u where u.username=?1")
	public Usuariox findByUsername2(String username);

}
