package architect.engine.architecture.statistics;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import architect.engine.architecture.ElementSet;
import architect.engine.architecture.comparators.ResponsibilityComplexityComparator;
import architect.model.Element;
import architect.model.Responsibility;

public class ResponsibilityCalculation implements Element {
	
	private ResponsibilityStatistics r1;
	private ResponsibilityStatistics r2;
	private Double sharedPercentaje;
	private Double averageDependency;

	public ResponsibilityCalculation(ResponsibilityStatistics r1, ResponsibilityStatistics r2) {
		super();
		this.r1 = r1;
		this.r2 = r2;
		// sharedPercentaje = calculatetSharedPercentage(); //<--DESCOMENTAR
		// DESPUES DE ARREGLAR EL ERROR
		averageDependency = calculateAverageCouplingCost();
	}

	public Double calculatetSharedPercentage() {
		List<Responsibility> r1List = r1.getDependenciesOf();
		List<Responsibility> r2List = r2.getDependenciesOf();
		ResponsibilityComplexityComparator rc = new ResponsibilityComplexityComparator();
		Set<Element> s1 = new TreeSet<Element>(rc);
		s1.addAll(r1List);
		Set<Element> s2 = new TreeSet<Element>(rc);
		s2.addAll(r2List);
		int size = ElementSet.intersection(s1, s2, rc).size();
		return (double) ((size / (double) r1List.size()) * (size / (double) r2List.size()));
	}

	public Double calculateAverageCouplingCost() {
		return (r1.getCoupling(r2) + r2.getCoupling(r1)) / 2;
	}

	public ResponsibilityStatistics getR1() {
		return r1;
	}

	public void setR1(ResponsibilityStatistics r1) {
		this.r1 = r1;
	}

	public ResponsibilityStatistics getR2() {
		return r2;
	}

	public void setR2(ResponsibilityStatistics r2) {
		this.r2 = r2;
	}

	public Double getsharedPercentaje() {
		return sharedPercentaje;
	}

	@Override
	public boolean equals(Object obj) {
		ResponsibilityStatistics r1aux = ((ResponsibilityCalculation) obj).getR1();
		ResponsibilityStatistics r2aux = ((ResponsibilityCalculation) obj).getR2();
		return ((r1.equals(r1aux) && r2.equals(r2aux)) || (r1.equals(r2aux) && r2.equals(r1aux)));
	}

	public Double getAverageDependency() {
		return averageDependency;
	}
}
