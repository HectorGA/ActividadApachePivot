package dad.recetapp.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
import dad.recetapp.services.items.RecetaItem;
import dad.recetapp.services.items.RecetaListItem;

public class NuevaReceta extends Window implements Bindable {

	@BXML private NuevaReceta nuevaReceta;
	@BXML private Button botonCrear, botonCancelar;
	@BXML public static TabPane tabPaneNuevaReceta;
	@BXML private TextInput nombreText, paraText;
	@BXML private Spinner spinnerTotalMinutos, spinnerTotalSegundos, spinnerThermomixMinutos, spinnerThermomixSegundos;
	@BXML private static ListButton paraComboBox, categoriasComboBox;
	
	public org.apache.pivot.collections.List<RecetaListItem> recetas;
	private static org.apache.pivot.collections.List<CategoriaItem> categorias;
	private static List<ComponenteReceta> componentes;

	@Override
	public void initialize(Map<String, Object> namespace, URL location,Resources resources) {
		componentes = new ArrayList<ComponenteReceta>();
		crearNuevoPanelComponente();

		try {
			cargarComboCategorias();

		} catch (ServiceException e) {
			e.printStackTrace();
		}

		tabPaneNuevaReceta.getComponentMouseButtonListeners().add(new ComponentMouseButtonListener.Adapter() {
					@Override
					public boolean mouseClick(Component arg0,org.apache.pivot.wtk.Mouse.Button arg1, int arg2,int arg3, int arg4) {
						if (tabPaneNuevaReceta.getSelectedIndex() == tabPaneNuevaReceta.getTabs().getLength()-1) {
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

		botonCrear.getButtonPressListeners().add(new ButtonPressListener() {
					@Override
					public void buttonPressed(Button arg0) {
						crearReceta();
					}
				});
	}

	private void crearReceta() {
		if (nombreText.getText().equals("") || paraText.getText().equals("") || categoriasComboBox.getSelectedItem().toString().equals("Seleccione una categoría")) {
			Prompt error = new Prompt("Los campos \"Nombre\", \"Para\" y \"Categoría\" son obligatorios.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		} else {
			RecetaItem recetaItem = new RecetaItem();
			recetaItem.setNombre(nombreText.getText());
			recetaItem.setCantidad(Integer.parseInt(paraText.getText()));
			recetaItem.setCategoria((CategoriaItem) categoriasComboBox.getSelectedItem());
			recetaItem.setPara(paraComboBox.getSelectedItem().toString());
			recetaItem.setFechaCreacion(new Date());
			recetaItem.setTiempoThermomix(spinnerThermomixMinutos.getSelectedIndex() * 60 + spinnerThermomixSegundos.getSelectedIndex());
			recetaItem.setTiempoTotal(spinnerTotalMinutos.getSelectedIndex() * 60 + spinnerTotalSegundos.getSelectedIndex());

			for (ComponenteReceta componente : componentes) {
				if(!componente.getSeccion().getNombre().equals("")){
					recetaItem.getSecciones().add(componente.getSeccion());
				}
			}
			try {
				ServiceLocator.getRecetasService().crearReceta(recetaItem);
				VentanaPrincipalWindow.setNumReceta();
			} catch (ServiceException e) {
				Prompt error = new Prompt("Error al crear la receta.");
				error.open(this.getWindow(), new SheetCloseListener() {
					public void sheetClosed(Sheet sheet) {
					}
				});
			}

			RecetaListItem recetaListItem = new RecetaListItem();
			recetaListItem.setId(recetaItem.getId());
			recetaListItem.setNombre(nombreText.getText());
			recetaListItem.setCantidad(Integer.parseInt(paraText.getText()));
			recetaListItem.setCategoria(categoriasComboBox.getSelectedItem().toString());
			recetaListItem.setPara(paraComboBox.getSelectedItem().toString());
			recetaListItem.setFechaCreacion(new Date());
			recetaListItem.setTiempoThermomix(spinnerThermomixMinutos.getSelectedIndex()* 60 + spinnerThermomixSegundos.getSelectedIndex());
			recetaListItem.setTiempoTotal(spinnerTotalMinutos.getSelectedIndex() * 60 + spinnerTotalSegundos.getSelectedIndex());

			recetas.add(recetaListItem);
			close();
		}
	}

	protected void crearNuevoPanelComponente() {
		try {
			URL bxmlUrl = getClass().getResource("ComponenteReceta.bxml");
			BXMLSerializer serializer = new BXMLSerializer();
			ComponenteReceta componenteReceta = (ComponenteReceta) serializer.readObject(bxmlUrl);
			componenteReceta.setParent(this);
			tabPaneNuevaReceta.getTabs().insert(componenteReceta,tabPaneNuevaReceta.getLength() - 2);
			tabPaneNuevaReceta.setSelectedTab(componenteReceta);
			componentes.add(componenteReceta);

		} catch (IOException | SerializationException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public void cambiarTituloPestana(String titulo) {
		tabPaneNuevaReceta.setTabData(tabPaneNuevaReceta.getSelectedTab(),titulo);
	}

	public static void eliminarPestanaActual() {
		if (tabPaneNuevaReceta.getTabs().getLength() > 0) {
			int indice = tabPaneNuevaReceta.getSelectedIndex();
			if (indice == 0) {
				tabPaneNuevaReceta.setSelectedIndex(indice+1);
			} else {
				tabPaneNuevaReceta.setSelectedIndex(indice-1);
			}
			componentes.remove(indice);
			tabPaneNuevaReceta.getTabs().remove(indice, 1);
		} else if (tabPaneNuevaReceta.getTabs().getLength()==2) {
			tabPaneNuevaReceta.setSelectedIndex(tabPaneNuevaReceta.getTabs().getLength()-1);
			tabPaneNuevaReceta.getTabs().remove(tabPaneNuevaReceta.getSelectedIndex()-1, 1);
			componentes.remove(tabPaneNuevaReceta.getSelectedIndex()-1);
		}
	}

	@SuppressWarnings("unchecked")
	public static void cargarComboCategorias() throws ServiceException {
		CategoriaItem categoriaItem = new CategoriaItem();
		categoriaItem.setId(null);
		categoriaItem.setDescripcion("Seleccione una categoría");
		categorias = convertirList(ServiceLocator.getCategoriasService().listarCategoria());
		categorias.insert(categoriaItem, 0);
		categoriasComboBox.setListData(categorias);
		categoriasComboBox.setSelectedItem(categoriaItem);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static org.apache.pivot.collections.List convertirList(java.util.List<?> listaUtil) {
		org.apache.pivot.collections.List listaApache = new org.apache.pivot.collections.ArrayList();
		for (int i = 0; i < listaUtil.size(); i++) {
			listaApache.add(listaUtil.get(i));
		}
		return listaApache;
	}
	
	public void setVariables(org.apache.pivot.collections.List<RecetaListItem> variables){
		this.recetas = variables;
	}
	
}