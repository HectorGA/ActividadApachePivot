package dad.recetapp;

import java.io.IOException;
import java.net.URL;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

import dad.recetapp.ui.Inicio;
import dad.recetapp.ui.images.Images;

public class RecetAppApplication implements Application{
	
	private Inicio inicio = null;
	
	@Override
	public void suspend() throws Exception { }

	@Override
	public void resume() throws Exception {	}

	@Override
	public boolean shutdown(boolean optional) throws Exception {
		return false;
	}

	@Override
	public void startup(Display display, Map<String, String> properties) throws Exception {		
		inicio = (Inicio) loadWindow("dad/recetapp/ui/inicio.bxml");
		inicio.open(display);
		inicio.setTitle("RecetApp");
		java.awt.Window frame = display.getHostWindow();
		frame.setIconImage(Images.LOGO_ICON.getImage());
	}

	public static Window loadWindow(String bxmlFile) throws IOException, SerializationException {
		URL bxmlUrl = RecetAppApplication.class.getClassLoader().getResource(bxmlFile);
		BXMLSerializer serializer = new BXMLSerializer();
		return (Window) serializer.readObject(bxmlUrl);
	}
}
