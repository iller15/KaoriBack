package Kaori.wiki.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Usuarios") //SEGÃšN ESQUEMA YA ESCRITO
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUsuario;
	private String nombreUsuario;
	private Integer nivelUsuario; 
	private String password;
	
	@JsonIgnore
	@OneToMany(mappedBy = "usuario")
	List<AvanceSerie> series;
	
	private String correo;
	private boolean banned;
	
	
	public Usuario() {
		this.series = new ArrayList<AvanceSerie>();
	}
	
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public Integer getNivelUsuario() {
		return nivelUsuario;
	}
	public void setNivelUsuario(Integer nivelUsuario) {
		this.nivelUsuario = nivelUsuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<AvanceSerie> getAvancesSeries() {
		return series;
	}
	public void setAvancesSeries(List<AvanceSerie> series) {
		this.series = series;
	}
	public List<Serie> getSeries(){
		List<Serie> listaSeries = new ArrayList<Serie>();
		for(AvanceSerie veo:series) { listaSeries.add(veo.getSerie()); }
		return listaSeries;
	}
	
	//add implementado en Funciones
	
 	public String getCorreo() {
		return correo;
	}
	public boolean isBanned() {
		return banned;
	}
	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	// FUNCIONES: 
	// ----------------------------------------------------------------------------------
	public boolean veSerie(Serie serie) {
		for (AvanceSerie temp: series) {
			if (temp.getSerie().getIdSerie() == serie.getIdSerie()) { return true; }
		}
		return false;
	}
	public AvanceSerie getSerieV(Serie serie) { // posible NULL
		for (AvanceSerie temp: series) {
			if (temp.getSerie().getIdSerie() == serie.getIdSerie()) { return temp; }
		}
		return null;
	}
	public Capitulo ultCapVisto(Serie serie) {  // puede Devoler NULL
		for(AvanceSerie temp: series) {
			if(temp.getSerie().getIdSerie() == serie.getIdSerie()) { return temp.getCapitulo(); }
			return null;
		}
		return null;
	}
	
	//INCOMPLETO: ERRORES (try catch y otros >>> manejo con NULL actualmente)
	//DONE: Serie que ve o serie nueva
	public void ActualizarCapitulo(Serie serie, Integer temporada, Integer capitulo) {
		AvanceSerie t_serieV = this.getSerieV(serie);
		Capitulo nuevoCap = serie.getCapitulo(temporada, capitulo);
		if (t_serieV != null) {
			//CASO 1: ES UNA SERIE QUE ESTÁ VIENDO
			if(nuevoCap != null) {
				t_serieV.setCapitulo(nuevoCap);
			}
		}else {
			//CASO 2: La serie no la ha visto
			if(nuevoCap != null) {
				//t_serieV = new AvanceSerie(this,serie, nuevoCap);
				this.series.add(t_serieV);
			}
			//NO HACE NADA SI EL INPUT ES INVALIDO
		}
		//MANEJO DE ERRORES
		//TODO: todo
	}
	
}
