package dad.recetapp.ui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.Window;

import dad.recetapp.RecetAppApplication;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;


public class MainWindow extends Window implements Bindable {
	@SuppressWarnings("unused")
	private RecetAppApplication recetAppApplication;
	@BXML private Window mainWindow;
	@BXML private static Label numRecetas;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		mainWindow.setIcon("/dad/recetapp/ui/images/logo.png");
		setNumReceta();
	}
	
	public void setRecetAppApplication(RecetAppApplication recetAppApplication){
		this.recetAppApplication = recetAppApplication;
	}
	
	public static void setNumReceta(){
    	try {
    		numRecetas.setText(String.valueOf(ServiceLocator.getRecetasService().listarRecetas().size()));
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    }
}
