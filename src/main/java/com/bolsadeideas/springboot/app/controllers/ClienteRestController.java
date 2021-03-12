package com.bolsadeideas.springboot.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.services.IClienteService;

@RestController //Es una combinación de Controller y ResponseBody.
@RequestMapping("/api/clientes")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping(value = "/listar")
	public List<Cliente> listar() { //No hace falta poner el @ResponseBody ya que se hereda de @RestController.
		return clienteService.findAll();
	}
}
