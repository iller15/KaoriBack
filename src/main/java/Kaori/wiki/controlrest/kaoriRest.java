package Kaori.wiki.controlrest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Kaori.wiki.entidades.Articulo;
import Kaori.wiki.entidades.AvanceSerie;
import Kaori.wiki.entidades.Capitulo;
import Kaori.wiki.entidades.Serie;
import Kaori.wiki.entidades.Snippet;
import Kaori.wiki.entidades.Temporada;
import Kaori.wiki.entidades.Usuario;
import Kaori.wiki.entidades.Usuariox;
import Kaori.wiki.servicios.Servicios_web;
import Kaori.wiki.servicios.UsuarioService;
import Kaori.wiki.servicios.UsuarioServices;

@RestController
@RequestMapping("/api")
public class kaoriRest {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private Servicios_web servicio;
	
	
	@RequestMapping(path = "/test")
    public String testto() {
        return "Holo, funciono!";
    }


	@Autowired
	private UsuarioServices usuarioService;
//USUARIO
	
	@PostMapping("/regusuario")
	public Usuariox registrar(@RequestBody Usuariox usuario) {
		return usuarioService.registrar(usuario);
	}
	
	@GetMapping("/usuarios")
	public List<Usuariox> verUsuarios(){
		return servicio.obtenerUsuarios();
	}
	@PostMapping("/agregarSerie/{idUsuario}/{idSerie}")
	public List<AvanceSerie> añadirSerie(@PathVariable(value = "idUsuario")Long idUsuario,@PathVariable(value = "idSerie")String idSerie){
		return this.servicio.añadirSerie(idUsuario, idSerie);
	}
	@GetMapping("/lista/{idUsuario}")
	public List<AvanceSerie> listaUsuario(@PathVariable(value = "idUsuario")Long idUsuario){
		return this.servicio.getLista(idUsuario);
	}
	@GetMapping("/listarCap/{idSerie}/{idUsuario}")
	public Capitulo getUltimoCap(@PathVariable(value = "idSerie") String idSerie,@PathVariable(value = "idUsuario") Long idUsuario ) {
		return this.servicio.getUltimoCapSerie(idUsuario, idSerie);
	}

// AvanceSeries
	@GetMapping("/listAvanceSeries")
	public List<AvanceSerie> getListaAvanceSeries() {
		return this.servicio.listListas();
	}

	@PostMapping("/actualizarLista/{idUsuario}/{idSerie}/{nTemp}/{nCap}")
	public List<AvanceSerie> actualizarLista(@PathVariable(value = "idUsuario") Long idUsuario,
			@PathVariable(value = "idSerie") String idSerie, @PathVariable(value = "nTemp") Integer temporada,
			@PathVariable(value = "nCap") Integer capitulo) {
		Usuariox usuario = this.servicio.buscarUsuarioId(idUsuario);
		Serie serie = this.servicio.getSeriebyId(idSerie);
		return this.servicio.actualizarLista(usuario, serie, temporada - 1, capitulo);
	}
	
//SERIE
	
	@PostMapping("/regSerie")
	public Serie registrarSerie(@RequestBody Serie serie) {
		boolean unico = false;
		Integer cont = 1;
		Articulo articulo = new Articulo();
		//Para obtener la fecha actual;
		Date date = new Date();
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
		serie.setAutoIdSerie();
		//verifica si el id dado a la series unico, si no se le cambia el ultimo digito de este aumentandolo en 1
		while(unico) {
			for(int i = 0; i < servicio.listSeries().size();i++) { //estoy seguro de que se puede mejorar con un catch y la funcion ya existente de encontrar por id
				if( serie.getIdSerie() != servicio.listSeries().get(i).getIdSerie() ) {
					unico = true;
					}
				else {
					serie.setIdSerie(serie.getIdSerie() + cont.toString());
					cont += 1;
				}
			}
		}
		//creando el articulo principal de la serie
		//Serie
		articulo.setSerie(serie);
		//titulo
		articulo.setTituloArticulo(serie.getNombre());
		//fecha
		formatoFecha.format(date);
		articulo.setFechaPublicacion(date);
		
		//guardando el articulo en la DB
		registrarArticulo(articulo);
		
		return servicio.registrarSerie(serie);
	}
	
	@GetMapping("/listSeries")
	public List<Serie> listSerie(){
		return this.servicio.listSeries();
	}
	
	//TODO: SOLO COMO TESTEO
	@PostMapping("/actualiza/{usuario}/{serie}")
	public String actualizarSerie(@PathVariable(value = "usuario") Long usuarioId, @PathVariable(value = "serie") String serieId) {
		//TODO: Formulario
		Usuariox usuario = servicio.buscarUsuarioId(usuarioId);
		Serie serie = servicio.getSeriebyId(serieId);
		servicio.actualizarAvanceSerie(usuario, serie, 1, 1);
		return "TEST, FUNCIONA";
	}
	
	@RequestMapping("/getSerieByNombre/{nombre}")
	public Serie obtenerSerieByNombre(@PathVariable(value="nombre") String nombre) {
		return this.servicio.buscarSerieByTituloArticulo(nombre);
	}
	
	@GetMapping("/getSerieById/{idSerie}")
	public Serie getSerieById(@PathVariable(value = "idSerie") String idSerie) {
		return this.servicio.getSeriebyId(idSerie);
	}

//TEMPORADA
	@PostMapping("/regTemporada-{idSerie}")
	public Temporada registrarTemporada(@PathVariable(value = "idSerie")String idSerie, @RequestBody Temporada temporada) {
		Serie serie = servicio.getSeriebyId(idSerie);
		Integer nTemporadas = serie.getTemporadas().size();
		temporada.setSerie(serie);
		temporada.setNumTemporada(serie.getTemporadas().size()+1);
		temporada.setAutoIdTemporada(serie.getIdSerie(), nTemporadas);
		return servicio.registrarTemporada(temporada);
	}
	
	@GetMapping("/listTemporadas")
	public List<Temporada> listarTemporada(){
		return this.servicio.listarTemporada();
	}
	
	@RequestMapping("/listTemporadasByIdSerie/{idSerie}")
	public List<Temporada> listTemporadasByIdSerie(@PathVariable(value="idSerie") String idSerie) {
		return this.servicio.listTemporadasDeSerie(idSerie);
	}
	
//CAPITULO
	
	//El usuario no tiene que poner ni idCapitulo ni temporada ni numero de capitulo
	@PostMapping("/regCapitulo-{idTemporada}") 
	public Capitulo registrarCapitulo(@PathVariable(value = "idTemporada")String idTemporada, @RequestBody Capitulo capitulo) {
		Temporada temporada = servicio.buscarTemporadaId(idTemporada);
		Integer ncap= temporada.getCapitulos().size();
		ncap +=1;
		capitulo.setNumCap(ncap);
		capitulo.setIdCapitulo(idTemporada + "C" + ncap.toString());
		capitulo.setTemporada(temporada);
		
		return servicio.registrarCapitulo(capitulo);
	}
	
	@GetMapping("/listCapitulo")
	public List<Capitulo> listCapitulo(){
		return servicio.listarCapitulo();
	}
	
	@RequestMapping("/listCapitulosByIdTemporada/{idTemporada}")
	public List<Capitulo> listCapitulosByIdTemporada(@PathVariable(value="idTemporada") String idTemporada) {
		return this.servicio.listCapitulosDeTemporada(idTemporada);
	}
	
	@RequestMapping("/buscarCapituloById/{idCapitulo}")
	public List<Capitulo> buscarCapituloById(@PathVariable(value="idCapitulo") String idCapitulo) {
		return this.servicio.buscarCapituloId2(idCapitulo);
	}
	
	
//SNIPPET
	
	@PostMapping("/snippet-{idCapitulo}")
	public Snippet registrarSnippet(@RequestBody Snippet snippet, @PathVariable(value = "idCapitulo") String idCapitulo) {
		snippet.setCapitulo(servicio.buscarCapituloId(idCapitulo));
			
		return servicio.registrarSnippet(snippet);
	}
	
	@GetMapping("/listSnippet")
	public List<Snippet> listarSnippet(){
		return servicio.listarSnippet();
	}
	
	@RequestMapping("/listSnippetsByIdCapitulo/{idCapitulo}")
	public List<Snippet> listSnippetsByIdCapitulo(@PathVariable(value="idCapitulo") String idCapitulo) {
		return this.servicio.listarSnippetsDeCapitulo(idCapitulo);
	}


//ARTICULO
	
	//Seccion articulos
	@GetMapping("/listArticulo")
	public List<Articulo> listarArticulos(){
		return servicio.obtenerArticulos(); 
	}
	
	@GetMapping("/articulo-{idArticulo}")
	public Articulo obtenerArticulo(@PathVariable(value = "idArticulo")Long idArticulo) {
		return servicio.obtenerArticulo(idArticulo);
	}
	
	
	/* EN OPERACION*/
	@GetMapping("/articulo-{idArticulo}/{nCapitulo}")
	public List<Snippet> obtenerSnippetsFiltrados(@PathVariable(value = "idArticulo")Long idArticulo,@PathVariable(value = "nCapitulo") Integer nCapitulo) {
		return servicio.filtrarArticulo(idArticulo,nCapitulo);
	}
	
	
	
	@PostMapping("/regArticulo")
	public Articulo registrarArticulo(Articulo articulo) {
		return servicio.registrarArticulo(articulo);
	}
	
	
}



	
	
