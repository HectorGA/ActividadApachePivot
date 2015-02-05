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
import dad.recetapp.services.items.MedidaItem;

public class MedidasPane extends TablePane implements Bindable {
	@BXML private TableView tableViewMedidas;
	@BXML private PushButton botonAnadir;
	@BXML private PushButton botonEliminar;
	@BXML private TextInput nombreText;
	@BXML private TextInput abreviaturaText;
	private org.apache.pivot.collections.List<MedidaItem> medidas;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		java.util.List<MedidaItem> traerMedidas = new ArrayList<MedidaItem>();
		try {
			traerMedidas = ServiceLocator.getMedidasService().listarMedidas();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		medidas = new org.apache.pivot.collections.ArrayList<MedidaItem>();
		for (MedidaItem medida : traerMedidas) {
			medidas.add(medida);
		}
		tableViewMedidas.setTableData(medidas);
		
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
		MedidaItem nuevo = new MedidaItem();
		nuevo.setNombre(nombreText.getText());
		nuevo.setAbreviatura(abreviaturaText.getText());
		try {
			ServiceLocator.getMedidasService().crearMedida(nuevo);
		} catch (ServiceException e) {
			String mensajeRerror = e.getMessage() + "\n\nDetalles: "
					+ e.getCause().getMessage();
			System.out.println(mensajeRerror);
		}
		medidas.add(nuevo);
		nombreText.setText("");
	}

	protected void onBotonEliminarPressed() {
		// TODO Auto-generated method stub
		
	}
}
