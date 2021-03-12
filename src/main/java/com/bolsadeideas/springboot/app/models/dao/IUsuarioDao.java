package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	
	//@Query("select u from Usuario u where u.username = ?") //También se podría utilizar esta sentencia.
	public Usuario findByUsername(String username); //Se busca al usuario mediante el username.
}
