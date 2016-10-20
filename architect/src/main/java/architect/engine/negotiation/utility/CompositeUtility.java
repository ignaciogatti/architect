package architect.engine.negotiation.utility;

/**
 * Implementación de una función de utilidad compuesta. 
 * Si costo <= limit -> f1 sino f2 
 * @author Ariel
 *
 */

public class CompositeUtility implements IUtilityFunction {
	
	private IUtilityFunction f1, f2;
	private double limit;

	public CompositeUtility(IUtilityFunction f1, IUtilityFunction f2, double limit) {
		this.f1 = f1;
		this.f2 = f2;
		this.limit = limit;
	}
	
	@Override
	public double getUtility(double cost) {
		if (f1 == null || f2 == null )
			return 0;
		
		if (cost <= limit)
			return f1.getUtility(cost);
		else
			return f2.getUtility(cost);
		
	}

}
