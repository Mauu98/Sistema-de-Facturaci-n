package com.bolsadeideas.springboot.app.controllers;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.services.IClienteService;
import com.bolsadeideas.springboot.app.models.services.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;


@Controller
public class ClienteController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	//protected final Log logger = (Log) LogFactory.getLog(this.getClass());
	
	@Autowired //Se inyecta la interfaz
	//@Qualifier("clienteDaoJPA") //Se selecciona el bean concreto, por ejemplo podría tener 2 clases usando la misma interfaz, en este caso se especifica que es la de JPA. (Se omite ya que tenemos 1 sola clase.)
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	
	@Secured("ROLE_USER")
	@GetMapping("/upload/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){ //Se toma la imagen filename y se la convierte a un Path
		
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"").body(recurso);
	}
	
	@Secured("ROLE_USER")
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(id); //Se obtiene al cliente.
		if(cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la Base de Datos");
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: "+cliente.getNombre().concat(" ").concat(cliente.getApellido()));
		
		
		return "ver";
	}
	
	//Forma de listar pero con @ResponseBody, este responderá en formato JSON. Este devolverá todos los usuarios, ya que se utiliza el findAll() completo, sin el paginable.
	@GetMapping(value = "/listar-rest")
	public @ResponseBody List<Cliente> listarRest(){ //Si se quisiera también utilizar el XML = listar-rest?format=XML, se debe poner en vez de List<Cliente>, ClienteList y retornar un new ClienteList(clienteService...) ya qué el XML si o si necesita una clase Wrapper.
		return clienteService.findAll();
	}
	
	@RequestMapping(value = {"/listar","/"}, method = RequestMethod.GET) //El valor dará a la direccion "listar", y es de tipo GET, ya que se obtienen datos, en este caso de los clientes.
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, Authentication authentication, HttpServletRequest request) {
		
		if(authentication != null) {
			log.info("Hola usuario autenticado, tu Username es: ".concat(authentication.getName()));
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			log.info("Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): User autenticado, tu Username es: ".concat(authentication.getName()));
		}
		
		//Si se tiene tal rol..
		if(hasRole("ROLE_ADMIN")) {
			log.info("Hola ".concat(auth.getName()).concat(" tienes acceso!. Tu rol es: "+auth.getAuthorities()));
		} else {
			log.info("Hola "+auth.getName()+", no tienes acceso");
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		
		if(securityContext.isUserInRole("ADMIN")) {
			log.info("Forma usando ...SecurityContextHolderAwareRequestWrapper. - Hola ".concat(auth.getName()).concat(" tienes acceso!. Tu rol es: "+auth.getAuthorities()));
		} else {
			log.info("Forma usando ...SecurityContextHolderAwareRequestWrapper. - Hola "+auth.getName()+", no tienes acceso");
		}
		
		if(request.isUserInRole("ROLE_ADMIN")) {
			log.info("Forma usando ...HttpServletRequest. - Hola ".concat(auth.getName()).concat(" tienes acceso!. Tu rol es: "+auth.getAuthorities()));
		} else {
			log.info("Forma usando ...HttpServletRequest. - Hola "+auth.getName()+", no tienes acceso");
		}
		
		/*if(format.equals("html")){
		    Pageable pageRequest = PageRequest.of(page, 4);
 
		    Page<Cliente> clientes = clienteService.findAll(pageRequest);
 
		    PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		    model.addAttribute("clientes", clientes);
			model.addAttribute("page", pageRender);
		} else {
			model.addAttribute("clientes", clienteService.findAll());
		}*/
		
		
		Pageable pageRequest = PageRequest.of(page, 4); // , páginas a mostrar, en este caso 4.
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("titulo", "Listado de Clientes");
		model.addAttribute("clientes", clientes); //Es el cliente que viene con paginación.
		model.addAttribute("page", pageRender);
		
		return "listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping("/form")
	public String crear(Map<String, Object> model) { //Nombre del parámetro, Objeto(Cualquier dato)
		
		Cliente cliente = new Cliente();
		
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	//Método que se encarga de guardar los datos. Los datos ya vienen cargados desde el crear, y este llama al método save de la interfaz y guarda el objeto cliente, luego redirige a la lista de Clientes.
	@RequestMapping(value = "/form", method = RequestMethod.POST) //Procesa el formulario
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) { //Se anota con @Valid ya que le decimos que se validen los campos a los que le hemos pasado las validaciones en la clase Cliente
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		
		//IF Para la foto
		if(!foto.isEmpty()) {
			
			if(cliente.getId() != null && cliente.getFoto() != null && cliente.getId() > 0 && cliente.getFoto().length() > 0) {
				
				uploadFileService.delete(cliente.getFoto());
			}
			
			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			flash.addFlashAttribute("info", "Ha subido correctamente "+uniqueFilename);
			cliente.setFoto(uniqueFilename); 

			
		}
		String mensajeFlash = (cliente.getId() != null)? "Cliente editado con éxito" : "Cliente creado con éxito"; //Mensaje para diferenciar de cuando es editado o creado de 0. Si el ID existe, es editado..
		
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		
		if(id > 0) {
			cliente = clienteService.findOne(id);
			if(cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la base de datos!");
				return "redirect:/listar";
			}
		} else {		
			flash.addFlashAttribute("error", "El ID del cliente no puede ser 0");
			return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if(id > 0) {
			Cliente cliente = clienteService.findOne(id); //Se encuentra al cliente..
			
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente "+cliente.getNombre().concat(" ").concat(cliente.getApellido())+" eliminado con éxito");
			
			
				if(uploadFileService.delete(cliente.getFoto())) { //Si el archivo fue eliminado..
					flash.addFlashAttribute("info", "Foto "+cliente.getFoto()+" eliminada con éxito!");
				}
			}
		
		
	
		return "redirect:/listar";
	}
	
	private boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null) {
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		
		if(auth == null) {
			return false;
		}
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities(); //Se obtiene o se pobla la colección con la cantidad de roles.
		
		return authorities.contains(new SimpleGrantedAuthority(role)); //Retorna un true o false. Simplifica el for de la línea 229.
		
		/*for(GrantedAuthority authority : authorities) { //Recorre la colección buscando el rol
			if(role.equals(authority.getAuthority())) { //Si se da la condición significa que este tiene permisos..
				log.info("Hola ".concat(auth.getName()).concat(" tu rol es: ").concat(authority.getAuthority()));
				return true;
			}
		}
		return false;*/
	}
}
	


