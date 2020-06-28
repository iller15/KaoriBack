package Kaori.wiki.entidades;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.type.TextType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Snippet")
public class Snippet {
	@Id
	@Column(unique = true)
	private String idSnippet;
	
	private String description;
	
	@Column(columnDefinition = "LONGTEXT")
	private String texto;
	
	
	//@Column(nullable = true)
	@JsonIgnore
	@ManyToMany(mappedBy = "snippets")
	private List<Personaje> personaje; //Puede ser null
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "idCapitulo")
	private Capitulo capitulo;
	
	@Cascade(CascadeType.ALL)
	@JsonIgnore
	@ManyToMany(mappedBy = "spoilers")
	private List<Articulo> articulos;
	
	public String getIdSnippet() {
		return idSnippet;
	}

	public void setIdSnippet(String idSnippet) {
		this.idSnippet = idSnippet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public List<Personaje> getPersonaje() {
		return personaje;
	}

	public void setPersonaje(List<Personaje> personaje) {
		this.personaje = personaje;
	}
	

	public Capitulo getCapitulo() {
		return capitulo;
	}

	public void setCapitulo(Capitulo capitulo) {
		this.capitulo = capitulo;
	}

	public List<Articulo> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<Articulo> articulos) {
		this.articulos = articulos;
	}
	
}
