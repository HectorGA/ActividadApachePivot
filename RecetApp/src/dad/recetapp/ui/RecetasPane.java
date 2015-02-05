package dad.recetapp.ui;

import java.io.IOException;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.FillPane;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.ListButtonSelectionListener;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.Spinner;
import org.apache.pivot.wtk.SpinnerSelectionListener;
import org.apache.pivot.wtk.SplitPane;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.RecetAppApplication;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;
import dad.recetapp.services.items.RecetaListItem;

public class RecetasPane extends FillPane implements Bindable {

	@BXML private SplitPane splitPane;
	@BXML public static TableView tableViewRecetas;
	@BXML private static ListButton categoriasComboBox;
	@BXML private Button botonAnadir;
	@BXML private Button botonEliminar;
	@BXML private Button botonEditar;
	@BXML private TextInput nombreText;
	@BXML private Spinner spinnerMinutos, spinnerSegundos;
	public static org.apache.pivot.collections.List<RecetaListItem> recetas;
	private org.apache.pivot.collections.List<RecetaListItem> filtroRecetas;
	private static org.apache.pivot.collections.List<CategoriaItem> categorias;

	@SuppressWarnings({ "unchecked" })
	@Override
	public void initialize(Map<String, Object> namespace, URL location,Resources resources) {
		recetas = new org.apache.pivot.collections.ArrayList<RecetaListItem>();
		filtrarDatos();

		try {
			filtroRecetas = convertirList(ServiceLocator.getRecetasService().listarRecetas());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		for (RecetaListItem recetaItem : filtroRecetas) {
			recetas.add(recetaItem);
		}
		tableViewRecetas.setTableData(recetas);
		try {
			cargarComboCategorias();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		botonAnadir.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				onBotonAnadirPressed();
			}
		});

		botonEliminar.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				onBotonEliminarPressed();
			}
		});

		botonEditar.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				onBotonEditarPressed();
			}
		});
	}

	protected void onBotonAnadirPressed() {
		NuevaReceta nuevaReceta = null;

		try {
			nuevaReceta = (NuevaReceta) RecetAppApplication.loadWindow("dad/recetapp/ui/nuevaReceta.bxml");
			nuevaReceta.setVariables(recetas);
			nuevaReceta.open(getDisplay());
			MainWindow.setNumReceta();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		recargarTabla();
	}

	protected void onBotonEditarPressed() {
		Sequence<?> seleccionados = tableViewRecetas.getSelectedRows();
		if (seleccionados.getLength() == 0 || seleccionados.getLength() > 1) {
			Prompt error = new Prompt("Debe seleccionar una receta.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		} else {
			EditarReceta editarReceta = null;
			try {
				editarReceta = (EditarReceta) RecetAppApplication.loadWindow("dad/recetapp/ui/editarReceta.bxml");
				editarReceta.setReceta((RecetaListItem)tableViewRecetas.getSelectedRow());
				editarReceta.open(getDisplay());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SerializationException e) {
				e.printStackTrace();
			}
		}
		recargarTabla();
	}

	protected void onBotonEliminarPressed() {
		Sequence<?> seleccionados = tableViewRecetas.getSelectedRows();
		if (seleccionados.getLength() == 0) {
			Prompt error = new Prompt("Debe seleccionar una receta.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
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
						for (int i = 0; i < seleccionados.getLength(); i++) {
							try {
								RecetaListItem recetaSeleccionada = (RecetaListItem) seleccionados.get(i);
								recetas.remove(recetaSeleccionada);
								ServiceLocator.getRecetasService().eliminarReceta(recetaSeleccionada.getId());
								MainWindow.setNumReceta();
							} catch (ServiceException e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
		}
		recargarTabla();
	}

	@SuppressWarnings("unchecked")
	public static void cargarComboCategorias() throws ServiceException {
		CategoriaItem categoriaItem = new CategoriaItem();
		categoriaItem.setId(null);
		categoriaItem.setDescripcion("Todas");
		categorias = convertirList(ServiceLocator.getCategoriasService().listarCategoria());
		categorias.insert(categoriaItem, 0);
		categoriasComboBox.setListData(categorias);
		categoriasComboBox.setSelectedItem(categoriaItem);
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

	protected void filtrarDatos() {
		spinnerSegundos.getSpinnerSelectionListeners().add(new SpinnerSelectionListener() {
					@Override
					public void selectedItemChanged(Spinner arg0, Object arg1) {
						filtroTabla();
					}

					@Override
					public void selectedIndexChanged(Spinner arg0, int arg1) {
						filtroTabla();
					}
				});

		spinnerMinutos.getSpinnerSelectionListeners().add(new SpinnerSelectionListener() {
					@Override
					public void selectedItemChanged(Spinner arg0, Object arg1) {
						filtroTabla();
					}

					@Override
					public void selectedIndexChanged(Spinner arg0, int arg1) {
						filtroTabla();
					}
				});

		nombreText.getComponentKeyListeners().add(new ComponentKeyListener.Adapter() {
					@Override
					public boolean keyTyped(Component arg0, char arg1) {
						try {
							filtroTabla();
						} catch (NullPointerException e) {
						}
						return false;
					}
				});

		categoriasComboBox.getListButtonSelectionListeners().add(new ListButtonSelectionListener.Adapter() {
					@Override
					public void selectedItemChanged(ListButton listButton,
							Object previousSelectedItem) {
						filtroTabla();
					}
				});
	}

	@SuppressWarnings("unchecked")
	protected void filtroTabla() {
		CategoriaItem categoriaItem = (CategoriaItem) categoriasComboBox.getSelectedItem();
		Integer tiempo = (spinnerMinutos.getSelectedIndex() * 60) + spinnerSegundos.getSelectedIndex();
		if (tiempo == 0) {
			tiempo = null;
		}

		if (categoriaItem != null) {
			try {
				filtroRecetas = convertirList(ServiceLocator.getRecetasService().buscarRecetas(nombreText.getText(), tiempo, categoriaItem.getId()));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		tableViewRecetas.setTableData(filtroRecetas);
	}
	
	protected void recargarTabla(){
		tableViewRecetas.setTableData(recetas);
	}
}