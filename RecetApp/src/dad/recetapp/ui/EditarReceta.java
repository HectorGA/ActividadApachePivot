package dad.recetapp.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.Spinner;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;
import dad.recetapp.services.items.IngredienteItem;
import dad.recetapp.services.items.InstruccionItem;
import dad.recetapp.services.items.RecetaItem;
import dad.recetapp.services.items.RecetaListItem;
import dad.recetapp.services.items.SeccionItem;

public class EditarReceta extends Window implements Bindable {

	@BXML private EditarReceta editarReceta;
	@BXML private Button botonEditar, botonCancelar;
	@BXML public static TabPane tabPaneEditarReceta;
	@BXML private TextInput nombreText, paraText;
	@BXML private Spinner spinnerTotalMinutos, spinnerTotalSegundos, spinnerThermomixMinutos, spinnerThermomixSegundos;
	@BXML private static ListButton categoriasComboBox, paraComboBox;
	public org.apache.pivot.collections.List<RecetaListItem> recetas;
	private static List<ComponenteReceta> componentes;
	private static org.apache.pivot.collections.List<CategoriaItem> categorias;
	private RecetaItem recetaItem;
	private RecetaListItem recetaList;
	private int indiceRecetaList;
	private Long id;

	@Override
	public void initialize(Map<String, Object> namespace, URL location,Resources resources) {
		componentes = new ArrayList<ComponenteReceta>();
		
		try {
			cargarComboCategorias();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		categoriasComboBox.setSelectedIndex(0);
		tabPaneEditarReceta.getComponentMouseButtonListeners().add(new ComponentMouseButtonListener.Adapter() {
			@Override
			public boolean mouseClick(Component arg0,org.apache.pivot.wtk.Mouse.Button arg1, int arg2,int arg3, int arg4) {
				if (tabPaneEditarReceta.getSelectedIndex() == tabPaneEditarReceta.getTabs().getLength() - 1) {
					crearNuevoPanelComponente();
				}
				return false;
			}
		});

		botonCancelar.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button arg0) {
				close();
			}
		});

		botonEditar.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button arg0) {
				editarReceta();
			}
		});
	}

	protected void editarReceta() {
		if (nombreText.getText().equals("") || paraText.getText().equals("")) {
			Prompt error = new Prompt("Los campos \"Nombre\", \"Para\" y \"Categoría\" son obligatorios.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		} else {
			int minutosTotal = spinnerTotalMinutos.getSelectedIndex();
			int segundosTotal = spinnerTotalSegundos.getSelectedIndex();
			int minutosThermomix = spinnerThermomixMinutos.getSelectedIndex();
			int segundosThermomix = spinnerThermomixSegundos.getSelectedIndex();

			recetaList.setNombre(nombreText.getText());
			recetaList.setCantidad(Integer.parseInt(paraText.getText()));
			recetaList.setCategoria(categoriasComboBox.getSelectedItem().toString());
			recetaList.setPara(paraComboBox.getSelectedItem().toString());
			recetaList.setTiempoTotal(minutosTotal*60 + segundosTotal);
			recetaList.setTiempoThermomix(minutosThermomix*60 + segundosThermomix);

			recetaItem.setNombre(nombreText.getText());
			recetaItem.setCantidad(Integer.parseInt(paraText.getText()));
			recetaItem.setCategoria((CategoriaItem)categoriasComboBox.getSelectedItem());
			recetaItem.setPara(paraComboBox.getSelectedItem().toString());
			recetaItem.setTiempoTotal(minutosTotal*60 + segundosTotal);
			recetaItem.setTiempoThermomix(minutosThermomix*60 + segundosThermomix);

			recetaItem.getSecciones().removeAll(recetaItem.getSecciones());

			for (ComponenteReceta componente : componentes) {
				if(!componente.getSeccion().getNombre().equals("")){
					recetaItem.getSecciones().add(componente.getSeccion());
				}
			}

			try {
				ServiceLocator.getRecetasService().modificarReceta(recetaItem);
				RecetasPane.recetas.update(indiceRecetaList, recetaList);
				RecetasPane.tableViewRecetas.setTableData(RecetasPane.recetas);
			} catch (ServiceException e) {
			}
			close();
		}
	}

	protected void crearNuevoPanelComponente() {
		try {
			URL bxmlUrl = getClass().getResource("ComponenteReceta.bxml");
			BXMLSerializer serializer = new BXMLSerializer();
			ComponenteReceta componenteReceta = (ComponenteReceta) serializer.readObject(bxmlUrl);
			componenteReceta.setParent(this);

			tabPaneEditarReceta.getTabs().insert(componenteReceta,tabPaneEditarReceta.getLength() - 2);
			tabPaneEditarReceta.setSelectedTab(componenteReceta);
			componentes.add(componenteReceta);

		} catch (IOException | SerializationException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "static-access", "unchecked" })
	protected void crearPanelesExistentes(String texto, List<IngredienteItem> ingredientes, List<InstruccionItem> instrucciones) {
		try {
			URL bxmlUrl = getClass().getResource("ComponenteReceta.bxml");
			BXMLSerializer serializer = new BXMLSerializer();
			ComponenteReceta componenteReceta = (ComponenteReceta) serializer.readObject(bxmlUrl);

			componenteReceta.setParent(this);

			tabPaneEditarReceta.getTabs().insert(componenteReceta,tabPaneEditarReceta.getLength() - 2);
			tabPaneEditarReceta.setSelectedTab(componenteReceta);
			tabPaneEditarReceta.setTabData(tabPaneEditarReceta.getSelectedTab(), texto);
			componenteReceta.setSeccionText(texto);
			componenteReceta.setIngredientes(convertirList(ingredientes));
			componenteReceta.setInstrucciones(convertirList(instrucciones));

			componentes.add(componenteReceta);

		} catch (IOException | SerializationException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public void cambiarTituloPestana(String titulo) {
		tabPaneEditarReceta.setTabData(tabPaneEditarReceta.getSelectedTab(),titulo);
	}

	public static void eliminarPestanaActual() {
		if (tabPaneEditarReceta.getTabs().getLength() > 0) {
			int posicion = tabPaneEditarReceta.getSelectedIndex();
			if (posicion == 0) {
				tabPaneEditarReceta.setSelectedIndex(posicion + 1);
			} else {
				tabPaneEditarReceta.setSelectedIndex(posicion - 1);
			}
			componentes.remove(posicion);
			tabPaneEditarReceta.getTabs().remove(posicion, 1);
		}
	}

	@SuppressWarnings("unchecked")
	public static void cargarComboCategorias() throws ServiceException {
		categorias = convertirList(ServiceLocator.getCategoriasService().listarCategoria());
		categoriasComboBox.setListData(categorias);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static org.apache.pivot.collections.List convertirList(java.util.List<?> listaUtil) {
		org.apache.pivot.collections.List listaApache = new org.apache.pivot.collections.ArrayList();
		for (int i = 0; i < listaUtil.size(); i++) {
			listaApache.add(listaUtil.get(i));
		}
		return listaApache;
	}

	public void setReceta(RecetaListItem recetaList){
		this.recetaList = recetaList;
		id = recetaList.getId();

		try {
			recetaItem = ServiceLocator.getRecetasService().obtenerReceta(id);
		} catch (ServiceException e1) {
		}

		int minutosTotal = recetaItem.getTiempoTotal() / 60;
		int segundosTotal = recetaItem.getTiempoTotal() % 60;
		int minutosThermomix = recetaItem.getTiempoThermomix() / 60;
		int segundosThermomix = recetaItem.getTiempoThermomix() % 60;

		nombreText.setText(recetaItem.getNombre());
		paraText.setText(recetaItem.getCantidad().toString());
		spinnerThermomixMinutos.setSelectedIndex(minutosThermomix);
		spinnerThermomixSegundos.setSelectedIndex(segundosThermomix);
		spinnerTotalMinutos.setSelectedIndex(minutosTotal);
		spinnerTotalSegundos.setSelectedIndex(segundosTotal);

		if(recetaItem.getSecciones().size() == 0){
			crearNuevoPanelComponente();
		}
		else{
			for (SeccionItem seccion : recetaItem.getSecciones()) {
				crearPanelesExistentes(seccion.getNombre(), seccion.getIngredientes(), seccion.getInstrucciones());
			}
		}
	}

	public void setVariables(org.apache.pivot.collections.List<RecetaListItem> variables){
		this.recetas = variables;
	}

	public void setPosicionRecetaListSeleccionada(int posicionRecetaListSeleccionada) {
		this.indiceRecetaList = posicionRecetaListSeleccionada;
	}


}