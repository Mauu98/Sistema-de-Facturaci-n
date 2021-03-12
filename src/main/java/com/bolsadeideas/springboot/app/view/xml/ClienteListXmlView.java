package com.bolsadeideas.springboot.app.view.xml;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("listar.xml")
public class ClienteListXmlView extends MarshallingView{
	
	
	@Autowired //Se inyecta el jaxb2Marshaller
	public ClienteListXmlView(Jaxb2Marshaller marshaller) {
		super(marshaller);
	}

	@Override //Implementación de método
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		 //Se remueven el titulo y el page, ya que son cosas que no queremos dentro del XML.
		model.remove("titulo");
		model.remove("page");
		
		@SuppressWarnings("unchecked")
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		model.remove("clientes");
		
		//Se convierten los clientes al tipo List.
		//Se guarda en el model el ClienteList con el Listado de Clientes.
		model.put("clienteList", new ClienteList(clientes.getContent())); //Se pasa la lista de clientes de cada página, no todos los clientes paginados. //Se pasa la lista de Clientes por constructor.

		
		super.renderMergedOutputModel(model, request, response);
	}
	
	

}
