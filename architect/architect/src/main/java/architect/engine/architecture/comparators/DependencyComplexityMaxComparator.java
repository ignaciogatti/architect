package architect.engine.architecture.comparators;

import java.util.Comparator;

import architect.engine.architecture.statistics.ResponsibilityCalculation;

public class DependencyComplexityMaxComparator extends
		ChainedComparator<ResponsibilityCalculation> {


	public DependencyComplexityMaxComparator(
			Comparator<ResponsibilityCalculation> next) {
		super(next);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int doCompare(ResponsibilityCalculation o1,
			ResponsibilityCalculation o2) {
		Double d1=Math.max(o1.getR1().getResponsibility().getComplexity(), o1.getR2().getResponsibility().getComplexity());
		Double d2=Math.max(o2.getR1().getResponsibility().getComplexity(), o2.getR2().getResponsibility().getComplexity());
		return d2.compareTo(d1);
	}

}
