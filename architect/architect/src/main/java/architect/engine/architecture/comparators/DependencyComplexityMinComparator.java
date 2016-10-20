package architect.engine.architecture.comparators;

import java.util.Comparator;

import architect.engine.architecture.statistics.ResponsibilityCalculation;

public class DependencyComplexityMinComparator extends
		ChainedComparator<ResponsibilityCalculation> {

	public DependencyComplexityMinComparator(
			Comparator<ResponsibilityCalculation> next) {
		super(next);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int doCompare(ResponsibilityCalculation o1,
			ResponsibilityCalculation o2) {
		Double d1=Math.min(o1.getR1().getResponsibility().getComplexity(), o1.getR2().getResponsibility().getComplexity());
		Double d2=Math.min(o2.getR1().getResponsibility().getComplexity(), o2.getR2().getResponsibility().getComplexity());
		return d2.compareTo(d1);

	}

}
