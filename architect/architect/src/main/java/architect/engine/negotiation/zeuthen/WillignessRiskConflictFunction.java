package architect.engine.negotiation.zeuthen;

import java.util.ArrayList;
import java.util.List;

import architect.engine.DBNegotiationExecutor;
import architect.engine.DesignBotExecutor;
import architect.model.Scenario;

/**
 * 
 * Implementacion del calculo de propension al riesgo de conflicto para un agente, como resultante asociada al valor de zeuthen
 *
 */
public class WillignessRiskConflictFunction implements IZeuthenFunction {

	@Override
	public double getZeuthenValue(DBNegotiationExecutor agent,
			List<DesignBotExecutor> agents) {
		List<Scenario> archiScenarios = new ArrayList<Scenario>(agent.getActualCandidate().getArchitecture().getScenarios());
		double utility1 = agent.getUtility(archiScenarios.get(archiScenarios.indexOf(agent.getArchitectureAnalysis().getScenario())));
		double min = INFINITE_VALUE;
		if ((utility1 != 0)){
			for (DesignBotExecutor agent2 : agents){
				archiScenarios = new ArrayList<Scenario>(((DBNegotiationExecutor)agent2).getActualCandidate().getArchitecture().getScenarios());
				double utility2 = agent.getUtility(archiScenarios.get(archiScenarios.indexOf(agent.getArchitectureAnalysis().getScenario())));
				if (utility2 < min)
					min = utility2;
			}
			return (utility1 - min) / utility1;
		}
		else
			return 1;
	}

}
