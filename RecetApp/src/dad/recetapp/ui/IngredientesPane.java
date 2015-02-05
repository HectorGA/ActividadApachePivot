package dad.recetapp.ui;

import java.net.URL;
import java.util.ArrayList;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.TipoIngredienteItem;

public class IngredientesPane extends TablePane implements Bindable {
	@BXML private TableView tableViewIngredientes;
	@BXML private PushButton botonAnadir;
	@BXML private PushButton botonEliminar;
	@BXML private TextInput descripcionText;
	private org.apache.pivot.collections.List<TipoIngredienteItem> ingredientes;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		java.util.List<TipoIngredienteItem> traerIngredientes = new ArrayList<TipoIngredienteItem>();
		try {
			traerIngredientes = ServiceLocator.getTiposIngredientesService().listarTipoIngrediente();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		ingredientes = new org.apache.pivot.collections.ArrayList<TipoIngredienteItem>();
		for (TipoIngredienteItem tipoIngrediente : traerIngredientes) {
			ingredientes.add(tipoIngrediente);
		}
		tableViewIngredientes.setTableData(ingredientes);
		
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
	
	protected void onBotonAnadirPressed() {
		TipoIngredienteItem nuevo = new TipoIngredienteItem();
		nuevo.setNombre(descripcionText.getText());
		try {
			ServiceLocator.getTiposIngredientesService().crearTipoIngrediente(nuevo);
		} catch (ServiceException e) {
			String mensajeRerror = e.getMessage() + "\n\nDetalles: "
					+ e.getCause().getMessage();
			System.out.println(mensajeRerror);
		}
		ingredientes.add(nuevo);
		descripcionText.setText("");
	}


	protected void onBotonEliminarPressed() {
		// TODO Auto-generated method stub
		
	}

}
