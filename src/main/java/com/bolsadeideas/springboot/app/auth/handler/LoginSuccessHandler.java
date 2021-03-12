package com.bolsadeideas.springboot.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component //Se inyecta en el SpringSecurityConfig.
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		//Al no poder usar el RedirectAttributes para mandar mensajes, se hace de forma manual con el SessionMap..
		SessionFlashMapManager flashManager = new SessionFlashMapManager();
		
		FlashMap flashMap = new FlashMap();
		
		flashMap.put("success", "Hola "+authentication.getName()+" , ha iniciado sesión con éxito");
		flashManager.saveOutputFlashMap(flashMap, request, response); //Se guarda el mensaje en el flashManager.
		
		if(authentication != null) {
			logger.info("El usuario '"+authentication.getName()+"' ha iniciado sesión con éxito");
		}
		
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	

}
