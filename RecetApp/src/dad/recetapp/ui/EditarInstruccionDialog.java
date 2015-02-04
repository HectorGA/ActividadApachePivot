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
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.services.items.InstruccionItem;

public class EditarInstruccionDialog extends Dialog implements Bindable {

	private Boolean aceptar = false;

	@BXML
	private EditarInstruccionDialog editarInstruccionDialog;
	@BXML
	private Button editarInstruccionButton, cancelarInstruccionButton;
	@BXML
	private TextInput ordenText;
	@BXML
	private TextArea descripcionText;
	@BXML
	private Label errorLabel;

	private InstruccionItem instruccion;

	public void initialize(Map<String, Object> namespace, URL location,Resources resources) {

		cancelarInstruccionButton.getButtonPressListeners().add(
				new ButtonPressListener() {
					@Override
					public void buttonPressed(Button arg0) {
						editarInstruccionDialog.close();
					}
				});

		editarInstruccionButton.getButtonPressListeners().add(
				new ButtonPressListener() {
					@Override
					public void buttonPressed(Button arg0) {
						if (ordenText.getText().equals("")
								|| descripcionText.getText().equals("")) {
							errorLabel
							.setText("Debe rellenar todos los campos");
						} else {
							instruccion.setOrden(Integer.valueOf(ordenText.getText()));
							instruccion.setDescripcion(descripcionText.getText());

							aceptar = true;
							close();
						}
					}
				});
	}

	public InstruccionItem getInstruccion(){
		return this.instruccion;
	}

	public Boolean getAceptar() {
		return aceptar;
	}

	public void setInstruccion(InstruccionItem instruccion){
		this.instruccion = instruccion;
		ordenText.setText(String.valueOf(this.instruccion.getOrden()));
		descripcionText.setText(this.instruccion.getDescripcion());
	}
}
