package dad.recetapp.services;

import java.util.List;

import dad.recetapp.services.items.TipoIngredienteItem;

public interface ITiposIngredientesService {
	
	public void crearTipoIngrediente(TipoIngredienteItem tipoIngrediente) throws ServiceException;
	public void modificarTipoIngrediente(TipoIngredienteItem tipoIngrediente) throws ServiceException;
	public void eliminarTipoIngrediente(Long id) throws ServiceException;
	public List<TipoIngredienteItem> listarTipoIngrediente() throws ServiceException;
	public TipoIngredienteItem obteneTipoIngrediente(Long id) throws ServiceException;


}
