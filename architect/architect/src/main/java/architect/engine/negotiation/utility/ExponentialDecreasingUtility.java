package architect.engine.negotiation.utility;


/**
 * Implementacion de funcion exponencial decreciente de utilidad
 * de acuerdo a la ecuacion U(c) = (T/c)^(1/X) donde:
 * 	- c es el costo de la propuesta
 *  - T es el umbral de costo (Response)
 *  - X es el factor de decrecimiento, a menor valor de X más decrece la utilidad con el aumento del costo
 *  
 * @author Ariel
 */
public class ExponentialDecreasingUtility implements IUtilityFunction {
	
	private double t;
	private double x;
	
	public ExponentialDecreasingUtility(double t, double x) {
		this.t = t;
		this.x = x;
	}

	@Override
	public double getUtility(double cost) {
		if (cost > 0)
			return Math.pow(t/cost, 1/x);
		else
			return 1;
	}
	

}
