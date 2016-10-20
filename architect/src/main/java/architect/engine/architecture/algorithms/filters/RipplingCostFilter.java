package architect.engine.architecture.algorithms.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import architect.engine.architecture.comparators.DependencyComplexityMaxComparator;
import architect.engine.architecture.comparators.DependencyComplexityMinComparator;
import architect.engine.architecture.comparators.DependencyCouplingCostComparator;
import architect.engine.architecture.statistics.ResponsibilityCalculation;
import architect.engine.architecture.statistics.ResponsibilityStatistics;
import architect.model.Element;
import architect.model.Responsibility;
import architect.model.Scenario;

public class RipplingCostFilter implements Filter {

	@Override
	public List<Element> getFilteredElements(Element toAnalize) {
		
		List<ResponsibilityCalculation> candidates = new ArrayList<ResponsibilityCalculation>();
		
		for (Responsibility responsibility : ((Scenario) toAnalize).getResponsibilities()) {
			ResponsibilityStatistics responsibilityStatistics = new ResponsibilityStatistics(responsibility);

			/*
			 * 1-obtener todas las dependencias externas de ida y vuelta del
			 * escenario cuyo costo de dependencia promedio sea > THRESHOLD_COST
			 */
			for (ResponsibilityCalculation newOne : responsibilityStatistics.getSimilarServiceCandidates()) {
				if (!candidates.contains(newOne))
					candidates.add(newOne);
			}
		}
		
		if (candidates.isEmpty())
			return null;
		/*
		 * 2-ordenar las responsabilidades por su costo individual. Si tienen
		 * igual costo ordenar por el costo de de la asociada. Si siguen
		 * teniendo igual costo ordenar por el costo de dependencia promedio
		 * entre ambas
		 */
		Comparator<ResponsibilityCalculation> order = new DependencyComplexityMaxComparator(
				new DependencyComplexityMinComparator(
						new DependencyCouplingCostComparator()));
		Collections.sort(candidates, order);
		
		return new ArrayList<Element>(candidates);
	}

}
