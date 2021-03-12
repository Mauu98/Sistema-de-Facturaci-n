package com.bolsadeideas.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView { //Clase para que en vez de que muestra la plantilla HTML, muestre el PDF.

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, //Método para construir el documento PDF.
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Factura factura = (Factura) model.get("factura"); //Se obtienen los datos de la factura del objeto "factura" proveniente de Factura Controller.
		
		PdfPTable tabla = new PdfPTable(1); //La tabla solo tendrá 1 sola columna.
		tabla.setSpacingAfter(20);
		
		PdfPCell cell = null;
		
		cell = new PdfPCell(new Phrase("Datos del Cliente"));
		cell.setBackgroundColor(new Color(184,218,255));
		cell.setPadding(8f);
		
		tabla.addCell(cell); //Título de la tabla
		
		
		
		tabla.addCell(factura.getCliente().getNombre()+" "+factura.getCliente().getApellido()); //Se obtienen los datos del cliente.
		tabla.addCell(factura.getCliente().getEmail());
		
		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20); //Espacio entre las tablas.
		
		cell = new PdfPCell(new Phrase("Datos de la Factura"));
		cell.setBackgroundColor(new Color(195,230,203));
		cell.setPadding(8f);
		
		tabla2.addCell(cell);
		tabla2.addCell("Folio: "+factura.getId());
		tabla2.addCell("Descripción: "+factura.getDescripcion());
		tabla2.addCell("Fecha: "+factura.getCreateAt());
		
		//Se guardan las tablas en el documento.
		document.add(tabla);
		document.add(tabla2);
		
		//Tabla para los productos 
		PdfPTable tabla3 = new PdfPTable(4);
		tabla3.setWidths(new float[] {3.5f, 1, 1, 1}); //Tamaño de las columnas.
		tabla3.addCell("Producto");
		tabla3.addCell("Precio");
		tabla3.addCell("Cantidad");
		tabla3.addCell("Total");
		
		//Se iteran los productos.
		for(ItemFactura item : factura.getItems()) {
			tabla3.addCell(item.getProducto().getNombre());
			tabla3.addCell(item.getProducto().getPrecio().toString());
			
			cell = new PdfPCell(new Phrase(item.getCantidad().toString())); //Se hace esto para poder centrar la cantidad en este caso.
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //Alineamiento al centro.
			tabla3.addCell(cell);
			
			
			tabla3.addCell(item.calcularImporte().toString());
		}
		
		//Se crea una celda para el gran total.
		/*PdfPCell cell = new PdfPCell(new Phrase("Total: $"));
		cell.setColspan(3); //3 columnas.
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT); //Se alínea la celda a la derecha.
		tabla3.addCell(cell); //Se agrega la celda a la tabla 3.
		tabla3.addCell(factura.getTotal().toString());*/
		
		document.add(tabla3);
	} 

}
