package architect.engine.architecture.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import architect.engine.architecture.ElementSet;
import architect.engine.attribute.modifiability.AbstractCommonResponsibilities;
import architect.model.Dependency;
import architect.model.Responsibility;

public class ResponsibilityStatistics {

	private Responsibility responsibility;
	public static final Double DEFAULT_RESPONSIBILITY_COST = 7.5;
	public double responsibilityModifiabilityCost;

	public ResponsibilityStatistics(Responsibility responsibility) {
		this.responsibility = responsibility;
		calculateResponsibilityModificabilityCost();
	}

	public static Double getPercentage(List<Responsibility> o1,
			List<Responsibility> o2) {
		Set<Responsibility> s1 = new TreeSet<Responsibility>(o1);
		Set<Responsibility> s2 = new TreeSet<Responsibility>(o2);
		int size = ElementSet.intersection(s1, s2).size();
		return (double) ((o1.size() / size) * (o2.size() / size));
	}
	
	public static Double getAllDependenciesCouplingCost(Responsibility responsibility){
		Double coupling = new Double(0);
		for (Dependency dependency : responsibility.getDependenciesAsChild())
			coupling += dependency.getCouplingcost();
		for (Dependency dependency : responsibility.getDependenciesAsParent())
			coupling += dependency.getCouplingcost();
		return coupling;
	}
	
	public Double getCoupling(ResponsibilityStatistics dest){
		Double coupling=0.0;
		ModuleStatistics msOr=new ModuleStatistics(responsibility.getModule());
		Dependency dd=findDependency(responsibility,dest.getResponsibility(), msOr.getOuterDependencies());
		if(dd!=null)
			coupling+=dd.getCouplingcost();
		return coupling;
	}
	
	public Double getTotalCoupling(){
		Double coupling = 0.0;
		for (Responsibility r : getDependenciesOf()) {
			ResponsibilityStatistics res = new ResponsibilityStatistics(r);
			coupling += getCoupling(res) + res.getCoupling(this);
		}
		return coupling;
	}

	public List<Responsibility> getDependenciesOf() {
		ModuleStatistics ms = new ModuleStatistics(responsibility.getModule());		
		return ms.getHashOuterDependencies() != null ? ms
				.getHashOuterDependencies().get(responsibility) : null;
	}
	
	public Dependency findDependency(Responsibility orig, Responsibility child, List<Dependency> list){
		for(Dependency d:list){
			if(d.getParentResponsibility().equals(orig) && d.getChildResponsibility().equals(child))
				return d;
		}
		return null;	
	}

	public List<ResponsibilityCalculation> getSimilarServiceCandidates() {
		List<ResponsibilityCalculation> resultList = new ArrayList<ResponsibilityCalculation>();
		resultList.addAll(this.getSimilarServiceCandidatesFromOtherModules());
		resultList.addAll(this.getSimilarServiceCandidatesFromSameModules());
		return resultList;
	}
	
	private List<ResponsibilityCalculation> getSimilarServiceCandidatesFromOtherModules() {
		List<ResponsibilityCalculation> result = new ArrayList<ResponsibilityCalculation>();
		List<Dependency> childs = new ArrayList<Dependency>(responsibility.getDependenciesAsChild());
		List<Dependency> parents = new ArrayList<Dependency>(responsibility.getDependenciesAsParent());
		ResponsibilityCalculation rc1;
		for(Dependency imTheChild : childs){
			//si la dependencia no esta dentro del mismo modulo
			if(!imTheChild.getParentResponsibility().getModule().equals(imTheChild.getChildResponsibility().getModule())){
				Dependency dd=findDependency(responsibility,imTheChild.getParentResponsibility(),parents);
				if(dd!=null){
					rc1=new ResponsibilityCalculation(new ResponsibilityStatistics(imTheChild.getParentResponsibility()),this);
					if(rc1.getAverageDependency()>AbstractCommonResponsibilities.THRESHOLD_COST){
						result.add(rc1);
						result.add(new ResponsibilityCalculation(this,new ResponsibilityStatistics(imTheChild.getParentResponsibility())));
					}
					
				}
			}	
		}
		return result;
	}
	
	private List<ResponsibilityCalculation> getSimilarServiceCandidatesFromSameModules() {
		List<ResponsibilityCalculation> result = new ArrayList<ResponsibilityCalculation>();
		List<Dependency> dependenciesAsChild = new ArrayList<Dependency>(responsibility.getDependenciesAsChild());
		
		boolean hasExternalParent = false;
		for(Dependency imTheChild : dependenciesAsChild){
			if (!responsibility.getModule().equals(imTheChild.getParentResponsibility().getModule())) {
				hasExternalParent = true;	
			}	
		}
		
		if (hasExternalParent) {
			List<Responsibility> responsibilitiesWithExternalParents = this.getResponsibilitiesWithExternalParents();
			for (Responsibility responsibilityExt : responsibilitiesWithExternalParents) {
				if (hasSameChildren(responsibility, responsibilityExt)) {
					result.add( new ResponsibilityCalculation(new ResponsibilityStatistics(responsibilityExt),this) );
					result.add( new ResponsibilityCalculation(this, new ResponsibilityStatistics(responsibilityExt)) );
				}
			}
		}
		return result;
	}
	
	private List<Responsibility> getResponsibilitiesWithExternalParents() {
		List<Responsibility> resultList = new ArrayList<Responsibility>();
		Set<Responsibility> responsibilities = responsibility.getModule().getResponsibilities();
		for (Responsibility otherResponsibility : responsibilities) {
			if (!responsibility.equals(otherResponsibility)) {
				for (Dependency dependency : otherResponsibility.getDependenciesAsChild()) {
					if (!otherResponsibility.getModule().equals(dependency.getParentResponsibility().getModule())) {
						if (!resultList.contains(otherResponsibility)) {
							resultList.add(otherResponsibility);
						}
					}
				}
			}
		}
		return resultList;
	}
	
	public static boolean hasSameChildren(Responsibility r1, Responsibility r2) {
		Set<Dependency> dependenciesAsParentR1 = r1.getDependenciesAsParent();
		List<Responsibility> childResponsibilitiesOfR1 = new ArrayList<Responsibility>();
		for (Dependency dependencyAsParentR1 : dependenciesAsParentR1) {
			childResponsibilitiesOfR1.add(dependencyAsParentR1.getChildResponsibility());
		}
		
		Set<Dependency> dependenciesAsParentR2 = r2.getDependenciesAsParent();
		List<Responsibility> childResponsibilitiesOfR2 = new ArrayList<Responsibility>();
		for (Dependency dependencyAsParentR2 : dependenciesAsParentR2) {
			childResponsibilitiesOfR2.add(dependencyAsParentR2.getChildResponsibility());
		}
		
		boolean hasSameChildren = true;
		if (childResponsibilitiesOfR2.isEmpty()) {
			hasSameChildren = false;
		}
		for (Responsibility childResponsibilityOfR2 : childResponsibilitiesOfR2) {
			if (!childResponsibilitiesOfR1.contains(childResponsibilityOfR2)) {
				hasSameChildren = false;
			}
		}
		
		return hasSameChildren;
	}

	public Responsibility getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(Responsibility responsibility) {
		this.responsibility = responsibility;
	}

	@Override
	public boolean equals(Object obj) {
		return responsibility.equals(((ResponsibilityStatistics) obj).getResponsibility());
	}

	private void calculateResponsibilityModificabilityCost() {
		responsibilityModifiabilityCost = this.responsibility.getComplexity();
		for (Dependency dependency : this.responsibility.getDependenciesAsChild()) {
			responsibilityModifiabilityCost += (dependency.getCouplingcost() * dependency.getParentResponsibility().getComplexity()) * 0.2;
		}
		for (Dependency dependency :  this.responsibility.getDependenciesAsParent()) {
			responsibilityModifiabilityCost += (dependency.getCouplingcost() * dependency.getChildResponsibility().getComplexity());
		}
	}
	
	public Double getModifiabilityCost() {
		return responsibilityModifiabilityCost;
	}

}
