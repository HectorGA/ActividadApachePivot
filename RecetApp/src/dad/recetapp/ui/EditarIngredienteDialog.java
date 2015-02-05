package dad.recetapp.ui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.IngredienteItem;
import dad.recetapp.services.items.MedidaItem;
import dad.recetapp.services.items.TipoIngredienteItem;

public class EditarIngredienteDialog extends Dialog implements Bindable {
	@BXML private EditarIngredienteDialog editarIngredienteDialog;
	@BXML private Button editarIngredienteDialogButton, cancelarIngredienteDialogButton;
	@BXML private static ListButton medidaComboBox, ingredienteComboBox;
	@BXML private TextInput cantidadText;
	private Boolean aceptar = false;
	private static org.apache.pivot.collections.List<MedidaItem> medidas;
	private static org.apache.pivot.collections.List<TipoIngredienteItem> tiposIngredientes;
	private IngredienteItem ingrediente;

	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		ingrediente = new IngredienteItem();

		try {
			cargarCombos();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		cancelarIngredienteDialogButton.getButtonPressListeners().add(new ButtonPressListener() {
					@Override
					public void buttonPressed(Button arg0) {
						editarIngredienteDialog.close();
					}
				});

		editarIngredienteDialogButton.getButtonPressListeners().add(new ButtonPressListener() {
					@Override
					public void buttonPressed(Button arg0) {
						if (cantidadText.getText().equals("") || medidaComboBox.getSelectedItem().toString().equals("<Seleccione la medida>") || ingredienteComboBox.getSelectedItem().toString().equals("<Seleccione el tipo de instruccion>")) {
							Prompt error = new Prompt(MessageType.ERROR, "Faltan algunos campos por rellenar", null);
							error.open(getWindow());
						} else {
							ingrediente.setCantidad(Integer.valueOf(cantidadText.getText()));
							ingrediente.setMedida((MedidaItem) medidaComboBox.getSelectedItem());
							ingrediente.setTipo((TipoIngredienteItem) ingredienteComboBox.getSelectedItem());
							aceptar = true;
							close();
						}
					}
				});
	}

	@SuppressWarnings("unchecked")
	public static void cargarCombos() throws ServiceException {
		medidas = convertirList(ServiceLocator.getMedidasService().listarMedidas());
		medidaComboBox.setListData(medidas);
		tiposIngredientes = convertirList(ServiceLocator.getTiposIngredientesService().listarTipoIngrediente());
		ingredienteComboBox.setListData(tiposIngredientes);
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
		medidaComboBox.setSelectedIndex(0);
		ingredienteComboBox.setSelectedIndex(0);
	}
}