package dad.recetapp;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.apache.pivot.wtk.DesktopApplicationContext;
import dad.recetapp.db.DataBase;

public class Main {
	public static void main(String[] args) throws SQLException{		
		if(DataBase.test())
			DesktopApplicationContext.main(RecetAppApplication.class, args);
		else
			JOptionPane.showMessageDialog(null, "Error, no se pudo conectar con la base de datos.\nSe cerrará el programa.", "Error con la conexión.", JOptionPane.ERROR_MESSAGE);	
	}

}