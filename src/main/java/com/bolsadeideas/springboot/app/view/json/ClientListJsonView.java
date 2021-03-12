package com.bolsadeideas.springboot.app.view.json;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("listar")
public class ClientListJsonView extends MappingJackson2JsonView{

	//Se implementan los métodos con Source...
	@Override
	protected Object filterModel(Map<String, Object> model) {
		
		//Se remueven el titulo y page. Ya que no se los necesita en el JSON.
		model.remove("titulo");
		model.remove("page");
		
		@SuppressWarnings("unchecked")
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		model.remove("clientes");
		model.put("clientes", clientes.getContent()); //Se agregan únicamente los de tipo List.
		
		return super.filterModel(model);
	}
	
	
	

}
