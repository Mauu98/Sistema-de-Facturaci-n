package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	@Id // Indica que es la clave primaria
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Es auto incrementable
	private Long id;

	@NotEmpty
	private String nombre;

	@NotEmpty
	private String apellido;

	@NotEmpty
	@Email //De Constraint se importa.
	private String email;

	@NotNull
	@Column(name = "create_at") // Especifica la columna en la base de datos.
	@Temporal(TemporalType.DATE) // Indica que tipo de datos Date, se muestra, en este caso únicamente la fecha.
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") //Formato para la fecha ya que sino, sale en formato TimeStamp.
	private Date createAt;
	
	private String foto;
	
	/*@PrePersist //Se llama antes de que se ejecute el Persist.
	//public void prePersist() {
	//	createAt = new Date(); //Registra la fecha en la cual se registra un usuario.
	*/
	
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL) //1 cliente, muchas facturas.
	@JsonManagedReference //Es la parte que se quiere mostrar en el documento JSON.
	private List<Factura> facturas; //Un cliente puede tener varias facturas.

	public Cliente() {
		facturas = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;



	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	
	public void addFactura(Factura factura) { //Método para agregar facturas a la lista de facturas del cliente.
		facturas.add(factura);
	}

	@Override
	public String toString() {
		return nombre +" "+ apellido;
	}
	
	

}
