package dad.recetapp.ui;

import java.net.URL;

import javax.swing.JOptionPane;

import javafx.scene.control.TableRow;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.Window;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;

public class CategoriasWindows extends Window implements Bindable {
	
	@BXML private TablePane tablePane;

	public CategoriasWindows() {
		// TODO Auto-generated constructor stub
	}

	public CategoriasWindows(Component content) {
		super(content);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {
		// TODO Auto-generated method stub
		CategoriaItem categoria = new CategoriaItem();
		
		try {
			categoria = ServiceLocator.getCategoriasService().obtenerCategoria(1l);
		} catch (ServiceException e) {
			String mensajeRerror = e.getMessage() + "\n\nDetalles: " + e.getCause().getMessage();
			JOptionPane.showMessageDialog(null, mensajeRerror, "Error", JOptionPane.ERROR_MESSAGE);
		}
		//TODO Agregar la opción de agregar categoria.
		//tablePane.
	}

}
