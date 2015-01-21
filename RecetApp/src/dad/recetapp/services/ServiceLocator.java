package dad.recetapp.services;

import dad.recetapp.services.impl.CategoriasService;
import dad.recetapp.services.impl.MedidasService;
import dad.recetapp.services.impl.TipoAnotacionService;
import dad.recetapp.services.impl.TipoIngredienteService;

public class ServiceLocator {

	private static final ICategoriasService categoriaServices = new CategoriasService();
	private static final ITiposIngredientesService tipoIngredienteServices = new TipoIngredienteService();
	private static final IMedidasService medidasServices = new MedidasService();
	private static final ITiposAnotacionesService tipoAnotacionServices = new TipoAnotacionService();
	
	private ServiceLocator() {}
	
	public static ICategoriasService getCategoriasService() {
		return categoriaServices;
	}
	
	public static ITiposIngredientesService getTiposIngredientesService(){
		return tipoIngredienteServices;
	}
	
	public static IMedidasService getMedidasService(){
		return medidasServices;
	}
	
	public static ITiposAnotacionesService getTipoAnotacionService(){
		return tipoAnotacionServices;
	}
	
}
