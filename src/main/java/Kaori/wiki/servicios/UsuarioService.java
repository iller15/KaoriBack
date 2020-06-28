package Kaori.wiki.servicios;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Kaori.wiki.entidades.Usuario;
import Kaori.wiki.entidades.Usuariox;
import Kaori.wiki.repositorios.RepositorioUsuarioxDao;


@Service //valida y carga los datos del usuario, para que sea configurado en Spring Security Config
public class UsuarioService implements IUsuarioService, UserDetailsService{
	
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	
	
	
	@Autowired
	private RepositorioUsuarioxDao usuarioDao;
	
	@Override
	@Transactional(readOnly=true)
	public Usuariox findByUsername(String username) {
		return usuarioDao.findByUsername(username);
	}
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuariox usuario = usuarioDao.findByUsername(username);
		if(usuario == null) {
			logger.error("Error en el login: no existe el usuario '"+username+"' en el sistema!");
			throw new UsernameNotFoundException("Error en el login: no existe el usuario '"+username+"' en el sistema!");
		}
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> logger.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}
	
	
	
	
}

