package architect.engine.architecture.comparators;

import java.util.Comparator;

import architect.engine.architecture.statistics.ResponsibilityCalculation;

public class ResponsibilityCalculationPercentajeComparator implements
		Comparator<ResponsibilityCalculation> {

	@Override
	public int compare(ResponsibilityCalculation o1, ResponsibilityCalculation o2) {
		Double percentajeR1=o1.getsharedPercentaje();
		Double percentajeR2=o2.getsharedPercentaje();
		return percentajeR2.compareTo(percentajeR1);
	}

}
