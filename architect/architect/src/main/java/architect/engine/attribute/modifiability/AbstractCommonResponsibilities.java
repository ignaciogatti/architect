package architect.engine.attribute.modifiability;

import java.util.ArrayList;
import java.util.List;

import architect.engine.TacticExecutor;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.engine.architecture.algorithms.filters.Filter;
import architect.engine.architecture.algorithms.filters.SimilarResposibilitiesFilter;
import architect.engine.architecture.statistics.ScenarioStatistics;
import architect.engine.architecture.statistics.SimilarDependenciesResponsibilities;
import architect.model.Architecture;
import architect.model.Dependency;
import architect.model.DependencyId;
import architect.model.Element;
import architect.model.ElementChange;
import architect.model.Module;
import architect.model.Responsibility;
import architect.model.Scenario;

public class AbstractCommonResponsibilities implements TacticExecutor,
		Cloneable {

	public static final double THRESHOLD_COST = 0.3;
	
	@Override
	public Filter getDefaultFilter() {
		return new SimilarResposibilitiesFilter();
	}
	
	@Override
	public List<Element> getElementsToIterateTactic(Architecture architecture, Scenario scenario) {
		return new ArrayList<Element>(getDefaultFilter().getFilteredElements(scenario));
	}
	
	@Override
	public Element getElementToApplyTactic(Architecture architecture, Element element) {
		SimilarDependenciesResponsibilities newList = new SimilarDependenciesResponsibilities();
		List<Responsibility> newResponsibilities = new ArrayList<Responsibility>(architecture.getResponsibilities());
		SimilarDependenciesResponsibilities oldList = (SimilarDependenciesResponsibilities) element;
		for (Responsibility oldResponsibility : oldList.getResponsibilities()) {
			Responsibility newResponsibility = newResponsibilities.get(newResponsibilities.indexOf(oldResponsibility));
			newList.addResponsibility(newResponsibility);
		}
		return newList;
	}
	
	@Override
	public int compareScenariosCosts(Scenario scenario1, Scenario scenario2) {
		ScenarioStatistics scenarioStatistics1 = new ScenarioStatistics(scenario1);
		ScenarioStatistics scenarioStatistics2 = new ScenarioStatistics(scenario2);
		Double cost1 = scenarioStatistics1.getScenarioModifiabilityCost();
		Double cost2 = scenarioStatistics2.getScenarioModifiabilityCost();
		return cost1.compareTo(cost2);
	}

	@Override
	public void applyTactic(Architecture architecture, Scenario scenario, Element element, List<ElementChange> changes) {
		SimilarDependenciesResponsibilities similarDepsResponsibilitiesList = (SimilarDependenciesResponsibilities) element;
		
		Module module = similarDepsResponsibilitiesList.getResponsibilities().get(0).getModule();
		
		//Create new Common Responsibility
		Responsibility newResponsibility = new Responsibility();
		newResponsibility.setId(new Long(architecture.getResponsibilities().size() + 1) * (-1));
		newResponsibility.setName(new String("New Responsibility"));
		newResponsibility.setDescription(new String("Auto-created"));
		newResponsibility.setModule(module);
		newResponsibility.setArchitecture(architecture);
		newResponsibility.setComplexity(Responsibility.DEFAULT_COMPLEXITY_COST);
		newResponsibility.setExecutionTime(Responsibility.DEFAULT_EXECUTION_TIME);
		newResponsibility.getScenarios().add((Scenario) scenario);
				
		module.getResponsibilities().add(newResponsibility);
		architecture.getResponsibilities().add(newResponsibility);
		scenario.getResponsibilities().add(newResponsibility);
		
		changes.add(new ElementChange(null, ElementType.RESPONSIBILITY
				.toString(), ChangeType.INSERT.toString(), null,
				newResponsibility, true, false));
		
		for (Responsibility responsibility : similarDepsResponsibilitiesList.getResponsibilities()) {
			//Update old responsibility complexity
			responsibility.setComplexity(responsibility.getComplexity() * 0.5);
			changes.add(new ElementChange(null, ElementType.RESPONSIBILITY
					.toString(), ChangeType.UPDATE.toString(), null,
					responsibility, false, false));
			
			//Create or update new dependencies between old Responsibility parents and newResponsibility, and delete old dependencies
			List<Dependency> dependenciesAsChild = new ArrayList<Dependency>();
			dependenciesAsChild.addAll(responsibility.getDependenciesAsChild());
			
			for (Dependency dependency : dependenciesAsChild) {
				Dependency newDependecy2 = new Dependency();
				newDependecy2.setId(new DependencyId(new Long(dependency.getParentResponsibility().getId()),new Long(newResponsibility.getId())));
				newDependecy2.setParentResponsibility(dependency.getParentResponsibility());
				newDependecy2.setChildResponsibility(newResponsibility);
				newDependecy2.setCouplingcost(dependency.getCouplingcost());
				newDependecy2.setArchitecture(architecture);
				
				List<Dependency> dependenciesList = new ArrayList<Dependency>(architecture.getDependencies());
				int indexDependency = dependenciesList.indexOf(newDependecy2);
				if (indexDependency < 0) {
					newResponsibility.getDependenciesAsChild().add(newDependecy2);
					dependency.getParentResponsibility().getDependenciesAsParent().add(newDependecy2);
					architecture.getDependencies().add(newDependecy2);
					
					changes.add(new ElementChange(null, ElementType.DEPENDENCY
							.toString(), ChangeType.INSERT.toString(), null,
							newDependecy2, false, false));
				} else {
					Dependency dependencyToUpdate = dependenciesList.get(indexDependency);
					dependencyToUpdate.setCouplingcost(dependencyToUpdate.getCouplingcost() + (newDependecy2.getCouplingcost() * 0.5));
					
					changes.add(new ElementChange(null, ElementType.DEPENDENCY
							.toString(), ChangeType.UPDATE.toString(), null,
							dependencyToUpdate, false, false));
				}
				
				//Delete old dependency
				architecture.getDependencies().remove(dependency);
				dependency.getChildResponsibility().getDependenciesAsChild().remove(dependency);
				dependency.getParentResponsibility().getDependenciesAsParent().remove(dependency);
				
				changes.add(new ElementChange(null, ElementType.DEPENDENCY
						.toString(), ChangeType.DELETE.toString(), null,
						dependency, false, false));
			}
			
			//Create new dependency between newResponsibility and old Responsibility
			Dependency newDependecy = new Dependency();
			newDependecy.setId(new DependencyId(new Long(newResponsibility.getId()),new Long(responsibility.getId())));
			newDependecy.setParentResponsibility(newResponsibility);
			newDependecy.setChildResponsibility(responsibility);
			newDependecy.setCouplingcost(Dependency.DEFAULT_COUPLING_COST);
			newDependecy.setArchitecture(architecture);
			newResponsibility.getDependenciesAsParent().add(newDependecy);
			responsibility.getDependenciesAsChild().add(newDependecy);
			architecture.getDependencies().add(newDependecy);
			
			changes.add(new ElementChange(null, ElementType.DEPENDENCY
					.toString(), ChangeType.INSERT.toString(), null,
					newDependecy, false, false));
			
		}
	
	}
    
}