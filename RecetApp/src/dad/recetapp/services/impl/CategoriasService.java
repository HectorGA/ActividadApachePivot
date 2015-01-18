package dad.recetapp.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dad.recetapp.db.DataBase;
import dad.recetapp.services.ICategoriasService;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.items.CategoriaItem;

public class CategoriasService implements ICategoriasService {

	public CategoriasService() {
	}

	@Override
	public void crearCategoria(CategoriaItem categoria) throws ServiceException {
		try {
			//	Comprobamos que no este vacia la variable que recibimos.
			if (categoria == null || categoria.getDescripcion() == null) {
				throw new ServiceException("Error al crear la categoría: debe especificar la categoría");
			}	
			// Obtenemos otra vez la conexión
			Connection conn = DataBase.getConnection();
			// Preparamos la consulta
			PreparedStatement statement = conn
					.prepareStatement("insert into categorias (descripcion) values (?)");
			statement.setString(1, categoria.getDescripcion());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			// No queremos que nos muestre el mensaje del SQLException, sino
			// nuestro propio mensaje de ServiceException
			throw new ServiceException("Error al crear la categoría '"
					+ categoria.getDescripcion() + "': " + e.getMessage(), e);
		} catch (NullPointerException e) {
			throw new ServiceException(
					"Error al crear la categoría: la categoría no puede ser null."
							+ e.getMessage(), e);
		}
	}

	@Override
	public void modificarCategoria(CategoriaItem categoria) throws ServiceException {
		Connection conn = null;
		try {
			// Obtenemos otra vez la conexión
			conn = DataBase.getConnection();
			// Preparamos la consulta
			PreparedStatement statement = conn
					.prepareStatement("update categorias set descripcion = ? where descripcion = ?");
			statement.setString(1, categoria.getDescripcion());
			statement.setString(2, categoria.getDescripcion());
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			// No queremos que nos muestre el mensaje del SQLException, sino
			// nuestro propio mensaje de ServiceException
			throw new ServiceException("Error al crear la categoría '"
					+ categoria.getDescripcion() + "': " + e.getMessage(), e);
		} catch (NullPointerException e) {
			throw new ServiceException(
					"Error al crear la categoría: la categoría no puede ser null."
							+ e.getMessage(), e);
		}
	}

	@Override
	public void eliminarCategoria(Long id) throws ServiceException {
		try{
			if (id == null) {
				throw new ServiceException("Error al recuperar la categoría: Debe especificar el identificador");
			}
			Connection conn = DataBase.getConnection();
			PreparedStatement statement = conn.prepareStatement("delete from categorias where id = ?");
			statement.setLong(1, id);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			throw new ServiceException("Error al eliminar la categoría con ID '" + id + "'", e);
		}
	}

	@Override
	public List<CategoriaItem> listarCategoria() throws ServiceException {
		List<CategoriaItem> listcategoria = new ArrayList<CategoriaItem>();
		CategoriaItem categoria = null;
		try {
			Connection conn = DataBase.getConnection();
			PreparedStatement statement = conn.prepareStatement("select id,descripcion from categorias");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				categoria = new CategoriaItem();
				categoria.setId(rs.getLong("id")); // categoria.setId(id);
				categoria.setDescripcion(rs.getString("descripcion"));
				listcategoria.add(categoria);
			}
			statement.close();
		} catch (SQLException e) {
			throw new ServiceException("Error al recuperar las categorías", e);
		}
		return listcategoria;
	}

	@Override
	public CategoriaItem obtenerCategoria(Long id) throws ServiceException {
		CategoriaItem categoria = null;
		try {
			if(id==null){
				throw new ServiceException("Error al recuperar la categoría: Debe especificar el identificador.");
			}
			// Obtenemos otra vez la conexión
			Connection conn = DataBase.getConnection();
			// Preparamos la consulta
			PreparedStatement statement = conn.prepareStatement("select * from categorias where id = ?");
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			if(rs.next()){
				categoria = new CategoriaItem();
				categoria.setId(id);	//	Tambien se puede usar:	 categoria.setId(rs.getLong("id);
				categoria.setDescripcion(rs.getString("descripcion"));
			}
			statement.close();
			
		} catch (SQLException e) {
			throw new ServiceException("Error al recuperar la categoría con ID '" + id + "'", e);
		}

		return categoria;
	}

}
