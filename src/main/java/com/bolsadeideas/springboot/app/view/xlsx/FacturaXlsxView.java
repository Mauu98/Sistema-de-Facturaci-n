package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//Acción para cambiar el nombre del archivo.
		response.setHeader("Content-Disposition", "attachment; filename=\"factura_view.xlsx\"");
		
		Factura factura = (Factura) model.get("factura");
		Sheet sheet = workbook.createSheet("Factura de ".concat(factura.getCliente().getNombre().concat(" ").concat(factura.getCliente().getApellido()))); //Se crea la planilla.
		
		Row row = sheet.createRow(0); //Se crea la primera fila. ((Se importa todo de ss.usermodel))
		Cell cell = row.createCell(0); //Primera columna.
		
		cell.setCellValue("Datos del Cliente"); //Título
		row = sheet.createRow(1); //Fila 1
		cell = row.createCell(0); //Columna 0
		cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido()); //Se obtienen los datos del cliente a través de la factura.
		
		row = sheet.createRow(2); //Fila 2
		cell = row.createCell(0); //Columna 0
		cell.setCellValue(factura.getCliente().getEmail());
		
		//Se encadenan el createRow y el createCell //Se parte de la fila 4 para dar un salto de espacio.
		sheet.createRow(4).createCell(0).setCellValue("Datos de la Factura");
		sheet.createRow(5).createCell(0).setCellValue("Folio: "+factura.getId());
		sheet.createRow(6).createCell(0).setCellValue("Descripción: "+factura.getDescripcion());
		sheet.createRow(7).createCell(0).setCellValue("Fecha: "+factura.getCreateAt());
		
		//Estilos de la columna - fila //En este caso para el header.
		CellStyle theaderStyle = workbook.createCellStyle();
		theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
		theaderStyle.setBorderTop(BorderStyle.MEDIUM);
		theaderStyle.setBorderRight(BorderStyle.MEDIUM);
		theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
		theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
		theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		
		//Body del documento
		CellStyle tbodyStyle = workbook.createCellStyle();
		tbodyStyle.setBorderBottom(BorderStyle.THIN);
		tbodyStyle.setBorderTop(BorderStyle.THIN);
		tbodyStyle.setBorderRight(BorderStyle.THIN);
		tbodyStyle.setBorderLeft(BorderStyle.THIN);
		
		//Precio, producto, cantidad, total
		Row header = sheet.createRow(9);
		header.createCell(0).setCellValue("Producto");
		header.createCell(1).setCellValue("Precio");
		header.createCell(2).setCellValue("Cantidad");
		header.createCell(3).setCellValue("Total");
		
		//Se aplica el estilo.
		header.getCell(0).setCellStyle(theaderStyle); //Se obtiene el número de columna y se le aplica el diseño.
		header.getCell(1).setCellStyle(theaderStyle);
		header.getCell(2).setCellStyle(theaderStyle);
		header.getCell(3).setCellStyle(theaderStyle);
		
		int rownum = 10; //Se inicializa en la 10 ya que las demás filas estan ocupadas.
		for(ItemFactura items: factura.getItems()) { //foreach que recorre los items de la factura..
			Row fila = sheet.createRow(rownum++); //Se incrementa el valor de las filas por cada producto que se vaya agregando. //Un producto se va a llenar en cada fila. //Primero asigna los valores, y luego incrementa.
			
			cell = fila.createCell(0);
			cell.setCellValue(items.getProducto().getNombre()); //Columna 0 en una fila irá el Producto
			cell.setCellStyle(tbodyStyle);
			
			cell = fila.createCell(1);
			cell.setCellValue(items.getProducto().getPrecio()); //Columna 1 en misma fila irá Precio.. etc.
			cell.setCellStyle(tbodyStyle);
			
			cell = fila.createCell(2);
			cell.setCellValue(items.getCantidad());
			cell.setCellStyle(tbodyStyle);
			
			cell = fila.createCell(3);
			cell.setCellValue(items.calcularImporte());
			cell.setCellStyle(tbodyStyle);
		}
		
		Row filaTotal = sheet.createRow(rownum); //Se crea en la fila siguiente a la última de las de arriba.
		
		cell = filaTotal.createCell(2);
		cell.setCellValue("Gran Total");  //Misma columna que cantidad.
		cell.setCellStyle(tbodyStyle);
		
		cell = filaTotal.createCell(3);
		cell.setCellValue("$ "+factura.getTotal()); //Misma columna que el total.
		cell.setCellStyle(tbodyStyle);
		
		
		
	}

}
