package Kaori.wiki.entidades;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "AvanceSerie")
public class AvanceSerie implements Serializable {
	private static final long serialVersionUID = 7935997482454457022L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idAvanceSerie")
	private Long idAvanceSerie;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id")
	private Usuariox usuario;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "idSerie")
	private Serie serie;
	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idCapitulo")
	private Capitulo capitulo;
	
	public AvanceSerie() {}
	
	public AvanceSerie(Usuariox usuario, Serie serie, Capitulo capitulo) {
		this.usuario = usuario;
		this.serie = serie;
		this.capitulo = capitulo;
	}
	
	public Long getIdAvanceSerie() {
		return idAvanceSerie;
	}
	public void setIdAvanceSerie(long idAvanceSerie) {
		this.idAvanceSerie = idAvanceSerie;
	}

	public Usuariox getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuariox usuario) {
		this.usuario = usuario;
	}

	public Serie getSerie() {
		return serie;
	}
	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public Capitulo getCapitulo() {
		return capitulo;
	}
	public void setCapitulo(Capitulo capitulo) {
		this.capitulo = capitulo;
	}
	
}
