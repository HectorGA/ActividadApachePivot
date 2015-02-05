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
import dad.recetapp.services.items.TipoAnotacionItem;

public class AnotacionesPane extends TablePane implements Bindable {
	@BXML private TableView tableViewAnotacion;
	@BXML private PushButton botonAnadir;
	@BXML private PushButton botonEliminar;
	@BXML private TextInput descripcionText;
	private org.apache.pivot.collections.List<TipoAnotacionItem> anotaciones;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		java.util.List<TipoAnotacionItem> traerAnotaciones = new ArrayList<TipoAnotacionItem>();
		try {
			traerAnotaciones = ServiceLocator.getTipoAnotacionService().listarTipoAnotacion();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		anotaciones = new org.apache.pivot.collections.ArrayList<TipoAnotacionItem>();
		for (TipoAnotacionItem tipoAnotacion : traerAnotaciones) {
			anotaciones.add(tipoAnotacion);
		}
		tableViewAnotacion.setTableData(anotaciones);
		
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
		TipoAnotacionItem nuevo = new TipoAnotacionItem();
		nuevo.setDescripcion(descripcionText.getText());
		try {
			ServiceLocator.getTipoAnotacionService().crearTipoAnotacion(nuevo);
		} catch (ServiceException e) {
			String mensajeRerror = e.getMessage() + "\n\nDetalles: "
					+ e.getCause().getMessage();
			System.out.println(mensajeRerror);
		}
		anotaciones.add(nuevo);
		descripcionText.setText("");
	}

	protected void onBotonEliminarPressed() {
		// TODO Auto-generated method stub
		
	}

}
