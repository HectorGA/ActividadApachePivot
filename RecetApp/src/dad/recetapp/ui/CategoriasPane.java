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
import dad.recetapp.services.items.CategoriaItem;

public class CategoriasPane extends TablePane implements Bindable {

	@BXML private TableView tableViewCategoria;
	@BXML private PushButton botonAnadir;
	@BXML private PushButton botonEliminar;
	@BXML private TextInput descripcionText;
	private org.apache.pivot.collections.List<CategoriaItem> categorias;
	private String textoSinModificar;

	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		categorias = new org.apache.pivot.collections.ArrayList<CategoriaItem>();
		rellenarVista();
		tableViewCategoria.setTableData(categorias);
		tableViewCategoria.getTableViewSelectionListeners().add(new TableViewSelectionListener.Adapter(){
			@Override
			public void selectedRangesChanged(TableView arg0, Sequence<Span> arg1) {
				textoSinModificar = categorias.get(arg0.getSelectedIndex()).getDescripcion();
				//System.out.println("Seleccionado " + arg0.getSelectedIndex());
			}
		});
		tableViewCategoria.getTableViewRowListeners().add(new TableViewRowListener(){
			@Override
			public void rowInserted(TableView arg0, int arg1) {}
			@Override
			public void rowUpdated(TableView tableViewCategoria, int index) {
				if(categorias.get(index).getDescripcion().equals("")){
					System.out.print("Celda vacia, recuperamos el dato ");
					categorias.get(index).setDescripcion(textoSinModificar);
					System.out.print(categorias.get(index).getDescripcion() + ".");
				}else{
					try {
						ServiceLocator.getCategoriasService().modificarCategoria(categorias.get(index));
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void rowsCleared(TableView arg0) {}
			@Override
			public void rowsRemoved(TableView arg0, int arg1, int arg2) {}
			@Override
			public void rowsSorted(TableView arg0) {}
		});

		botonAnadir.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button button) {
				onBotonAnadirPressed();
			}
		});
		
		botonEliminar.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button button) {
				try {
					onBotonEliminarPressed();
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void onBotonAnadirPressed() {
		if(!descripcionText.getText().equals("")){
			CategoriaItem nueva = new CategoriaItem();
			nueva.setDescripcion(descripcionText.getText());
			try {
				ServiceLocator.getCategoriasService().crearCategoria(nueva);
			} catch (ServiceException e) {
				String mensajeError = e.getMessage() + "\n\nDetalles: " + e.getCause().getMessage();
				System.out.println(mensajeError);
			}
			rellenarVista();
			descripcionText.setText("");
		}
	}
	
	protected void onBotonEliminarPressed() throws ServiceException {
		StringBuffer mensaje = new StringBuffer();
		mensaje.append("¿Desea eliminar las siguientes Categorias?\n\n");
		
		Sequence<?> seleccionados = tableViewCategoria.getSelectedRows();
		for (int i = 0; i < seleccionados.getLength(); i++) {
			CategoriaItem categoria = (CategoriaItem) seleccionados.get(i);
			mensaje.append(" - " + categoria.getDescripcion() + "\n");
		}
		
		org.apache.pivot.collections.ArrayList<String> siNo = new org.apache.pivot.collections.ArrayList<String>();
		siNo.add("Sí");
		siNo.add("No");
		
		Prompt confirmar = new Prompt(MessageType.WARNING, mensaje.toString(), (Sequence<?>) siNo);
		
		confirmar.open(this.getWindow(), new SheetCloseListener() {
			public void sheetClosed(Sheet sheet) {
				
				if (confirmar.getResult() && confirmar.getSelectedOption().equals("Sí")) {
					for (int i = seleccionados.getLength()-1; i >= 0; i--) {
						CategoriaItem categoria = (CategoriaItem) seleccionados.get(i);
						try {
							ServiceLocator.getCategoriasService().eliminarCategoria(categoria.getId());
							categorias.remove(categoria);
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
		java.util.List<CategoriaItem> traerCategorias = new ArrayList<CategoriaItem>();
		try {
			traerCategorias = ServiceLocator.getCategoriasService().listarCategoria();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		categorias.clear();
		for (CategoriaItem categoria : traerCategorias) {
			categorias.add(categoria);
		}
	}
}
