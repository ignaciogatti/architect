package architect.engine.negotiation.zeuthen;

import java.util.ArrayList;
import java.util.List;

import architect.engine.DBNegotiationExecutor;
import architect.engine.DesignBotExecutor;
import architect.model.Scenario;

/**
 * 
 * Permite calcular el incremento del producto de utilidades para determinar el valor de zeuthen
 *
 */
public class ProductIncreasingFunction implements IZeuthenFunction{
	
	@Override
	public double getZeuthenValue(DBNegotiationExecutor agent,
			List<DesignBotExecutor> agents) {
		List<Scenario> archiScenarios = new ArrayList<Scenario>(agent.getActualCandidate().getArchitecture().getScenarios());
		double product = 1;
		for (DesignBotExecutor agent2 : agents){
			double cost = agent2.getScenarioCost(archiScenarios.get(archiScenarios.indexOf(agent2.getArchitectureAnalysis().getScenario())));
			product = product * ((DBNegotiationExecutor) agent2).getUtility(cost);
		}
		return product;
	}

}
