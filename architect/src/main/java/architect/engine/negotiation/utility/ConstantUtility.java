package architect.engine.negotiation.utility;


/**
 * Independientemente de costo devuelve siempre el mismo valor de utilidad
 * NOTA: para usar dentro de una función compuesta
 * @author Ariel
 *
 */
public class ConstantUtility implements IUtilityFunction {

	private double constant;
	
	public ConstantUtility(double constant) {
		this.constant = constant;
	}
	
	@Override
	public double getUtility(double cost) {
		return constant;
	}

}
