package Kaori.wiki.repositorios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import Kaori.wiki.entidades.Role;


public interface Roles_Repositorio extends CrudRepository<Role, Long>{
	@Query("select r from Role r where r.id=?1")
	public Role findById2(Long id);
	
}
