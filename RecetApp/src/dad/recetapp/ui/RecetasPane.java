package dad.recetapp.ui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.collections.List;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;
import dad.recetapp.services.items.RecetaListItem;

public class RecetasPane extends TablePane implements Bindable {

	@BXML
	private TableView tableViewRecetas;
	private org.apache.pivot.collections.List<RecetaListItem> recetas;
	private org.apache.pivot.collections.List<RecetaListItem> lista;
	@BXML
	private static ListButton categoriasListButton;
	private static org.apache.pivot.collections.List<CategoriaItem> categoriasBD;
	@BXML
	private Button botonAnadir;
	@BXML
	private Button botonEliminar;
	@BXML
	private Button botonEditar;

	@SuppressWarnings({ "unchecked" })
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		
		recetas = new org.apache.pivot.collections.ArrayList<RecetaListItem>();

		try {
			lista = convertirList(ServiceLocator.getRecetasService().listarRecetas());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		for (RecetaListItem l : lista) {
			recetas.add(l);
		}
		tableViewRecetas.setTableData(recetas);
		
		try {
			recargarCategoriaListButton();

		} catch (ServiceException e) {
			e.printStackTrace();
		}
		

		botonAnadir.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				onAnadirRecetaButtonActionPerformed();
			}
		});

		botonEliminar.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				onEliminarRecetaButtonActionPerformed();
			}
		});

		botonEditar.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				onEditarRecetaButtonActionPerformed();
			}
		});
		
	}

	protected void onEditarRecetaButtonActionPerformed() {
		/*
		Sequence<?> seleccionados = tableView.getSelectedRows();
		if(seleccionados.getLength() == 0){
			Prompt error = new Prompt(MessageType.ERROR, "Debe selecionar una receta", new ArrayList<String>("OK"));
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {}
			});
		} else {
			EditarRecetaWindow editarRecetaWindow = null;
			try {
				editarRecetaWindow = (EditarRecetaWindow) RecetappApplication.loadWindow("dad/recetapp/ui/EditarRecetaWindow.bxml");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SerializationException e) {
				e.printStackTrace();
			}
			editarRecetaWindow.open(getDisplay());
		}
		*/
	}

	protected void onAnadirRecetaButtonActionPerformed() {
		/*
		NuevaRecetaWindow nuevaRecetaWindow = null;

		try {
			nuevaRecetaWindow = (NuevaRecetaWindow) RecetappApplication.loadWindow("dad/recetapp/ui/NuevaRecetaWindow.bxml");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		nuevaRecetaWindow.open(getDisplay());
		*/
	}

	@SuppressWarnings("unchecked")
	public static void recargarCategoriaListButton() throws ServiceException {
		
		CategoriaItem categoriaTitle = new CategoriaItem();
		categoriaTitle.setId(null);
		categoriaTitle.setDescripcion("<Categorías>");
		categoriasBD = convertirList(ServiceLocator.getCategoriasService().listarCategoria());
		categoriasBD.insert(categoriaTitle, 0);
		categoriasListButton.setListData(categoriasBD);
		categoriasListButton.setSelectedItem(categoriaTitle);
		
	}

	protected void onEliminarRecetaButtonActionPerformed() {
		/*
		Sequence<?> seleccionados = tableView.getSelectedRows();
		if(seleccionados.getLength() == 0){
			Prompt error = new Prompt(MessageType.ERROR, "Debe selecionar una receta", new ArrayList<String>("OK"));
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {}
			});
		} else {
			StringBuffer mensaje = new StringBuffer();
			mensaje.append("¿Desea eliminar las siguientes recetas?\n\n");

			for (int i = 0; i < seleccionados.getLength(); i++) {
				RecetaListItem recetaSeleccionada = (RecetaListItem) seleccionados.get(i);
				mensaje.append(" - " + recetaSeleccionada.getNombre() + "\n");
			}

			Prompt confirmar = new Prompt(MessageType.WARNING, mensaje.toString(), new ArrayList<String>("Sí", "No"));
			confirmar.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {

					if (confirmar.getResult() && confirmar.getSelectedOption().equals("Sí")) {
						Sequence<?> seleccionados = tableView.getSelectedRows();
						for (int i = 0; i < seleccionados.getLength(); i++) {
							try {
								RecetaListItem recetaSeleccionada = (RecetaListItem) seleccionados.get(i);
								variables.remove(recetaSeleccionada);
								ServiceLocator.getRecetasService().eliminarReceta(recetaSeleccionada.getId());
								RecetappFrame.setNumReceta();
							} catch (ServiceException e) {
								e.printStackTrace();
							}
						}	
					}			
				}
			});
		}
		*/
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static List convertirList(java.util.List<?> listaUtil){
		List listaApache = new ArrayList();
		for(int i = 0; i<listaUtil.size(); i++){
			listaApache.add(listaUtil.get(i));
		}
		return listaApache;
	}
	 
}