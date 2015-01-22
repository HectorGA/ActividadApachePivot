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

	/* esta lista es una lista observable (cuando modificamos su contenido los observadores,
	*como el TableView, se enteran sin necesidad de avisarles)*/
	private org.apache.pivot.collections.List<CategoriaItem> categorias;
	private String textoSinModificar;
	// Componentes para gestionar la pestaña Categoria.
	@BXML private TableView tableViewCategoria;
	@BXML private PushButton botonAnadir;
	@BXML private PushButton botonEliminar;
	@BXML private TextInput descripcionText;

	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		
		categorias = new org.apache.pivot.collections.ArrayList<CategoriaItem>();
		rellenarVista();
		tableViewCategoria.setTableData(categorias);
		tableViewCategoria.getTableViewSelectionListeners().add(new TableViewSelectionListener.Adapter(){
			@Override
			public void selectedRangesChanged(TableView arg0, Sequence<Span> arg1) {
				textoSinModificar = categorias.get(arg0.getSelectedIndex()).getDescripcion();
				System.out.println("Seleccionado " + arg0.getSelectedIndex());
			}
		});
		tableViewCategoria.getTableViewRowListeners().add(new TableViewRowListener(){
			@Override
			public void rowInserted(TableView arg0, int arg1) {}
			@Override
			public void rowUpdated(TableView tableViewCategoria, int index) {
				// CUANDO MODIFICAMOS UNA FILA.
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
		
		//******** BOTONES *********//
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

	/**
	 * Este metodo crea un objeto CategoriaItem y lo agrega a la lista
	 * del tableview, luego invoca el servicio crearCategoria para
	 * crear la nueva categoría en la base de datos.
	 */
	private void onBotonAnadirPressed() {
		if(!descripcionText.getText().equals("")){
			CategoriaItem nueva = new CategoriaItem();
			nueva.setDescripcion(descripcionText.getText());
			//	Creamos el objeto en la base de datos.
			try {
				ServiceLocator.getCategoriasService().crearCategoria(nueva);
			} catch (ServiceException e) {
				String mensajeRerror = e.getMessage() + "\n\nDetalles: "
						+ e.getCause().getMessage();
				System.out.println(mensajeRerror);
			}
			rellenarVista();
			descripcionText.setText("");
		}		
	}
	
	/**
	 * Esta clase esta vinculada al botonEliminar del xml al pulsar
	 * borra el objeto de la lista y hace un delete en la base de datos
	 * con la descripción de la categoria a borrar.
	 * @throws ServiceException 
	 */
	protected void onBotonEliminarPressed() throws ServiceException {

		StringBuffer mensaje = new StringBuffer();
		mensaje.append("¿Desea eliminar las siguientes Categorias?\n\n");
		
		Sequence<?> seleccionados = tableViewCategoria.getSelectedRows();
		for (int i = 0; i < seleccionados.getLength(); i++) {
			CategoriaItem variableSeleccionada = (CategoriaItem) seleccionados.get(i);
			mensaje.append(" - " + variableSeleccionada.getDescripcion() + "\n");
		}
		
		org.apache.pivot.collections.ArrayList<String> siNo = new org.apache.pivot.collections.ArrayList<String>();
		siNo.add("Sí");
		siNo.add("No");
		
		Prompt confirmar = new Prompt(MessageType.WARNING, mensaje.toString(), (Sequence<?>) siNo);
		
		confirmar.open(this.getWindow(), new SheetCloseListener() {
			public void sheetClosed(Sheet sheet) {
				
				if (confirmar.getResult() && confirmar.getSelectedOption().equals("Sí")) {
					for (int i = 0; i < seleccionados.getLength(); i++) {
						CategoriaItem variableSeleccionada = (CategoriaItem) seleccionados.get(i);
						try {
							ServiceLocator.getCategoriasService().eliminarCategoria(variableSeleccionada.getId());
						} catch (ServiceException e) {
							e.printStackTrace();
						}
						categorias.remove(variableSeleccionada);
					}
				}
			}
		});
	}
	
	/**
	 * Metodo que rellena la vista del TableView
	 * @param traerCategorias esta variable trae una colección de objetos a una lista de Java.util
	 * que luego recorremos para rellenar la lista de apache pivot.
	 * @param categorias es la vista en la que se fija el tableView para colocar sus datos.
	 */
	private void rellenarVista() {
		//******* RELLENAMOS LA TABLA ********//
		// Hacemos un listar Categorias para obtener todas las categorias
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
