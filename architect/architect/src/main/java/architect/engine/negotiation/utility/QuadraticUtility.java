package architect.engine.negotiation.utility;

/**
 * Implementacion de funcion cuadratica de utilidad de acuerdo a la ecuacion Y = (X+a)^2
 */

public class QuadraticUtility implements IUtilityFunction{

	double a;
	
	public QuadraticUtility(double a){
		this.a = a;
	}
	
	@Override
	public double getUtility(double cost){
		if (cost < a)
			return 0;
		return Math.pow(cost + a, 2);
	}
	
}
