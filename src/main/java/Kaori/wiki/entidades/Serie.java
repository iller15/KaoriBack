package Kaori.wiki.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "Series_TP")
public class Serie {
	@Id
	private String idSerie;
	
	private String nombre;	
	
	
	@OneToMany(mappedBy = "serie")
	private List<Personaje> personajes;
	
	@Cascade(CascadeType.ALL)
	@OneToMany(mappedBy = "serie")
	private List<Temporada> temporadas;
	
	@JsonIgnore
	@OneToMany(mappedBy = "serie")
	private List<Articulo> articulos;
	
	@Cascade(CascadeType.ALL)
	
	@OneToMany(mappedBy = "serie")
	List<AvanceSerie> usuarios;
	
	public Serie() {
		this.personajes = new ArrayList<Personaje>();
		this.temporadas = new ArrayList<Temporada>();
		this.usuarios = new ArrayList<AvanceSerie>();
	}
	
	//Getters and setters
	public String getIdSerie() {
		return idSerie;
	}
	public void setIdSerie(String idSerie) {
		this.idSerie = idSerie;
	}
	
	public void setAutoIdSerie() {
		String[] sum;
		String titulo = "";
		sum = this.nombre.split(" ");
		for (int i = 0; i < sum.length; i++) {
			titulo += sum[i].toCharArray()[0];
		}
		this.idSerie = titulo;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Personaje> getPersonajes() {
		return personajes;
	}
	public void setPersonajes(List<Personaje> personajes) {
		this.personajes = personajes;
	}

	public List<Temporada> getTemporadas() {
		return temporadas;
	}
	public void setTemporadas(List<Temporada> temporadas) {
		this.temporadas = temporadas;
	}

	public List<Articulo> getArticulos() {
		return articulos;
	}
	public void setArticulos(List<Articulo> articulos) {
		this.articulos = articulos;
	}

	//get / set modificados
	public List<Usuariox> getUsuarios(){
		List<Usuariox> resUsuarios = new ArrayList<Usuariox>();
		for(AvanceSerie auxTemp:usuarios) {
			resUsuarios.add(auxTemp.getUsuario());
		}
		return resUsuarios;
	}
	
	//NUEVOS MÉTODOS
	
	public void addArticulos(Articulo nuevo) {
		for(Articulo auxArt: articulos) {
			if(auxArt.getIdArticulo() == nuevo.getIdArticulo() || nuevo == null) { return; }
		}
		articulos.add(nuevo);
	}
	
	public void addPersonaje(Personaje nuevo) {
		if(nuevo != null) {
			this.personajes.add(nuevo);
		}
	}
	public void addTemporada(Temporada nueva) { // Nueva temporada (ya creada)
		if(nueva != null) {
			this.temporadas.add(nueva);
		}
	}
	public void addTemporada() { // Nueva Temporada de Serie en Emisión
		Temporada nueva = new Temporada(this.temporadas.size()+1, " ", this);
		this.temporadas.add(nueva); //alternativamente usar addTemporada(nueva)
	}
	public void addTemporada(String fechaInicio) {
		Temporada nueva = new Temporada(this.temporadas.size()+1, fechaInicio, this);
		this.temporadas.add(nueva);
	}
	
	//Pueden ser NULL
	public Capitulo getCapitulo(int temporada, int capitulo) {
		if(this.temporadas.size() > temporada) {
			return temporadas.get(temporada).getCapitulo(capitulo);
		}
		return null;
	}
	public Temporada getTemporada(int temporada) {
		if(this.temporadas.size() > temporada) {
			return this.temporadas.get(temporada);
		}
		return null;
	}
}
