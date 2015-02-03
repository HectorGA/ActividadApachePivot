package dad.recetapp.ui;

import java.net.URL;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.ListButtonSelectionListener;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Spinner;
import org.apache.pivot.wtk.SpinnerSelectionListener;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.RecetAppApplication;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;
import dad.recetapp.services.items.RecetaListItem;

public class RecetasPane extends TablePane implements Bindable {
	
	private List<RecetaListItem> recetas,nuevalist;
	private RecetAppApplication windowsApp;
	@BXML private PushButton botonAnadir;	
	@BXML private PushButton botonEditar;
	@BXML private PushButton botonEliminar;
	@BXML private TableView tableViewRecetas;
	@BXML private TextInput nombretext;
	@BXML private Spinner filtrarMinutos,filtrarSegundos;
	private List lista;
	
	@BXML private static ListButton comboReceta;
	private static List<CategoriaItem> categoriasBD;
	
	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {
		
		recetas= new ArrayList<RecetaListItem>();
		tableViewRecetas.setTableData(recetas);
		
		initRecetasView();
		try {
			recargarCategoriaListButton();
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}

		comboReceta.getListButtonSelectionListeners().add(
				new ListButtonSelectionListener.Adapter() {
					public void selectedItemChanged(ListButton listButton,
							Object previousSelectedItem) {
						filtroTabla();
					}
				});

		nombretext.getComponentKeyListeners().add(
				new ComponentKeyListener.Adapter() {
					@Override
					public boolean keyTyped(Component arg0, char arg1) {
						try {
							filtroTabla();
						} catch (NullPointerException e) {
						}
						return false;
					}
				});
		filtrarMinutos.getSpinnerSelectionListeners().add(
				new SpinnerSelectionListener() {
					@Override
					public void selectedItemChanged(Spinner arg0, Object arg1) {
						filtroTabla();
					}

					@Override
					public void selectedIndexChanged(Spinner arg0, int arg1) {
						filtroTabla();
					}
				});
		filtrarSegundos.getSpinnerSelectionListeners().add(
				new SpinnerSelectionListener() {

					@Override
					public void selectedItemChanged(Spinner arg0, Object arg1) {
						filtroTabla();
					}

					@Override
					public void selectedIndexChanged(Spinner arg0, int arg1) {
						filtroTabla();
					}
				});



		botonAnadir.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button arg0) {
				onBotonAnadirPressed();
			}
		});

		botonEditar.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button arg0) {
				onBotonEditarPressed();
			}
		});
		botonEliminar.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button arg0) {
				onBotonEliminarPressed();
			}
		});
	}

	protected void onBotonAnadirPressed() {	
		System.out.println("añadir");
		//windowsApp.openSecondWindow();
	}
	
	protected void onBotonEditarPressed() {		
		System.out.println("editar");
		//windowsApp.abrirEditarWindow();
	}
	
	protected void onBotonEliminarPressed() {
		System.out.println("eliminar");
		//windowsApp.openSecondWindow();
	}

	public void setWindowsApp(RecetAppApplication windowsApp) {
		this.windowsApp = windowsApp;
	}
	
	private void initRecetasView() {
		try {
			java.util.List<RecetaListItem> aux = ServiceLocator.getRecetasService().listarRecetas();
			for (RecetaListItem c : aux) {
				recetas.add(c);
			}
		} catch (ServiceException e) {
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void recargarCategoriaListButton() throws ServiceException {
		CategoriaItem categoriaTitle = new CategoriaItem();
		categoriaTitle.setId(null);
		categoriaTitle.setDescripcion("<Categorías>");
		categoriasBD = convertirList(ServiceLocator.getCategoriasService().listarCategoria());
		categoriasBD.insert(categoriaTitle, 0);
		comboReceta.setListData(categoriasBD);
		comboReceta.setSelectedItem(categoriaTitle);
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

	
	protected void filtroTabla() {
		CategoriaItem selectedItem = (CategoriaItem) comboReceta.getSelectedItem();
		Integer tiempoFinal = (filtrarMinutos.getSelectedIndex() * 60) + filtrarSegundos.getSelectedIndex();
		if (tiempoFinal == 0) {
			tiempoFinal = null;
		}

		if (selectedItem != null) {
			try {
				lista = convertirList(ServiceLocator.getRecetasService().buscarRecetas(nombretext.getText(), tiempoFinal, selectedItem.getId()));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		tableViewRecetas.setTableData(lista);
	}
}
