package dad.recetapp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.pivot.wtk.DesktopApplicationContext;

import dad.recetapp.db.DataBase;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;

public class Main {

	public static void main(String[] args) throws SQLException{
		/*Se hace una prueba de la base de datos.*/
		System.out.println("Prueba de conexión: " + DataBase.test());
		
		//	Probamos los servicios
		
		/* EJEMPLO DE CREAR UNA NUEVA CATEGORIA CON EL SERVICIO
		 * crearCategoria.*/
		/*CategoriaItem categoria = new CategoriaItem();
		categoria.setDescripcion("Pastas");
		
		try {
			ServiceLocator.getCategoriasService().crearCategoria(categoria);
		} catch (ServiceException e) {
			String mensajeRerror = e.getMessage() + "\n\nDetalles: " + e.getCause().getMessage();
			JOptionPane.showMessageDialog(null, mensajeRerror, "Error", JOptionPane.ERROR_MESSAGE);
		}*/
		
		/* EJEMPLO PARA OBTENER UNA LISTA DE CATEGORIAS CON EL SERVICIO
		 * listarCategoria.
		
		List<CategoriaItem> listaCategorias = new ArrayList<CategoriaItem>();
		
		try {
			listaCategorias = ServiceLocator.getCategoriasService().listarCategoria();
		} catch (ServiceException e) {
			String mensajeRerror = e.getMessage() + "\n\nDetalles: " + e.getCause().getMessage();
			JOptionPane.showMessageDialog(null, mensajeRerror, "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		for (CategoriaItem categoria : listaCategorias) {
			String descripcion = categoria.getDescripcion();
			System.out.println(descripcion);
		}*/
		
		DesktopApplicationContext.main(RecetAppApplication.class, args);
	}

}
