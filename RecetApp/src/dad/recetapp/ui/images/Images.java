package dad.recetapp.ui.images;

import java.net.URL;
import javax.swing.ImageIcon;

public class Images {

	public static final String ICONS_PATH = "dad/recetapp/ui/images/";
	
	public static final ImageIcon ADD_ICON = loadIcon("add-icon-20x20.png");
	public static final ImageIcon ADD_TAB_ICON = loadIcon("addTabIcon.png");
	public static final ImageIcon CLOSE_TAB_ICON = loadIcon("closeTabIcon.png");
	public static final ImageIcon CLOSE_TAB_OVER_ICON = loadIcon("closeTabOverIcon.png");
	public static final ImageIcon DELETE_ICON = loadIcon("delete-icon-20x20.png");
	public static final ImageIcon EDIT_ICON = loadIcon("edit-icon-20x20.png");
	public static final ImageIcon LOGO_ICON = loadIcon("logo.png");
	public static final ImageIcon RECETAPP_INICIO = loadIcon("recetapp-inicio.png");
	
	public static ImageIcon loadIcon(String name){
		URL url = Images.class.getClassLoader().getResource(ICONS_PATH+name);
		return new ImageIcon(url);
	}
}
