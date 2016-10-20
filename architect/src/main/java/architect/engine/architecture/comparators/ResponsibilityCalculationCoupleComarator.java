package architect.engine.architecture.comparators;

import java.util.Comparator;

import architect.engine.architecture.statistics.ResponsibilityCalculation;

public class ResponsibilityCalculationCoupleComarator implements
		Comparator<ResponsibilityCalculation> {

	@Override
	public int compare(ResponsibilityCalculation o1,
			ResponsibilityCalculation o2) {
		Double averageCouplingO1=o1.calculateAverageCouplingCost();
		Double averageCouplingO2=o2.calculateAverageCouplingCost();
		return averageCouplingO2.compareTo(averageCouplingO1);
	}

}
