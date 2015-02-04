package dad.recetapp.ui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.IngredienteItem;
import dad.recetapp.services.items.MedidaItem;
import dad.recetapp.services.items.TipoIngredienteItem;

public class EditarIngredienteDialog extends Dialog implements Bindable {

	private Boolean aceptar = false;
	
	@BXML
	private EditarIngredienteDialog editarIngredienteDialog;
	@BXML
	private Button editarIngredienteDialogButton,
			cancelarIngredienteDialogButton;
	@BXML
	private static ListButton medidasListButton, tipoIngredienteListButton;
	@BXML
	private TextInput cantidadText;
	@BXML
	private Label errorLabel;

	private static org.apache.pivot.collections.List<MedidaItem> medidasBD;
	private static org.apache.pivot.collections.List<TipoIngredienteItem> tiposIngredientesBD;

	private IngredienteItem ingrediente;

	@Override
	public void initialize(Map<String, Object> namespace, URL location,
			Resources resources) {

		ingrediente = new IngredienteItem();

		try {
			cargarCombos();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		cancelarIngredienteDialogButton.getButtonPressListeners().add(
				new ButtonPressListener() {
					@Override
					public void buttonPressed(Button arg0) {
						editarIngredienteDialog.close();
					}
				});

		editarIngredienteDialogButton.getButtonPressListeners().add(
				new ButtonPressListener() {
					@Override
					public void buttonPressed(Button arg0) {
						if (cantidadText.getText().equals("")
								|| medidasListButton.getSelectedItem()
										.toString()
										.equals("<Seleccione la medida>")
								|| tipoIngredienteListButton
										.getSelectedItem()
										.toString()
										.equals("<Seleccione el tipo de instruccion>")) {
							errorLabel
									.setText("Debe rellenar todos los campos");
						} else {
							ingrediente.setCantidad(Integer.valueOf(cantidadText.getText()));
							ingrediente.setMedida((MedidaItem) medidasListButton.getSelectedItem());
							ingrediente.setTipo((TipoIngredienteItem) tipoIngredienteListButton.getSelectedItem());

							aceptar = true;
							close();
						}
					}
				});
	}

	@SuppressWarnings("unchecked")
	public static void cargarCombos() throws ServiceException {
		medidasBD = convertirList(ServiceLocator.getMedidasService()
				.listarMedidas());
		medidasListButton.setListData(medidasBD);
		// ----------------------------------------------------
		tiposIngredientesBD = convertirList(ServiceLocator
				.getTiposIngredientesService().listarTipoIngrediente());
		tipoIngredienteListButton.setListData(tiposIngredientesBD);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static org.apache.pivot.collections.List convertirList(
			java.util.List<?> listaUtil) {
		org.apache.pivot.collections.List listaApache = new org.apache.pivot.collections.ArrayList();
		for (int i = 0; i < listaUtil.size(); i++) {
			listaApache.add(listaUtil.get(i));
		}
		return listaApache;
	}
	
	public IngredienteItem getIngrediente(){
		return this.ingrediente;
	}

	public Boolean getAceptar() {
		return aceptar;
	}
	
	public void setIngrediente(IngredienteItem ingrediente){
		this.ingrediente = ingrediente;
		cantidadText.setText(String.valueOf(this.ingrediente.getCantidad()));
		medidasListButton.setSelectedIndex(0);
		tipoIngredienteListButton.setSelectedIndex(0);
	}

}