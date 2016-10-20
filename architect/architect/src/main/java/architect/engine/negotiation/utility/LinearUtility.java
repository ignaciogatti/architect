package architect.engine.negotiation.utility;

/**
 * Implementacion de funcion lineal de utilidad de acuerdo a la ecuacion Y = aX + b
 */
public class LinearUtility implements IUtilityFunction{

	double b;
	double a;
	
	public LinearUtility(double a, double b){
		this.a = a;
		this.b = b;
	}
	
	@Override
	public double getUtility(double cost){
		return (a*cost + b);
	}
	
	
	

}
