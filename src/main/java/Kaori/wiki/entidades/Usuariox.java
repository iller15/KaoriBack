package Kaori.wiki.entidades;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuariox")
public class Usuariox implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, length = 20)
	private String username;

	@Column(length = 60)
	private String password;

	private Boolean enabled;
	
	private String nombre;
	private String apellido;
	
	@Column(unique = true)
	private String email;
	
	@JsonIgnore
	@OneToMany(mappedBy = "usuario")
	List<AvanceSerie> series;
	

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="usuarios_roles", joinColumns= @JoinColumn(name="usuario_id"),
	inverseJoinColumns=@JoinColumn(name="role_id"),
	uniqueConstraints= {@UniqueConstraint(columnNames= {"usuario_id", "role_id"})})
	private List<Role> roles;
	
	public Usuariox() {
		this.roles = new ArrayList<Role>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<AvanceSerie> getLista() {
		return this.series;
	}

	public void setLista(List<AvanceSerie> lista) {
		this.series = lista;
	}
//End setters y getters
	public boolean veSerie(Serie serie) {
		for (AvanceSerie temp : series) {
			if (temp.getSerie().getIdSerie() == serie.getIdSerie()) {
				return true;
			}
		}
		return false;
	}

	public AvanceSerie getSerieV(Serie serie) { // posible NULL
		for (AvanceSerie temp : series) {
			if (temp.getSerie().getIdSerie() == serie.getIdSerie()) {
				return temp;
			}
		}
		return null;
	}

	public Capitulo ultCapVisto(Serie serie) { // puede Devoler NULL
		for (AvanceSerie temp : series) {
			if (temp.getSerie().getIdSerie() == serie.getIdSerie()) {
				return temp.getCapitulo();
			}
			return null;
		}
		return null;
	}

	// INCOMPLETO: ERRORES (try catch y otros >>> manejo con NULL actualmente)
	// DONE: Serie que ve o serie nueva
	public AvanceSerie ActualizarCapitulo(Serie serie, Integer temporada, Integer capitulo) {
		AvanceSerie t_serieV = this.getSerieV(serie);
		Capitulo nuevoCap = serie.getCapitulo(temporada, capitulo);
		if (t_serieV != null) {
			// CASO 1: ES UNA SERIE QUE ESTÁ VIENDO
			if (nuevoCap != null) {
				t_serieV.setCapitulo(nuevoCap);
				return t_serieV;
			}
		} else {
			// CASO 2: La serie no la ha visto
			if (nuevoCap != null) {
				t_serieV = new AvanceSerie(this, serie, nuevoCap);
				this.series.add(t_serieV);
				return t_serieV;
			}
			// NO HACE NADA SI EL INPUT ES INVALIDO
		}
		return t_serieV;
		// MANEJO DE ERRORES
		// TODO: todo
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
