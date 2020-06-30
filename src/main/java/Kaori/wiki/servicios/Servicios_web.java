package Kaori.wiki.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import Kaori.wiki.entidades.Articulo;
import Kaori.wiki.entidades.AvanceSerie;
import Kaori.wiki.entidades.Capitulo;
import Kaori.wiki.entidades.Serie;
import Kaori.wiki.entidades.Snippet;
import Kaori.wiki.entidades.Temporada;
import Kaori.wiki.entidades.Usuariox;
import Kaori.wiki.repositorios.Articulo_Repositorio;
import Kaori.wiki.repositorios.AvanceSerie_Repositorio;
import Kaori.wiki.repositorios.Capitulo_Repositorio;
import Kaori.wiki.repositorios.Personaje_Repositorio;
import Kaori.wiki.repositorios.RepositorioUsuarioxDao;
import Kaori.wiki.repositorios.Serie_Repositorio;
import Kaori.wiki.repositorios.Snippet_Repositorio;
import Kaori.wiki.repositorios.Temporadas_Repositorio;
import Kaori.wiki.repositorios.Usuario_Repositorio;

@Service
public class Servicios_web {
	// todos los repositorios;
	@Autowired
	Articulo_Repositorio articulo_Repositorio;
	@Autowired
	AvanceSerie_Repositorio  avanceSerie_Repositorio;
	//AvanceSerieKey_Respositorio avanceSerieKey_Respositorio;
	@Autowired
	Capitulo_Repositorio capitulo_Repositorio;
	@Autowired
	Personaje_Repositorio personaje_Repositorio;
	@Autowired
	Serie_Repositorio serie_Repositorio;
	@Autowired
	Snippet_Repositorio snippet_Repositorio;
	@Autowired
	Temporadas_Repositorio temporadas_Repositorio;
	@Autowired
	Usuario_Repositorio usuario_Repositorio;
	@Autowired
	RepositorioUsuarioxDao usuariox_Repositorio;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Environment environment;
	
	//AUXILIAR
	public static Boolean similares(String s1, String s2) { //S1: Original | S2: Busqueda
	    if(s1 == s2) { return true; }
	    s1 = s1.toLowerCase();
	    s2 = s2.toLowerCase();
	    int iguales = 0;
	    int umbral = s1.length()*(3/5);
	    for(int i = 0; i < s1.length(); i++) {
	    	for(int j = 0; j < s2.length(); j++) {
	    		if(s1.charAt(i) == s2.charAt(j)) { iguales++; }
	    		else if (s1.charAt(i) != s2.charAt(j)) {
	    			if(iguales > umbral) { return true; }
	    		}
	    	}
	    }
	    return false;
	}
	
	//--------------------------------------------------------------------
	//FUNCIONES DE LA PAGINA Y ENTIDADES

	
//SNIPPET
	
	public Snippet registrarSnippet(Snippet snippet) {
		Integer nSnippets = snippet.getCapitulo().getSpoilers().size() + 1;
		String nombreSerie;
		Articulo articulo = new Articulo();
		
		//dandole su id
		snippet.setIdSnippet(snippet.getCapitulo().getIdCapitulo() + "SNP" + nSnippets );
		
		//obtenemos el nombre para encontrar el articulo correspondiente
		nombreSerie = snippet.getCapitulo().getTemporada().getSerie().getNombre();
		articulo = findArticuloBySerie(nombreSerie);
		articulo.getSpoilers().add(snippet);
		
		
		articulo_Repositorio.save(articulo);
			
		
		return snippet_Repositorio.save(snippet);
	}	
	
	public List<Snippet> listarSnippet(){ //deberiamos poner un condicionl para que solo admins tengan acceso
		return this.snippet_Repositorio.findAll();
	}
	
	public List<Snippet> listarSnippetsDeCapitulo(String idCapitulo){
		return (List<Snippet>) snippet_Repositorio.findAllByCapitulo_idCapitulo(idCapitulo);
	}
	
//USUARIO
	//ESTRICTO
	public Usuariox buscarUsuarioNombre(String nombre){
		List<Usuariox> todos = this.usuariox_Repositorio.findAll();
		for(Usuariox usuario:todos) {
			if (usuario.getUsername() == nombre) { return usuario; }
		}
		return null;
	}
	public Usuariox buscarUsuarioId(Long idUsuario) {
		return usuariox_Repositorio.findById(idUsuario).get();
	}
	public List<Usuariox> obtenerUsuarios(){
		return this.usuariox_Repositorio.findAll();
	}
	public List<AvanceSerie> getLista(Long idUsuario){
		return usuariox_Repositorio.findById(idUsuario).get().getLista();
	}
	public List<AvanceSerie> añadirSerie(Long idUsuario,String idSerie){
		Usuariox usuario = this.buscarUsuarioId(idUsuario);
		Serie serie = this.getSeriebyId(idSerie);
		AvanceSerie avance = new AvanceSerie();
		avance.setSerie(serie);
		avance.setUsuario(usuario);
		avance.setCapitulo(serie.getCapitulo(0, 0));
		List<AvanceSerie> series = new ArrayList<AvanceSerie>();
		usuario.setLista(series);
		usuariox_Repositorio.save(usuario);
		series.add(avanceSerie_Repositorio.save(avance));
		
		return usuario.getLista();	
	}
	public Capitulo getUltimoCapSerie(Long idUsuario, String idSerie) {
		Usuariox usuario = this.usuariox_Repositorio.findById(idUsuario).get();
		Serie serie = this.serie_Repositorio.findById(idSerie).get();
		return usuario.ultCapVisto(serie);
	}
	
	public List<AvanceSerie> actualizarLista(Usuariox usuario, Serie serie, Integer temporada, Integer capitulo) {
		AvanceSerie avanceSerie = usuario.ActualizarCapitulo(serie, temporada, capitulo);
		avanceSerie_Repositorio.save(avanceSerie);
		return  usuario.getLista();
	}
	
//AvanceSerie
			public List<AvanceSerie> listListas() {
				return (List<AvanceSerie>) avanceSerie_Repositorio.findAll();
			}
			
//PERSONAJE
	@PostMapping("/regPersonaje")
	Object registrarPersonaje() {
		return this.restTemplate.getForObject("http://personaje-service/api/regPersonaje", Object.class);
	}
	@GetMapping("/getPersonaje/{idPersonaje}")
	Object obtenerPersonaje(@PathVariable(value = "idPersonaje") String idPersonaje) {
		return this.restTemplate.getForObject("http://personaje-service/api/Personaje/"+idPersonaje, Object.class);
	}

	
// ARTICULOS
	
	@Transactional
	public Articulo registrarArticulo(Articulo nuevo) {
		return articulo_Repositorio.save(nuevo);
	}

	public List<Articulo> obtenerArticulos(){
		return articulo_Repositorio.findAll();
	}
	public Articulo obtenerArticulo(Long id){
		Optional<Articulo> aux = this.articulo_Repositorio.findById(id);
		return aux.get();
	}
	/*
	public List<Snippet> filtrarArticulo(Long idArticulo,String idCapitulo){
		Articulo articulo = new Articulo();
		articulo = obtenerArticulo(idArticulo);
		Capitulo capitulo = capitulo_Repositorio.findById(idCapitulo).get();
		return articulo.getArticuloCensura(capitulo);
	}
	*/
	//EN OPERACION
	public List<Snippet> filtrarArticulo(Long idArticulo, Integer nCap){
		Articulo articulo = new Articulo();
		articulo = obtenerArticulo(idArticulo);
		return articulo.getArticuloCensuraInt(nCap);
		
	}
	
	
	public Articulo findArticuloBySerie(String nombreSerie) {
		List<Articulo> articulos = articulo_Repositorio.findAll();
		for (int i = 0; i < articulo_Repositorio.count();i++) {
			if(articulos.get(i).getSerie().getNombre() == nombreSerie) {
				return articulos.get(i);
			}
		}
		return null;
	}
	
	//EN TEORÍA LA DE SEARCH BAR
	public List<Articulo> obtenerArticulo(String busqueda) {
		String[] aux = busqueda.split(" ");
		List<Articulo> articulos = this.obtenerArticulos();
		List<Articulo> res = new ArrayList<Articulo>();
		for(Articulo auxArt:articulos) {
			if(auxArt.getTituloArticulo() == busqueda || auxArt.getSerie().getNombre() == busqueda){
				res.add(auxArt);
			}else{
				for(short i = 0; i < aux.length;i++) {
					if (auxArt.getSerie().getNombre() == aux[i]) { res.add(auxArt); }
				}
			}
		}
		return articulos;
	}

//AVANCESERIES
	
	public void actualizarAvanceSerie(Usuariox usuario, Serie serie, Integer temporada, Integer capitulo) {
		usuario.ActualizarCapitulo(serie, temporada, capitulo);
	}
	
//SERIES
	
	//TODO: FORMULARIOS EN FRONT
	public Serie registrarSerie(Serie nuevo) {
		return serie_Repositorio.save(nuevo);
	}
	
	public List<Serie> listSeries(){
		return (List<Serie>) serie_Repositorio.findAll();
	}
	
	public Serie buscarSerieTitulo(String titulo) {
		//TODO: APRENDER QUERYS
		List<Serie> series = (List<Serie>) serie_Repositorio.findAll();
		for(Serie aux:series) {
			if(this.similares(aux.getNombre(),titulo)) {
				return aux;
			}
		}
		return null;
	}
	
	public Serie getSeriebyId(String idSerie) {
		return serie_Repositorio.findById(idSerie).get();
	}
	
	//FUNCION TEMPORAL
	public Serie buscarSerieByTituloArticulo(String nombre) {
		return serie_Repositorio.findByNombre(nombre);
	}
	
//TEMPORADA
	public List<Temporada> listarTemporada(){
		return temporadas_Repositorio.findAll();
	}
	public Temporada buscarTemporadaId(String idTemporada) {
		return temporadas_Repositorio.findById(idTemporada).get();
	}
	
	public void addTemporada(Serie serie, Temporada temporada) {
		serie.addTemporada(temporada);
	}
	
	public Temporada registrarTemporada(Temporada temporada) {
		return temporadas_Repositorio.save(temporada);
	}
	
	public void addCapitulo(Serie serie, Integer temporada, Capitulo nuevo) {
		serie.getTemporada(temporada).addCapitulo(nuevo);
	}
	
	public List<Temporada> listTemporadasDeSerie(String idSerie){
		return (List<Temporada>) temporadas_Repositorio.findAllBySerie_idSerie(idSerie);
	}
	
//CAPITULOS
	
	public Capitulo registrarCapitulo(Capitulo capitulo) {
		return capitulo_Repositorio.save(capitulo);
	}
	public Capitulo buscarCapituloId(String idCapitulo) {
		return capitulo_Repositorio.findById(idCapitulo).get();
	}
	public List<Capitulo> buscarCapituloId2(String idCapitulo) {
		return (List<Capitulo>) capitulo_Repositorio.findAllByidCapitulo(idCapitulo);
	}
	public List<Capitulo> listarCapitulo(){
		return (List<Capitulo>) capitulo_Repositorio.findAll();
	}
	
	public List<Capitulo> listCapitulosDeTemporada(String idTemporada){
		return (List<Capitulo>) capitulo_Repositorio.findAllByTemporada_idTemporada(idTemporada);
	}
	
}


