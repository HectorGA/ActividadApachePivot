package dad.recetapp.services;

import java.util.List;

import dad.recetapp.services.items.RecetaItem;
import dad.recetapp.services.items.RecetaListItem;

public interface IResetasService {

	public void crearReceta(RecetaItem receta) throws ServiceException;
	public void modificarReceta(RecetaItem receta) throws ServiceException;
	public void eliminarReceta(Long id) throws ServiceException;
	public List<RecetaListItem> buscarReceta(String nombre, Integer tiempoTotal, Long idCategoria) throws ServiceException;
	public List<RecetaListItem> listarRecetas() throws ServiceException;
	public RecetaItem obtenerReceta(Long id) throws ServiceException;
	
}
