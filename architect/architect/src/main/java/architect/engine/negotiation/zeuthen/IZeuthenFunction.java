package architect.engine.negotiation.zeuthen;

import java.util.List;

import architect.engine.DBNegotiationExecutor;
import architect.engine.DesignBotExecutor;

/**
 * 
 * Interfaz que define el calculo del valor de zeuthen para la concesion de agentes
 *
 */
public interface IZeuthenFunction {
	
	
	public static final double INFINITE_VALUE = 999999;
	
	/**
	 * Retorna el valor de zeuthen de un agente dado
	 * @param agent agente seleccionado
	 * @param agents lista de agentes involucrados en la negociacion
	 * @return valor de zeuthen
	 */
	public double getZeuthenValue(DBNegotiationExecutor agent,List<DesignBotExecutor> agents);

}
