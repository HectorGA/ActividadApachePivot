package dad.recetapp.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.FillPane;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import dad.recetapp.RecetAppApplication;
import dad.recetapp.services.items.IngredienteItem;
import dad.recetapp.services.items.InstruccionItem;
import dad.recetapp.services.items.SeccionItem;

public class ComponenteReceta extends FillPane implements Bindable {

	@BXML private Button botonAnadirIngrediente, botonEditarIngrediente, botonEliminarIngrediente;
	@BXML private Button botonAnadirInstruccion, botonEditarInstruccion, botonEliminarInstruccion;
	@BXML private TextInput seccionText;
	@BXML private Button eliminarPestanaButton;
	@BXML public TableView tableViewIngredientes;
	@BXML public TableView tableViewInstrucciones;
	
	public org.apache.pivot.collections.List<IngredienteItem> ingredientes;
	public org.apache.pivot.collections.List<InstruccionItem> instrucciones;
	public Window parent;

	@Override
	public void initialize(Map<String, Object> namespace, URL location,Resources resources) {

		ingredientes = new org.apache.pivot.collections.ArrayList<IngredienteItem>();
		instrucciones = new org.apache.pivot.collections.ArrayList<InstruccionItem>();

		seccionText.getComponentKeyListeners().add(new ComponentKeyListener.Adapter() {
			@SuppressWarnings("static-access")
			@Override
			public boolean keyTyped(Component arg0, char arg1) {
				
				if (parent instanceof NuevaReceta){
					try {
						((NuevaReceta) parent).cambiarTituloPestana(seccionText.getText());
						((NuevaReceta) parent).tabPaneNuevaReceta.repaint();
					} catch (NullPointerException e) {
					}
				} else {
					try {
						((EditarReceta) parent).cambiarTituloPestana(seccionText.getText());
						((EditarReceta) parent).tabPaneEditarReceta.repaint();
					} catch (NullPointerException e) {
					}
				}
				
				return false;
			}
		});

		eliminarPestanaButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				try {
					NuevaReceta.eliminarPestanaActual();
				} catch (NullPointerException e) {
				}
				try {
					EditarReceta.eliminarPestanaActual();
				} catch (NullPointerException e) {
				}
			}
		});

		botonAnadirIngrediente.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				anadirIngrediente();
			}
		});

		botonAnadirInstruccion.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				anadirInstruccion();
			}
		});

		botonEditarIngrediente.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				editarIngrediente();
			}
		});

		botonEditarInstruccion.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				editarInstruccion();
			}
		});

		botonEliminarIngrediente.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				eliminarIngrediente();
			}
		});

		botonEliminarInstruccion.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				eliminarInstruccion();
			}
		});
	}

	private void editarIngrediente() {
		Sequence<?> seleccionados = tableViewIngredientes.getSelectedRows();
		if (seleccionados.getLength() != 1) {
			Prompt error = new Prompt("Debe seleccionar un ingrediente.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		} else {
			EditarIngredienteDialog editarIngredienteDialog = null;
			try {
				editarIngredienteDialog = (EditarIngredienteDialog) RecetAppApplication.loadWindow("dad/recetapp/ui/EditarIngredienteDialog.bxml");
				IngredienteItem ingredienteSeleccionado = null;

				for (int i = 0; i < seleccionados.getLength(); i++) {
					ingredienteSeleccionado = (IngredienteItem) seleccionados.get(i);
				}

				editarIngredienteDialog.setIngrediente(ingredienteSeleccionado);
				editarIngredienteDialog.open(this.getWindow(), new DialogCloseListener() {
					@Override
					public void dialogClosed(Dialog d, boolean arg1) {
						EditarIngredienteDialog nid = (EditarIngredienteDialog) d;
						if (nid.getAceptar()) {
							ingredientes.update(tableViewIngredientes.getLastSelectedIndex(),nid.getIngrediente());
							tableViewIngredientes.setTableData(ingredientes);
						}
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SerializationException e) {
				e.printStackTrace();
			}
		}
	}

	private void editarInstruccion() {
		Sequence<?> seleccionados = tableViewInstrucciones.getSelectedRows();
		if (seleccionados.getLength() != 1) {
			Prompt error = new Prompt("Debe seleccionar una instrucción.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		} else {
			EditarInstruccionDialog editarInstruccionDialog = null;
			try {
				editarInstruccionDialog = (EditarInstruccionDialog) RecetAppApplication.loadWindow("dad/recetapp/ui/EditarInstruccionDialog.bxml");
				InstruccionItem instruccionSeleccionada = null;

				for (int i = 0; i < seleccionados.getLength(); i++) {
					instruccionSeleccionada = (InstruccionItem) seleccionados.get(i);
				}

				editarInstruccionDialog.setInstruccion(instruccionSeleccionada);
				editarInstruccionDialog.open(this.getWindow(), new DialogCloseListener() {
					@Override
					public void dialogClosed(Dialog d, boolean arg1) {
						EditarInstruccionDialog nid = (EditarInstruccionDialog) d;

						if (nid.getAceptar()) {
							instrucciones.update(tableViewInstrucciones.getLastSelectedIndex(),nid.getInstruccion());
							tableViewInstrucciones.setTableData(instrucciones);
						}
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SerializationException e) {
				e.printStackTrace();
			}
		}
	}

	private void anadirInstruccion() {
		NuevaInstruccionDialog nuevaInstruccionDialog = null;
		try {
			nuevaInstruccionDialog = (NuevaInstruccionDialog) RecetAppApplication.loadWindow("dad/recetapp/ui/NuevaInstruccionDialog.bxml");
			nuevaInstruccionDialog.open(this.getWindow(), new DialogCloseListener() {
				@Override
				public void dialogClosed(Dialog d, boolean arg1) {
					NuevaInstruccionDialog nid = (NuevaInstruccionDialog) d;
					if (nid.getAceptar()) {
						instrucciones.add(nid.getInstruccion());
						tableViewInstrucciones.setTableData(instrucciones);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}
	}

	private void anadirIngrediente() {
		NuevoIngredienteDialog nuevoIngredienteDialog = null;
		try {
			nuevoIngredienteDialog = (NuevoIngredienteDialog) RecetAppApplication.loadWindow("dad/recetapp/ui/NuevoIngredienteDialog.bxml");		
			nuevoIngredienteDialog.open(this.getWindow(), new DialogCloseListener() {
				@Override
				public void dialogClosed(Dialog d, boolean arg1) {
					NuevoIngredienteDialog nid = (NuevoIngredienteDialog) d;
					if (nid.getAceptar()) {
						ingredientes.add(nid.getIngrediente());
						tableViewIngredientes.setTableData(ingredientes);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}
	}

	private void eliminarIngrediente() {
		Sequence<?> seleccionados = tableViewIngredientes.getSelectedRows();
		if (seleccionados.getLength() == 0) {
			Prompt error = new Prompt("Debe seleccionar un ingrediente.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		} else {
			for (int i = 0; i < seleccionados.getLength(); i++) {
				IngredienteItem ingredienteSeleccionado = (IngredienteItem) seleccionados.get(i);
				ingredientes.remove(ingredienteSeleccionado);
			}
		}
	}

	private void eliminarInstruccion() {
		Sequence<?> seleccionados = tableViewInstrucciones.getSelectedRows();
		if (seleccionados.getLength() == 0) {
			Prompt error = new Prompt("Debe seleccionar una instrucción.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		} else {
			for (int i = 0; i < seleccionados.getLength(); i++) {
				InstruccionItem instruccionSeleccionada = (InstruccionItem) seleccionados.get(i);
				instrucciones.remove(instruccionSeleccionada);
			}
		}
	}

	public SeccionItem getSeccion() {
		SeccionItem seccion = new SeccionItem();
		seccion.setNombre(seccionText.getText());
		seccion.setIngredientes(convertirListIngredientes(ingredientes));
		seccion.setInstrucciones(convertirListInstrucciones(instrucciones));
		return seccion;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static java.util.List<IngredienteItem> convertirListIngredientes(org.apache.pivot.collections.List listaUtil) {
		List listaApache = new ArrayList();
		for (int i = 0; i < listaUtil.getLength(); i++) {
			listaApache.add(listaUtil.get(i));
		}
		return listaApache;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static java.util.List<InstruccionItem> convertirListInstrucciones(org.apache.pivot.collections.List listaUtil) {
		List listaApache = new ArrayList();
		for (int i = 0; i < listaUtil.getLength(); i++) {
			listaApache.add(listaUtil.get(i));
		}
		return listaApache;
	}

	public void setParent(Window parent){
		this.parent = parent;
	}
	
	public void setSeccionText(String texto){
		this.seccionText.setText(texto);
	}
	
	public void setIngredientes(org.apache.pivot.collections.List<IngredienteItem> ingredientes){
		this.ingredientes = ingredientes;
		tableViewIngredientes.setTableData(ingredientes);
	}
	
	public void setInstrucciones(org.apache.pivot.collections.List<InstruccionItem> instrucciones){
		this.instrucciones = instrucciones;
		tableViewInstrucciones.setTableData(instrucciones);
	}
}