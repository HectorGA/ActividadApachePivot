package dad.recetapp.ui;

import java.net.URL;
import java.util.ArrayList;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;

public class CategoriasPane extends TablePane implements Bindable {

	//	Nos traemos VentanaPrincipalWindows para usarla con el Prompt.
	private VentanaPrincipalWindow ventanaPrincipalWindow;
	// esta lista es una lista observable (cuando modificamos su contenido los
	// observadores,
	// como el TableView, se enteran sin necesidad de avisarles)
	private org.apache.pivot.collections.List<CategoriaItem> variables;
	// Componentes para gestionar la pestaña Categoria.
	@BXML private TableView tableViewCategoria;
	@BXML private PushButton botonAnadir;
	@BXML private PushButton botonEliminar;
	@BXML private TextInput descripcionText;

	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		
		//******* RELLENAMOS LA TABLA ********//
		// Hacemos un listar Categorias para obtener todas las categorias
		java.util.List<CategoriaItem> traerCategorias = new ArrayList<CategoriaItem>();
		try {
			traerCategorias = ServiceLocator.getCategoriasService()
					.listarCategoria();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		// Creamos una lista de categorias para el TableView.
		variables = new org.apache.pivot.collections.ArrayList<CategoriaItem>();
		for (CategoriaItem categoria : traerCategorias) {
			variables.add(categoria);
		}
		
		tableViewCategoria.setTableData(variables);
		
		//******** BOTONES *********//
		botonAnadir.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button button) {
				onBotonAnadirPressed();
			}
		});
		
		botonEliminar.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button button) {
				onBotonEliminarPressed();
			}
		});
	}
	
	/**
	 * Este metodo crea un objeto CategoriaItem y lo agrega a la lista
	 * del tableview, luego invoca el servicio crearCategoria para
	 * crear la nueva categoría en la base de datos.
	 */
	
	private void onBotonAnadirPressed() {
		CategoriaItem nueva = new CategoriaItem();
		nueva.setDescripcion(descripcionText.getText());
		//	Creamos el objeto en la base de datos.
		try {
			ServiceLocator.getCategoriasService().crearCategoria(nueva);
		} catch (ServiceException e) {
			String mensajeRerror = e.getMessage() + "\n\nDetalles: "
					+ e.getCause().getMessage();
			System.out.println(mensajeRerror);
		}
		//	Lo agregamos a la tabla.
		variables.add(nueva);
		descripcionText.setText("");
	}
	
	
	/**
	 * Esta clase esta vinculada al botonEliminar del xml al pulsar
	 * borra el objeto de la lista y hace un delete en la base de datos
	 * con la descripción de la categoria a borrar.
	 */
	protected void onBotonEliminarPressed() {
		
		//	TODO Sigue sin funcionar el Prompt lanza un error "java.lang.IllegalArgumentException: owner is null"
		//	owner es un elemento que se supone trae ventanaPrincipalWindow
		StringBuffer mensaje = new StringBuffer();
		mensaje.append("¿Desea eliminar las siguientes variables?\n\n");
		
		Sequence<?> seleccionados = tableViewCategoria.getSelectedRows();
		for (int i = 0; i < seleccionados.getLength(); i++) {
			CategoriaItem variableSeleccionada = (CategoriaItem) seleccionados.get(i);
			mensaje.append(" - " + variableSeleccionada.getDescripcion() + "\n");
		}
		
		//	No acepta el metodo para agregar la colección del ejemplo y no tengo claro por que
		//	Si creo el ArrayList primero ya el constructor del Prompt acepta la collección.
		org.apache.pivot.collections.ArrayList<String> siNo = new org.apache.pivot.collections.ArrayList<String>();
		siNo.add("Si");
		siNo.add("No");
		
		Prompt confirmar = new Prompt(MessageType.WARNING, mensaje.toString(), (Sequence<?>) siNo);
		
		confirmar.open(ventanaPrincipalWindow, new SheetCloseListener() {
			public void sheetClosed(Sheet sheet) {
				
				if (confirmar.getResult() && confirmar.getSelectedOption().equals("Sí")) {
					Sequence<?> seleccionados = tableViewCategoria.getSelectedRows();
					for (int i = 0; i < seleccionados.getLength(); i++) {
						variables.remove((CategoriaItem)seleccionados.get(i));
					}
				}
			}
		});
	}
	
	/**
	 * Este metodo recibe una ventana que luego usaremos en el Prompt.
	 * @param window
	 */
	public void setWindows(VentanaPrincipalWindow window){
		ventanaPrincipalWindow = window;
	}

	

}
