package dad.recetapp.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.Timer;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.Mouse.Button;

import dad.recetapp.RecetAppApplication;

public class Inicio extends Window implements Bindable{
	@BXML private ImageView imageView;
	private Timer timer;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		timer = new Timer(4000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciarPrograma();				
			}
		});
		timer.start();
		
		imageView.getComponentMouseButtonListeners().add(new ComponentMouseButtonListener() {
					@Override
					public boolean mouseUp(Component arg0, Button arg1, int arg2, int arg3) {
						return false;
					}

					@Override
					public boolean mouseDown(Component arg0, Button arg1, int arg2, int arg3) {
						return false;
					}

					@Override
					public boolean mouseClick(Component arg0, Button arg1, int arg2, int arg3, int arg4) {
						iniciarPrograma();
						return false;
					}
				});
	}

	private void iniciarPrograma(){
		timer.stop();
		try {
			MainWindow mainWindow = (MainWindow) RecetAppApplication.loadWindow("dad/recetapp/ui/mainWindow.bxml");
			mainWindow.open(this);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}
		
	}
}