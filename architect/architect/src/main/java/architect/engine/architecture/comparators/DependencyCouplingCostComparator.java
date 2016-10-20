package architect.engine.architecture.comparators;

import architect.engine.architecture.statistics.ResponsibilityCalculation;

public class DependencyCouplingCostComparator extends ChainedComparator<ResponsibilityCalculation> {

//	@Override
//	public int compare(ResponsibilityCalculation o1, ResponsibilityCalculation o2) {
//		Double d1 = ((Dependency) o1).getCouplingcost();
//		Double d2 = ((Dependency) o2).getCouplingcost();
//		return d2.compareTo(d1);
//	}

	public DependencyCouplingCostComparator(){
		setNext(null);
	}
	
	@Override
	public int doCompare(ResponsibilityCalculation o1,
			ResponsibilityCalculation o2) {
		Double d1 = o1.getAverageDependency();
		Double d2 = o2.getAverageDependency();
		return d2.compareTo(d1);
	}

}
