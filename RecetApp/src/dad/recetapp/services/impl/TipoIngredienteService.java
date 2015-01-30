package dad.recetapp.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dad.recetapp.db.DataBase;
import dad.recetapp.services.ITiposIngredientesService;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.items.TipoIngredienteItem;

public class TipoIngredienteService implements ITiposIngredientesService{

	@Override
	public void crearTipoIngrediente(TipoIngredienteItem tipoIngrediente)
			throws ServiceException {
		try {
			if (tipoIngrediente == null || tipoIngrediente.getNombre() == null) {
				throw new ServiceException("Error al crear el tipo de ingrediente: debe rellenar todos los datos");
			}			
			Connection conn = DataBase.getConnection();
			PreparedStatement statement = conn.prepareStatement("INSERT INTO tipos_ingredientes (nombre) VALUES (?);");
			statement.setString(1, tipoIngrediente.getNombre());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			throw new ServiceException("Error al crear el tipo de ingrediente '" + tipoIngrediente.getNombre() + "'", e);
		} catch (NullPointerException e) {
			throw new ServiceException("Error al crear el tipo de ingrediente: el tipo de ingrediente no puede ser nulo", e);
		}
	}

	@Override
	public void modificarTipoIngrediente(TipoIngredienteItem tipoIngrediente)
			throws ServiceException {
		try{
			if (tipoIngrediente.getId() == null) {
				throw new ServiceException("Error al recuperar el tipo de ingrediente: Debe especificar el identificador");
			}
			Connection conn = DataBase.getConnection();
			PreparedStatement statement = conn.prepareStatement("update tipos_ingredientes set nombre = ? where id = ?");
			statement.setString(1, tipoIngrediente.getNombre());
			statement.setLong(2, tipoIngrediente.getId());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			throw new ServiceException("Error al modificar la medida con ID '" + tipoIngrediente.getId()+ "'", e);
		}
	}

	@Override
	public void eliminarTipoIngrediente(Long id) throws ServiceException {
		try{
			if (id == null) {
				throw new ServiceException("Error al recuperar el tipo de ingrediente: Debe especificar el identificador");
			}
			Connection conn = DataBase.getConnection();
			PreparedStatement statement = conn.prepareStatement("delete from tipos_ingredientes where id = ?");
			statement.setLong(1, id);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			throw new ServiceException("Error al eliminar la el tipo de ingrediente con ID '" + id + "'", e);
		}
	}

	@Override
	public List<TipoIngredienteItem> listarTipoIngrediente()
			throws ServiceException {
		List<TipoIngredienteItem> listTipoIngrediente = new ArrayList<TipoIngredienteItem>();
		TipoIngredienteItem tipoIngrediente = null;
		try {
			Connection conn = DataBase.getConnection();
			PreparedStatement statement = conn.prepareStatement("select id, nombre from tipos_ingredientes");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				tipoIngrediente = new TipoIngredienteItem();
				tipoIngrediente.setId(rs.getLong("id"));
				tipoIngrediente.setNombre(rs.getString("nombre"));
				listTipoIngrediente.add(tipoIngrediente);
			}
			statement.close();
		} catch (SQLException e) {
			throw new ServiceException("Error al recuperar los tipos de ingredientes", e);
		}
		return listTipoIngrediente;
	}

	@Override
	public TipoIngredienteItem obtenerTipoIngrediente(Long id) throws ServiceException {
		TipoIngredienteItem tipoIngrediente = null;
		try{
			if(id==null){
				throw new ServiceException("Error al recuperar el tipo de ingrediente: Debe especificar el identificador");
		}
		Connection conn = DataBase.getConnection();
		PreparedStatement statement = conn.prepareStatement("select nombre from tipos_ingredientes where id = ?"); 
		statement.setLong(1, id);
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
			tipoIngrediente = new TipoIngredienteItem();
			tipoIngrediente.setId(rs.getLong("id")); // medida.setId(id);
			tipoIngrediente.setNombre(rs.getString("nombre"));
		}
		statement.close();
		}catch(SQLException e){
			throw new ServiceException("Error al recuperar el tipo de ingrediente con ID '" + id + "'", e);
		}
		return tipoIngrediente;
	}

}
