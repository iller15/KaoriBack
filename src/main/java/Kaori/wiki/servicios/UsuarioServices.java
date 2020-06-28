package Kaori.wiki.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import Kaori.wiki.entidades.Role;
import Kaori.wiki.entidades.Usuariox;
import Kaori.wiki.repositorios.RepositorioUsuarioxDao;
import Kaori.wiki.repositorios.Roles_Repositorio;

@Service
public class UsuarioServices {
	
	@Autowired
	private RepositorioUsuarioxDao daoUsuario;
	@Autowired
	private Roles_Repositorio roles_Repositorio;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public Usuariox registrar(Usuariox usuariox) {
		usuariox.setPassword(passwordEncoder.encode(usuariox.getPassword()));
		List<Role> roles = usuariox.getRoles();
		Role role = roles_Repositorio.findById2((long)1);
		roles.add(role); 
		return daoUsuario.save(usuariox);
	}

	

}
