package dad.recetapp.ui;

import java.net.URL;
import java.util.ArrayList;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.TipoAnotacionItem;

public class AnotacionesPane extends BoxPane implements Bindable {

	// Esta lista es una lista observable (cuando modificamos su contenido los
	// observadores,
	// como el TableView, se enteran sin necesidad de avisarles)
	private org.apache.pivot.collections.List<TipoAnotacionItem> variables;
	// Componentes para gestionar la pestaña Categoria.
	@BXML private TableView tableViewAnotacion;
	@BXML private PushButton botonAnadir;
	@BXML private PushButton botonEliminar;
	@BXML private TextInput descripcionText;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		//******* RELLENAMOS LA TABLA ********//
		// Hacemos un listar Ingredientes para obtener todas las descripciones
		java.util.List<TipoAnotacionItem> traerAnotaciones = new ArrayList<TipoAnotacionItem>();
		try {
			traerAnotaciones = ServiceLocator.getTipoAnotacionService().listarTipoAnotacion();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		// Creamos una lista de categorias para el TableView.
		variables = new org.apache.pivot.collections.ArrayList<TipoAnotacionItem>();
		for (TipoAnotacionItem tipoAnotacion : traerAnotaciones) {
			variables.add(tipoAnotacion);
		}
		tableViewAnotacion.setTableData(variables);
		
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
	 * Este metodo crea un objeto TipoAnotacionItem y lo agrega a la lista
	 * del tableview, luego invoca el servicio crearTipoAnotacion para
	 * crear una nueva tipoAnotacion en la base de datos.
	 */
	protected void onBotonAnadirPressed() {
		TipoAnotacionItem nuevo = new TipoAnotacionItem();
		nuevo.setDescripcion(descripcionText.getText());
		try {
			ServiceLocator.getTipoAnotacionService().crearTipoAnotacion(nuevo);
		} catch (ServiceException e) {
			String mensajeRerror = e.getMessage() + "\n\nDetalles: "
					+ e.getCause().getMessage();
			System.out.println(mensajeRerror);
		}
		//	Lo agregamos a la tabla.
		variables.add(nuevo);
		descripcionText.setText("");
	}

	/**
	 * Esta clase esta vinculada al botonEliminar del xml, al pulsar
	 * borra el objeto de la lista y hace un delete en la base de datos
	 * usando el nombre del ingrediente a borrar.
	 */
	protected void onBotonEliminarPressed() {
		// TODO Auto-generated method stub
		
	}

}
