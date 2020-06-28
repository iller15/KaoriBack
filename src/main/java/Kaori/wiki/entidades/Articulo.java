package Kaori.wiki.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;



@Entity
@Table(name = "Articulos")
public class Articulo implements Serializable {
	private static final long serialVersionUID = 7935997482454457022L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idArticulo;
	
	private String tituloArticulo;
	private Date fechaPublicacion;
	//elimine lo de palabras clace porque se puede hacer con metodos de java solo unsando el titulo.
	
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
			name = "Articulo_Serie_tlb",
			joinColumns = @JoinColumn(name = "idArticulo"),
			inverseJoinColumns = @JoinColumn(name = "idSnippet")
	)
	private List<Snippet> spoilers; 
	
	// List como LISTA ENLAZADA en Funcion getArticuloCensura
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@ManyToOne
	@JoinColumn(name = "idSeries")
	private Serie serie;
	
	//GET & SET
	public Long getIdArticulo() {
		return idArticulo;
	}
	public void setIdArticulo(Long idArticulo) {
		this.idArticulo = idArticulo;
	}

	public String getTituloArticulo() {
		return tituloArticulo;
	}
	public void setTituloArticulo(String tituloArticulo) {
		this.tituloArticulo = tituloArticulo;
	}

	public Date getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(Date fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public List<Snippet> getSpoilers() {
		return spoilers;
	}
	public void setSpoilers(List<Snippet> spoilersEnArticulo) {
		this.spoilers = spoilersEnArticulo;
	}

	public Serie getSerie() {
		return serie;
	}
	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	//FUNCIONES
	
	//ERROR AL QUERER INCIAR LIST EN NULL :C
	// LIST DECLARADA COMO LISTA ENLAZADA
	
	public List<Snippet> getArticuloCensura(Capitulo capitulo){
		List<Snippet> articuloFinal = new LinkedList<Snippet>();
		for (Snippet spoiler: spoilers) {
			if(spoiler.getCapitulo().getNumCap() <= capitulo.getNumCap()) {
				articuloFinal.add(spoiler);
			}
		}
		return articuloFinal;
	}
	
	public List<Snippet> getArticuloCensuraInt(Integer ncapitulo){
		List<Snippet> articuloFinal = new LinkedList<Snippet>();
		for (Snippet spoiler: spoilers) {
			if(spoiler.getCapitulo().getNumCap() <= ncapitulo) {
				articuloFinal.add(spoiler);
			}
		}
		return articuloFinal;
	}
	
}
