package architect.engine.negotiation.utility;

/**
 * Implementacion de funcion exponencial de utilidad de acuerdo a la ecuacion Y = b^X
 */

public class ExponentialUtility implements IUtilityFunction{

	double b;
	
	public ExponentialUtility(double b){
		this.b = b;
	}
	
	@Override
	public double getUtility(double cost){
		return Math.pow(b, cost);
	}
	
}
