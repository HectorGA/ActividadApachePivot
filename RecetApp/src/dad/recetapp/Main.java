package dad.recetapp;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.apache.pivot.wtk.DesktopApplicationContext;

import dad.recetapp.db.DataBase;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;

public class Main {

	public static void main(String[] args) throws SQLException{
		/*Aun no ejecuta nada, solo hace una prueba de la base de datos.*/
		System.out.println("Prueba de conexión: " + DataBase.test());
		
		//	Probamos los servicios
		CategoriaItem categoria = new CategoriaItem();
		categoria.setDescripcion("Pastas");
		
		try {
			ServiceLocator.getCategoriasService().crearCategoria(categoria);
		} catch (ServiceException e) {
			String mensajeRerror = e.getMessage() + "\n\nDetalles: " + e.getCause().getMessage();
			JOptionPane.showMessageDialog(null, mensajeRerror, "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		DesktopApplicationContext.main(RecetAppApplication.class, args);
	}

}
