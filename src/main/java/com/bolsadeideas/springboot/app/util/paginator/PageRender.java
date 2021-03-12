package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> { //Se utiliza el <> ya que tiene que ser genérico y se puede paginar cualquier tipo de cosas
	
	private String url;
	
	private List<PageItem> paginas;
	
	private Page<T> page;
	
	private int totalPaginas;
	
	private int numElementosPorPagina;
	
	private int paginaActual;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();
		
		numElementosPorPagina = page.getSize(); //Se obtiene por la cantidad de elementos que tiene la página.
		totalPaginas = page.getTotalPages(); //Cantidad de páginas.
		paginaActual = page.getNumber() + 1;
		
		int desde, hasta;
		
		if(totalPaginas <= numElementosPorPagina) {
			desde = 1;
			hasta = totalPaginas;
		} else {
			if(paginaActual <= numElementosPorPagina/2) {
				desde = 1;
				hasta = numElementosPorPagina;
			} else if(paginaActual >= totalPaginas - numElementosPorPagina/2 ) {
				desde = totalPaginas - numElementosPorPagina + 1;
				hasta = numElementosPorPagina;
			} else { //Significa que se está en el medio
				desde = paginaActual - numElementosPorPagina/2;
				hasta = numElementosPorPagina;
			}
		}
		
		for(int x=0; x<hasta; x++) {
			paginas.add(new PageItem(desde + x, paginaActual == desde + x));
		}
	}

	public String getUrl() {
		return url;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
	
	

}
