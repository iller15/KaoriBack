package Kaori.wiki.servicios;

import Kaori.wiki.entidades.Usuariox;

public interface IUsuarioService {

	public Usuariox findByUsername(String username);
}
