package Kaori.wiki.entidades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Capitulos")
public class Capitulo {
	@Id
	@Column(name = "id")
	private String idCapitulo;
	private int numCap;
	private String tituloCap;
	
	@Cascade(CascadeType.ALL)
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "idTemporada")
	private Temporada temporada;
	
	@Cascade(CascadeType.ALL)
	@JsonIgnore
	@OneToMany(mappedBy = "capitulo")
	List<Snippet> spoilers;
	
	@Cascade(CascadeType.ALL)
	@JsonIgnore
	@OneToMany(mappedBy = "capitulo")
	List<AvanceSerie> avances;
	
	public Capitulo(Temporada temporada, Integer numCap, String titulo) {
		this.idCapitulo = temporada.getIdtemporada() + numCap.toString();
		this.tituloCap = titulo;
		this.temporada = temporada;
		this.numCap = numCap;
		this.spoilers = new ArrayList<Snippet>();
	}
	
	public Capitulo() {
		this.spoilers = new ArrayList<Snippet>();
		this.avances = new ArrayList<AvanceSerie>();
	}
	
	//GET & SET
	public String getIdCapitulo() {
		return idCapitulo;
	}
	public void setIdCapitulo(String idCapitulo) {
		this.idCapitulo = idCapitulo;
	}

	public int getNumCap() {
		return numCap;
	}
	public void setNumCap(int numCap) {
		this.numCap = numCap;
	}

	public String getTituloCap() {
		return tituloCap;
	}
	public void setTituloCap(String tituloCap) {
		this.tituloCap = tituloCap;
	}

	public Temporada getTemporada() {
		return temporada;
	}
	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}
	
	public List<Snippet> getSpoilers() {
		return spoilers;
	}

	public void setSpoilers(List<Snippet> spoilers) {
		this.spoilers = spoilers;
	}

	public List<AvanceSerie> getAvances() {
		return avances;
	}

	public void setAvances(List<AvanceSerie> avances) {
		this.avances = avances;
	}

	//MAYOR O MENOR (TODO: AVERIGUAR SOBRE SOBRECARGA DE OPERADORES EN JAVA)
	public boolean mayorA(Capitulo otro) {
		return (this.getTemporada().getNumTemporada() > otro.getTemporada().getNumTemporada());
	}
	public boolean menorA(Capitulo otro) {
		return (this.getTemporada().getNumTemporada() < otro.getTemporada().getNumTemporada());
	}
	
}
