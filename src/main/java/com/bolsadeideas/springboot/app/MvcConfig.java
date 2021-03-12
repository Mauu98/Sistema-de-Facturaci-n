package com.bolsadeideas.springboot.app;

import java.nio.file.Paths;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		String resourcePath  = Paths.get("upload").toAbsolutePath().toUri().toString(); //El toUri() convierte el Path en file.
		log.info(resourcePath);
		
		//Se mapen las imágenes a una ruta URL
		registry.addResourceHandler("/upload/**").addResourceLocations(resourcePath);
	}*/
	
	//Se manejan el error al no tener permiso al acceder a ciertas rutas como usuario.
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/error_403").setViewName("error_403");
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() { //Componente de Spring que se encarga de encriptar las password.
		return new BCryptPasswordEncoder();
	}
	
	//Método para manejar los idiomas del programa. Se guarda en la sesión.
	@Bean(name="localeResolver")
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("es","ES")); //Idioma por defecto.
		return localeResolver;
	}
	
	//Método para cambiar los textos de la página
	@Bean
	public LocaleChangeInterceptor localChangeInterceptor() {
		LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
		localeInterceptor.setParamName("lang");
		return localeInterceptor;
	}

	//Source -> Override Methods -> addInterceptors.
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor( localChangeInterceptor()); //Se inyecta el interceptor.
	}
	
	//Se encarga de extender y crear este componente //XML no puede convertir archivos de tipo List, Collection, para eso se debe tener una clase Wrapper.
	@Bean //XML
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(new Class[] {com.bolsadeideas.springboot.app.view.xml.ClienteList.class}); //Se agregarán las clases a convertir a XML. //Se pasa la ubicación en este caso de ClienteList(Wrapper)
		return marshaller;
	}
	
	
	
}
