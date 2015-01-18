package dad.recetapp.ui;


import java.net.URL;
import java.util.ArrayList;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.LinkedList;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;

public class VentanaPrincipalWindow extends Window implements Bindable {
	@BXML private ImageView imagen;
	
	//	Componentes para gestionar la pestaña Categoria.
	private TextInput descripcionText;
	private PushButton botonAnadir = null;
	private PushButton botonEliminar = null;
	private TableView tableView;
	private List<CategoriaItem> listaCategorias;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		
		
		//	PESTAÑA CATEGORIAS
		// 	Seguir ejemplo de http://chuwiki.chuidiang.org/index.php?title=Ejemplo_TableView_con_Apache_Pivot
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
		for(CategoriaItem categoria : traerCategorias){
			//System.out.println(categoria.getDescripcion());
			//TODO - FALLO: Al agregar cosas a la lista siempre devuelve Null y al final da un error NullPointerException.
			//listaCategorias.add(categoria);
		}
		
		//listaCategorias = new LinkedList <CategoriaItem>();
		//tableView.setTableData(listaCategorias);
	}
}
