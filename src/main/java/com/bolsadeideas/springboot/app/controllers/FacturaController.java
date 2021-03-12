package com.bolsadeideas.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.services.IClienteService;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/ver/{id}") //Método para encontrar la factura según su ID.
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Factura factura = clienteService.fetchByIdWithClienteWithItemFacturaWithProducto(id); //Se busca la factura mediante la interfaz.
		
		if(factura == null) { //Si es null..
			flash.addFlashAttribute("error", "La factura no existe en la base de datos");
			return "redirect:/listar"; //Se redirige a listar.
		}
		
		model.addAttribute("factura", factura); //Se muestra la factura.
		model.addAttribute("titulo", "Factura: ".concat(factura.getDescripcion())); //.. Y la descripción de la misma en el titulo.
		
		return "factura/ver"; //Se retorna la vista de la factura.
	}

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable (name = "clienteId") Long clienteId, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.fetchByIdWithFacturas(clienteId); //Se encuentra al cliente
		
		if(cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la Base de Datos");
			return "redirect:/listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente); //Acá se relaciona la factura con el cliente.
		
		model.put("factura", factura);
		model.put("titulo","Crear Factura");
		
		return "factura/form";
	}
	
	//Método para cargar productos en la lista al escribir.
	@GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){ //El responseBody lo que hace es poblar la lista con los datos JSON recibidos por el term.
		return clienteService.findByNombre(term);
		
	}
	
	@PostMapping("/form") //Es de tipo post ya que se envían datos
	public String guardar(@Valid Factura factura,BindingResult result , Model model, @RequestParam(name = "item_id[]", required=false) Long[] itemId, @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
										RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear Factura");
			return "factura/form";
		}
		
		if(itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear Factura");
			model.addAttribute("error", "La factura no puede no tener líneas!");
			return "factura/form";
		}
		
		for(int x=0; x<itemId.length; x++) {
			Producto producto = clienteService.findProductoById(itemId[x]);
			
			ItemFactura linea = new ItemFactura();
			linea.setCantidad(cantidad[x]);
			linea.setProducto(producto);
			
			factura.addItemFactura(linea);
			
			log.info("ID: "+itemId[x].toString()+", cantidad: "+cantidad[x].toString());
			
		}
		
		clienteService.saveFactura(factura);
		status.setComplete();
		
		flash.addFlashAttribute("success","Factura creada con éxito");
		
		return "redirect:/ver/"+factura.getCliente().getId(); //Se envía a los detalles del cliente luego de crear la factura.
		
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable (value = "id") Long id, RedirectAttributes flash) {
		Factura factura = clienteService.findFacturaById(id);
		
		if(factura != null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura eliminada con éxito");
			return "redirect:/ver/" + factura.getCliente().getId();
		}
		
		flash.addFlashAttribute("error", "La factura no existe en la Base de Datos");
		return "redirect:/listar";
	}
}
