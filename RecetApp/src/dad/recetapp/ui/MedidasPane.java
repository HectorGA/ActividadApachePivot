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
import dad.recetapp.services.items.MedidaItem;

public class MedidasPane extends TablePane implements Bindable {
	@BXML private TableView tableViewMedidas;
	@BXML private PushButton botonAnadir;
	@BXML private PushButton botonEliminar;
	@BXML private TextInput nombreText;
	@BXML private TextInput abreviaturaText;
	private org.apache.pivot.collections.List<MedidaItem> medidas;
	private String nombreSinModificar, abreviaturaSinModificar;
	
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
		
		tableViewMedidas.getTableViewSelectionListeners().add(new TableViewSelectionListener.Adapter(){
			@Override
			public void selectedRangesChanged(TableView arg0, Sequence<Span> arg1) {
				nombreSinModificar = medidas.get(arg0.getSelectedIndex()).getNombre();
				abreviaturaSinModificar = medidas.get(arg0.getSelectedIndex()).getAbreviatura();
			}
		});
		
		tableViewMedidas.getTableViewRowListeners().add(new TableViewRowListener() {
			@Override
			public void rowsSorted(TableView arg0) {}
			@Override
			public void rowsRemoved(TableView arg0, int arg1, int arg2) {}
			@Override
			public void rowsCleared(TableView arg0) {}
			@Override
			public void rowUpdated(TableView tableViewMedidas, int index) {
				if(medidas.get(index).getNombre().equals("")){
					System.out.print("Celda vacia, recuperamos el dato ");
					medidas.get(index).setNombre(nombreSinModificar);
					medidas.get(index).setAbreviatura(abreviaturaSinModificar);
					System.out.print(medidas.get(index).getNombre() + ".");
				}else{
					try {
						ServiceLocator.getMedidasService().modificarMedida(medidas.get(index));
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
			MedidaItem nuevo = new MedidaItem();
			nuevo.setNombre(nombreText.getText());
			nuevo.setAbreviatura(abreviaturaText.getText());
			
			try {
				ServiceLocator.getMedidasService().crearMedida(nuevo);
			} catch (ServiceException e) {
				String mensajeError = e.getMessage() + "\n\nDetalles: " + e.getCause().getMessage();
				System.out.println(mensajeError);
			}
			rellenarVista();
			nombreText.setText("");
			abreviaturaText.setText("");
		}
	}

	protected void onBotonEliminarPressed() {
		StringBuffer mensaje = new StringBuffer();
		mensaje.append("¿Desea eliminar los siguientes Ingredientes?\n\n");
		
		Sequence<?> seleccionados = tableViewMedidas.getSelectedRows();
		for (int i = 0; i < seleccionados.getLength(); i++) {
			MedidaItem medida = (MedidaItem) seleccionados.get(i);
			mensaje.append(" - " + medida.getAbreviatura() + "\n");
		}
		
		org.apache.pivot.collections.ArrayList<String> siNo = new org.apache.pivot.collections.ArrayList<String>();
		siNo.add("Sí");
		siNo.add("No");
		
		Prompt confirmar = new Prompt(MessageType.WARNING, mensaje.toString(), (Sequence<?>) siNo);
		
		confirmar.open(this.getWindow(), new SheetCloseListener() {
			public void sheetClosed(Sheet sheet) {
				
				if (confirmar.getResult() && confirmar.getSelectedOption().equals("Sí")) {
					for (int i = seleccionados.getLength()-1; i >= 0; i--) {
						MedidaItem medida = (MedidaItem) seleccionados.get(i);
						try {
							ServiceLocator.getMedidasService().eliminarMedida(medida.getId());
							medidas.remove(medida);
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
		java.util.List<MedidaItem> traerMedidas = new ArrayList<MedidaItem>();
		try {
			traerMedidas = ServiceLocator.getMedidasService().listarMedidas();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		medidas.clear();
		for (MedidaItem medida : traerMedidas) {
			medidas.add(medida);
		}
	}
}
