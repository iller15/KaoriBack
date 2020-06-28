package Kaori.wiki.entidades;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Personajes")
public class Personaje {
	@Id
	private String idPersonaje;
	private String Nombre;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "idSeries")
	private Serie serie;

	@JsonIgnore
	@ManyToMany
	(cascade = {CascadeType.ALL})
	@JoinTable(
			name = "Personaje_Snippet_tlb",
			joinColumns = @JoinColumn(name = "idPersonaje"),
			inverseJoinColumns = @JoinColumn(name = "idSnippet")
	)
	private List<Snippet> snippets;
	
	public String getIdPersonaje() {
		return idPersonaje;
	}

	public void setIdPersonaje(String idPersonaje) {
		this.idPersonaje = idPersonaje;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	
	
	//posiblemente falta poer list snippets
	
	
	
}
