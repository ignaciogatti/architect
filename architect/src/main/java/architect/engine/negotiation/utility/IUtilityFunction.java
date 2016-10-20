package architect.engine.negotiation.utility;

/**
 * Interface necesaria para definir una funcion de utilidad
 */

public interface IUtilityFunction {
	
	/**
	 * Retorna la utilidad asociada a un costo dado
	 * 
	 * @param cost
	 * @return
	 */
	public double getUtility(double cost);

}
