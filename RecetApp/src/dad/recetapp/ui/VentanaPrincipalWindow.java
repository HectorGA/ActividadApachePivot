package dad.recetapp.ui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.Window;

import dad.recetapp.RecetAppApplication;


public class VentanaPrincipalWindow extends Window implements Bindable {
	
	@SuppressWarnings("unused")
	private RecetAppApplication recetAppApplication;
	
	@BXML
	private Window ventanaPrincipalWindow;
	@BXML
	private CategoriasPane categoriasPane;
	@BXML
    private static Label numRecetas;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		ventanaPrincipalWindow.setIcon("/dad/recetapp/ui/images/logo.png");
		//numRecetas.setText(String.valueOf(ServiceLocator.getRecetasService().listarRecetas().size()));
	}
	
	public void setRecetAppApplication(RecetAppApplication recetAppApplication){
		this.recetAppApplication = recetAppApplication;
	}
}
