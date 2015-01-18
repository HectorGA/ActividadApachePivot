package dad.recetapp.ui;

import java.net.URL;
import java.util.ArrayList;

import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.LinkedList;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;

public class CategoriasWindows extends Window implements Bindable {

	private TextInput descripcionText;
	private PushButton botonAnadir = null;
	private PushButton botonEliminar = null;
	private TableView tableView;
	private List<String> listaCategorias;
	
	public CategoriasWindows() {
	}

	public CategoriasWindows(Component content) {
		super(content);
	}

	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		//TODO seguir ejemplo de http://chuwiki.chuidiang.org/index.php?title=Ejemplo_TableView_con_Apache_Pivot
		
		//	Vinculamos la tablaView y los botones añadir y eliminar
		tableView = (TableView)namespace.get("tableView");
		botonAnadir = (PushButton)namespace.get("botonAnadir");
		botonEliminar = (PushButton)namespace.get("botonEliminar");
		
		
		//	Hacemos un listar Categorias para obtener todas las categorias
		java.util.List<CategoriaItem> traerCategorias = new ArrayList<CategoriaItem>();
		try{
			traerCategorias = ServiceLocator.getCategoriasService().listarCategoria();
		}catch(ServiceException e){
			e.printStackTrace();
		}		
		
		//	Creamos una lista de categorias con solo las descripciones para el TableView.
		listaCategorias = null;
		
		for(CategoriaItem categoria : traerCategorias){
			String descripcion = categoria.getDescripcion();
			listaCategorias.add(descripcion);
		}
		
		listaCategorias = new LinkedList <String>();
		tableView.setTableData(listaCategorias);	
		
		
//		//	Vinculamos la tabla al componente
//		tablePane = (TablePane)namespace.get("tablePane");		
//		
//		CategoriaItem categoria = new CategoriaItem();
//		
//		try {
//			categoria = ServiceLocator.getCategoriasService().obtenerCategoria(1l);
//		} catch (ServiceException e) {
//			String mensajeRerror = e.getMessage() + "\n\nDetalles: " + e.getCause().getMessage();
//			JOptionPane.showMessageDialog(null, mensajeRerror, "Error", JOptionPane.ERROR_MESSAGE);
//		}
//		
//		// 	creamos una nueva fila
//		TablePane.Row row = new Row();
//		//	creamos el label para el Texto.
//		Label categoriaLabel = new Label();
//		//	agregamos al label el texto de la categoria
//		categoriaLabel.setText(categoria.getDescripcion());
//		//	agregamos a la fila el label.
//		row.add(categoriaLabel);
//		//	agregamos a la tabla la fila
	}

}
