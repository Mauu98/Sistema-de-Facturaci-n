package com.bolsadeideas.springboot.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@XmlRootElement(name = "clientes")
public class ClienteList { //Clase Wrapper para convertir el listado de Clientes a XML.
	
	@XmlElement(name = "cliente")
	public List<Cliente> clientes;

	//Se crea un constructor vacío para que lo pueda manejar el javaxbe - XML, para que lo pueda convertir.
	public ClienteList() {}

	public ClienteList(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}
	
	
	
	
	

}
