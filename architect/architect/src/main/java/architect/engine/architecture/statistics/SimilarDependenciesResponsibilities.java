package architect.engine.architecture.statistics;

import java.util.ArrayList;
import java.util.List;

import architect.model.Element;
import architect.model.Responsibility;


public class SimilarDependenciesResponsibilities implements Element {

	private List<Responsibility> responsibilities;
	
	public SimilarDependenciesResponsibilities() {
		this.responsibilities = new ArrayList<Responsibility>();
	}
	
	public List<Responsibility> getResponsibilities() {
		return this.responsibilities;
	}
	
	public void addResponsibility(Responsibility toAdd) {
		this.responsibilities.add(toAdd);
	}
	
	public void addAllResponsibility(List<Responsibility> toAdd) {
		this.responsibilities.addAll(toAdd);
	}
	
	public boolean areGoodCandidates() {
		for (Responsibility responsibility : this.responsibilities) {
			if (!ResponsibilityStatistics.hasSameChildren(this.responsibilities.get(0), responsibility))
				return false;
		}
		return true;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof SimilarDependenciesResponsibilities) {
			SimilarDependenciesResponsibilities other = (SimilarDependenciesResponsibilities) o;
			List<Responsibility> otherResponsibilities = other.getResponsibilities();
			if (this.responsibilities.size() == otherResponsibilities.size()) {
				for (Responsibility responsibility : this.responsibilities) {
					if (!otherResponsibilities.contains(responsibility)) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else
			return false;
	}
	
}
