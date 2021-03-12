package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.models.services.IUploadFileService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	IUploadFileService iUploadFileService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		iUploadFileService.deleteAll(); //Borra todo al cerrar el proyecto.
		iUploadFileService.init(); //Crea el directorio al iniciar el proyecto.
		
		//Se generan las contraseñas
		String password = "12345";
		
		for(int x=0; x<2; x++) { //Se generan "2" claves encriptadas a partir del "12345". "{x<2}".
			String bycriptPassword = passwordEncoder.encode(password);
			System.out.println(bycriptPassword);
		}
		
		
		
	}

}
