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
import dad.recetapp.services.items.TipoAnotacionItem;

public class AnotacionesPane extends TablePane implements Bindable {
	@BXML private TableView tableViewAnotacion;
	@BXML private PushButton botonAnadir;
	@BXML private PushButton botonEliminar;
	@BXML private TextInput descripcionText;
	private org.apache.pivot.collections.List<TipoAnotacionItem> anotaciones;
	private String textoSinModificar;
	
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
		
		tableViewAnotacion.getTableViewSelectionListeners().add(new TableViewSelectionListener.Adapter(){
			@Override
			public void selectedRangesChanged(TableView arg0, Sequence<Span> arg1) {
				textoSinModificar = anotaciones.get(arg0.getSelectedIndex()).getDescripcion();
			}
		});
		
		tableViewAnotacion.getTableViewRowListeners().add(new TableViewRowListener() {
			@Override
			public void rowsSorted(TableView arg0) {}
			@Override
			public void rowsRemoved(TableView arg0, int arg1, int arg2) {}
			@Override
			public void rowsCleared(TableView arg0) {}
			@Override
			public void rowUpdated(TableView tableViewAnotacion, int index) {
				if(anotaciones.get(index).getClass().equals("")){
					System.out.print("Celda vacia, recuperamos el dato ");
					anotaciones.get(index).setDescripcion(textoSinModificar);
					System.out.print(anotaciones.get(index).getDescripcion() + ".");
				}else{
					try {
						ServiceLocator.getTipoAnotacionService().modificarTipoAnotacion(anotaciones.get(index));
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
		if(!descripcionText.getText().equals("")){
			TipoAnotacionItem nuevo = new TipoAnotacionItem();
			nuevo.setDescripcion(descripcionText.getText());
			try {
				ServiceLocator.getTipoAnotacionService().crearTipoAnotacion(nuevo);
			} catch (ServiceException e) {
				String mensajeError = e.getMessage() + "\n\nDetalles: " + e.getCause().getMessage();
				System.out.println(mensajeError);
			}
			rellenarVista();
			descripcionText.setText("");
		}
	}

	protected void onBotonEliminarPressed() {
		StringBuffer mensaje = new StringBuffer();
		mensaje.append("¿Desea eliminar los siguientes Ingredientes?\n\n");
		
		Sequence<?> seleccionados = tableViewAnotacion.getSelectedRows();
		for (int i = 0; i < seleccionados.getLength(); i++) {
			TipoAnotacionItem anotacion = (TipoAnotacionItem) seleccionados.get(i);
			mensaje.append(" - " + anotacion.getDescripcion() + "\n");
		}
		
		org.apache.pivot.collections.ArrayList<String> siNo = new org.apache.pivot.collections.ArrayList<String>();
		siNo.add("Sí");
		siNo.add("No");
		
		Prompt confirmar = new Prompt(MessageType.WARNING, mensaje.toString(), (Sequence<?>) siNo);
		
		confirmar.open(this.getWindow(), new SheetCloseListener() {
			public void sheetClosed(Sheet sheet) {
				
				if (confirmar.getResult() && confirmar.getSelectedOption().equals("Sí")) {
					for (int i = seleccionados.getLength()-1; i >= 0; i--) {
						TipoAnotacionItem anotacion = (TipoAnotacionItem) seleccionados.get(i);
						try {
							ServiceLocator.getTipoAnotacionService().eliminarTipoAnotacion(anotacion.getId());
							anotaciones.remove(anotacion);
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
		java.util.List<TipoAnotacionItem> traerAnotaciones = new ArrayList<TipoAnotacionItem>();
		try {
			traerAnotaciones = ServiceLocator.getTipoAnotacionService().listarTipoAnotacion();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		anotaciones.clear();
		for (TipoAnotacionItem anotacion : traerAnotaciones) {
			anotaciones.add(anotacion);
		}
	}

}
