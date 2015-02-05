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
import org.apache.pivot.wtk.Span;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TableViewRowListener;
import org.apache.pivot.wtk.TableViewSelectionListener;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.TipoIngredienteItem;

public class IngredientesPane extends TablePane implements Bindable {
	@BXML private TableView tableViewIngredientes;
	@BXML private PushButton botonAnadir;
	@BXML private PushButton botonEliminar;
	@BXML private TextInput nombreText;
	private org.apache.pivot.collections.List<TipoIngredienteItem> ingredientes;
	private String textoSinModificar;
	
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
		
		tableViewIngredientes.getTableViewSelectionListeners().add(new TableViewSelectionListener.Adapter(){
			@Override
			public void selectedRangesChanged(TableView arg0, Sequence<Span> arg1) {
				textoSinModificar = ingredientes.get(arg0.getSelectedIndex()).getNombre();
			}
		});
		tableViewIngredientes.getTableViewRowListeners().add(new TableViewRowListener() {
			@Override
			public void rowsSorted(TableView arg0) {}
			@Override
			public void rowsRemoved(TableView arg0, int arg1, int arg2) {}
			@Override
			public void rowsCleared(TableView arg0) {}
			@Override
			public void rowUpdated(TableView tableViewIngredientes, int index) {
				if(ingredientes.get(index).getNombre().equals("")){
					System.out.print("Celda vacia, recuperamos el dato ");
					ingredientes.get(index).setNombre(textoSinModificar);
					System.out.print(ingredientes.get(index).getNombre() + ".");
				}else{
					try {
						ServiceLocator.getTiposIngredientesService().modificarTipoIngrediente(ingredientes.get(index));
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void rowInserted(TableView arg0, int arg1) {}
		});		
		
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
		if(!nombreText.getText().equals("")){
			TipoIngredienteItem nuevo = new TipoIngredienteItem();
			nuevo.setNombre(nombreText.getText());
		
			try {
				ServiceLocator.getTiposIngredientesService().crearTipoIngrediente(nuevo);
			} catch (ServiceException e) {
				String mensajeRerror = e.getMessage() + "\n\nDetalles: "
						+ e.getCause().getMessage();
				System.out.println(mensajeRerror);
			}
			rellenarVista();
			nombreText.setText("");
		}
	}

	protected void onBotonEliminarPressed() {
		StringBuffer mensaje = new StringBuffer();
		mensaje.append("¿Desea eliminar los siguientes Ingredientes?\n\n");
		
		Sequence<?> seleccionados = tableViewIngredientes.getSelectedRows();
		for (int i = 0; i < seleccionados.getLength(); i++) {
			TipoIngredienteItem ingrediente = (TipoIngredienteItem) seleccionados.get(i);
			mensaje.append(" - " + ingrediente.getNombre() + "\n");
		}
		
		org.apache.pivot.collections.ArrayList<String> siNo = new org.apache.pivot.collections.ArrayList<String>();
		siNo.add("Sí");
		siNo.add("No");
		
		Prompt confirmar = new Prompt(MessageType.WARNING, mensaje.toString(), (Sequence<?>) siNo);
		
		confirmar.open(this.getWindow(), new SheetCloseListener() {
			public void sheetClosed(Sheet sheet) {
				
				if (confirmar.getResult() && confirmar.getSelectedOption().equals("Sí")) {
					for (int i = seleccionados.getLength()-1; i >= 0; i--) {
						TipoIngredienteItem ingrediente = (TipoIngredienteItem) seleccionados.get(i);
						try {
							ServiceLocator.getTiposIngredientesService().eliminarTipoIngrediente(ingrediente.getId());
							ingredientes.remove(ingrediente);
						} catch (ServiceException e) {
							Prompt error = new Prompt(MessageType.ERROR, e.getMessage(), null);
							error.open(getWindow());
							//e.printStackTrace();
						} catch(IndexOutOfBoundsException e) { }
					}
				}
			}
		});
	}
	
	private void rellenarVista() {
		java.util.List<TipoIngredienteItem> traerIngredientes = new ArrayList<TipoIngredienteItem>();
		try {
			traerIngredientes = ServiceLocator.getTiposIngredientesService().listarTipoIngrediente();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		ingredientes.clear();
		for (TipoIngredienteItem ingrediente : traerIngredientes) {
			ingredientes.add(ingrediente);
		}
	}
}
