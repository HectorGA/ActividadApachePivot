package dad.recetapp.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dad.recetapp.db.DataBase;
import dad.recetapp.services.IMedidasService;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.items.MedidaItem;

public class MedidasService implements IMedidasService{

	@Override
	public void crearMedida(MedidaItem medida) throws ServiceException {
		try {
			if (medida == null || medida.getAbreviatura() == null || medida.getNombre() == null) {
				throw new ServiceException("Error al crear la medida: debe rellenar todos los datos");
			}			
			Connection conn = DataBase.getConnection();
			PreparedStatement statement = conn.prepareStatement("insert into medidas (nombre,abreviatura) values (?,?)");
			statement.setString(1, medida.getNombre());
			statement.setString(2, medida.getAbreviatura());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			throw new ServiceException("Error al crear la medida '" + medida.getNombre() + "'", e);
		} catch (NullPointerException e) {
			throw new ServiceException("Error al crear la medida: la medida no puede ser nula", e);
		}
	}
	@Override
	public void modificarMedida(MedidaItem medida) throws ServiceException {
		try{
			if (medida.getId() == null) {
				throw new ServiceException("Error al recuperar la medida: Debe especificar el identificador");
			}
			Connection conn = DataBase.getConnection();
			PreparedStatement statement = conn.prepareStatement("update medidas set (nombre,abreviatura) values = ?,? where id = ?");
			statement.setString(1, medida.getNombre());
			statement.setString(2, medida.getAbreviatura());
			statement.setLong(3, medida.getId());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			throw new ServiceException("Error al modificar la medida con ID '" + medida.getId()+ "'", e);
		}
	}

	@Override
	public void eliminarMedida(Long id) throws ServiceException {
		try{
			if (id == null) {
				throw new ServiceException("Error al recuperar la medida: Debe especificar el identificador");
			}
			Connection conn = DataBase.getConnection();
			PreparedStatement statement = conn.prepareStatement("delete from medidas where id = ?");
			statement.setLong(1, id);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			throw new ServiceException("Error al eliminar la medida con ID '" + id + "'", e);
		}
	}

	@Override
	public List<MedidaItem> listarMedidas() throws ServiceException {
		List<MedidaItem> listmedida = new ArrayList<MedidaItem>();
		MedidaItem medida = null;
		try {
			Connection conn = DataBase.getConnection();
			PreparedStatement statement = conn.prepareStatement("select id,nombre,abreviatura from medidas");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				medida = new MedidaItem();
				medida.setId(rs.getLong("id")); // categoria.setId(id);
				medida.setNombre(rs.getString("nombre"));
				medida.setAbreviatura(rs.getString("abreviatura"));
				listmedida.add(medida);
			}
			statement.close();
		} catch (SQLException e) {
			throw new ServiceException("Error al recuperar las medidas", e);
		}
		return listmedida;
	}

	@Override
	public MedidaItem obtenerMedida(Long id) throws ServiceException {
		MedidaItem medida = null;
		try{
			if(id==null){
				throw new ServiceException("Error al recuperar la medida: Debe especificar el identificador");
		}
		Connection conn = DataBase.getConnection();
		PreparedStatement statement = conn.prepareStatement("select * from medidas where id = ?"); 
		statement.setLong(1, id);
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
			medida = new MedidaItem();
			medida.setId(rs.getLong("id")); // medida.setId(id);
			medida.setNombre(rs.getString("nombre"));
			medida.setAbreviatura(rs.getString("abreviatura"));
		}
		statement.close();
		}catch(SQLException e){
			throw new ServiceException("Error al recuperar la categoría con ID '" + id + "'", e);
		}
		return medida;
	}

}
