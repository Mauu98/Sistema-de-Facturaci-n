package com.bolsadeideas.springboot.app;



import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.springboot.app.models.services.JPAUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter { // Clase para administrar la Seguridad de
																			// Spring.
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Autowired
	private LoginSuccessHandler successHandler;
		
	@Autowired
	private JPAUserDetailsService userDetailsService;
	
	

	@Override
	protected void configure(HttpSecurity http) throws Exception { //Método para el acceso a rutas. Según el rol.
		http.authorizeRequests().antMatchers("/","/css/**","/js/**","/images/**","/listar**","/locale","/api/clientes/**").permitAll() //Rutas permitidas para todos, osea públicas.
		/*.antMatchers("/ver/**").hasAnyRole("USER")*/ //Rutas permitidas para el usuario de tipo USER.
		/*.antMatchers("/upload/**").hasAnyRole("USER")*/ 
		/*.antMatchers("/form/**").hasAnyRole("ADMIN") //Rutas permitidas para el usuario con rol de ADMIN.*/
		/*.antMatchers("/eliminar/**").hasAnyRole("ADMIN")*/
		/*.antMatchers("/factura/**").hasAnyRole("ADMIN")*/
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.successHandler(successHandler)
		.loginPage("/login")
		.permitAll() //El inicio de sesion se permite a todos.
		.and()
		.logout().permitAll() //El logout se permite a todos los usuarios.
		.and()
		.exceptionHandling().accessDeniedPage("/error_403"); //Se pasa la página al tratar de ingresar a una ruta con permisos de Admin. siendo otro usuario con roles menores.
	}

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		//JDBC Authentication
		builder.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
		
		//.usersByUsernameQuery("select username, password, enable from users where username = ?")
		//.authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id = u.id)  where u.username = ?"); //Se seleccionan los parametros haciendo un inner join, ya que user_id es clave foranea de user, y se la relaciona con la tabla authorities
		

	}

}
