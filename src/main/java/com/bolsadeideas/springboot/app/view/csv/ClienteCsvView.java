package com.bolsadeideas.springboot.app.view.csv;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("listar.csv") //Se agrega en application properties la extension. Utilizando el ViewResolver.
public class ClienteCsvView  extends AbstractView{
	
	public ClienteCsvView() {
		setContentType("text/csv"); //Se define el tipo de dato.
		
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true; //Si genera un contenido descargable.
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//Mismo o método parecido al de pdf.
		response.setHeader("Content-Disposition", "attachment; filename=\"clientes.csv\"");
		response.setContentType(getContentType());
	
		//Se pasa los clientes, que es de tipo Paginable como en ClienteController. (Archivo Plano)
		
		Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		
		//Este fragmento se saca de Super-CSV -> Writin CSV Files
		ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), //Se obtiene el escritor del archivo plano y se guarda en response.
                CsvPreference.STANDARD_PREFERENCE);
		
		String[] header = {"id","nombre","apellido","email","createAt"};
		beanWriter.writeHeader(header); 
		
		for(Cliente cliente: clientes) {
			beanWriter.write(cliente, header); //Por cada cliente que se está recorriendo se va llenando el documento.
		}
		
		beanWriter.close();
		
	}
	
	

}
