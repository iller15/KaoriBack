package Kaori.wiki.entidades;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "Temporadas")
public class Temporada {
	
	@Id
	private String idTemporada; // GENERADA EN CONSTRUCTOR (idk si funciona con postman :| )
	//Numero de Temporada;
	private int numTemporada;
	//Fechas de emision de la serie.
	private String fecha_inicio;
	
	private String fecha_Final;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "idSerie")
	private Serie serie;
	
	@Cascade(CascadeType.ALL)
	@JsonIgnore
	@OneToMany(mappedBy = "temporada")
	private List<Capitulo> capitulos;
	
	public Temporada(Integer numTemporada, String fechaInicio, Serie serie) {
		this.idTemporada = serie.getIdSerie() + numTemporada.toString();
		this.numTemporada = numTemporada;
		this.fecha_inicio = fechaInicio;
	}
	public Temporada() {
		this.capitulos = new ArrayList<Capitulo>();
	}
	
	public String getIdtemporada() {
		return idTemporada;
	}

	public void setIdtemporada(String idtemporada) {
		this.idTemporada = idtemporada;
	}
	
	public void setAutoIdTemporada(String idSerie,Integer nTemporadas) {
		nTemporadas +=1;
		this.idTemporada = idSerie +"S"+ nTemporadas.toString();
	}

	public int getNumTemporada() {
		return numTemporada;
	}

	public void setNumTemporada(int numTemporada) {
		this.numTemporada = numTemporada;
	}

	public String getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public String getFecha_Final() {
		return fecha_Final;
	}

	public void setFecha_Final(String fecha_Final) {
		this.fecha_Final = fecha_Final;
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerie(Serie serie) {
		this.serie = serie;
	}

	public List<Capitulo> getCapitulos() {
		return capitulos;
	}

	public void setCapitulos(List<Capitulo> capitulos) {
		this.capitulos = capitulos;
	} 
	
	//GETS / SETS 2.0
	
	//MEJORAR PARA MANEJO DE ERRORES (Por ahora valida NULLs)
	public Capitulo getCapitulo(int numCap){ //Puede ser null
		for(Capitulo tempCap: capitulos) {
			if(tempCap.getNumCap() == numCap)
				return tempCap;
		}
		return null;
	}
	
	public void addCapitulo(Capitulo nuevo) {
		if(nuevo != null) { capitulos.add(nuevo); }
	}
	public void addCapitulo(String titulo) {
		Capitulo nuevo = new Capitulo(this,this.capitulos.size()+1,titulo);
		capitulos.add(nuevo);
	}

	
}
