package com.bolsadeideas.springboot.app.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocaleController {
	
	@GetMapping("/locale") //Al traducir la página se actualiza la última página en la que está, sin redirigir a ningúna otra.
	public String local(HttpServletRequest request) {
		String ultimaUrl = request.getHeader("referer"); //El "referer" nos entrega el link de nuestra ultima pagina.
		return "redirect:".concat(ultimaUrl);
	}

}
