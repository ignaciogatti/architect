package architect.engine.attribute.modifiability;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import architect.engine.TacticExecutor;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.engine.architecture.algorithms.filters.Filter;
import architect.engine.architecture.algorithms.filters.GreaterComplexityResposibilityFilter;
import architect.engine.architecture.statistics.ScenarioStatistics;
import architect.model.Architecture;
import architect.model.Dependency;
import architect.model.DependencyId;
import architect.model.Element;
import architect.model.ElementChange;
import architect.model.Module;
import architect.model.Responsibility;
import architect.model.Scenario;

public class SplitResponsibility implements TacticExecutor, Cloneable {
	
	@Override
	public Filter getDefaultFilter() {
		return new GreaterComplexityResposibilityFilter();
	}
	
	@Override
	public List<Element> getElementsToIterateTactic(Architecture architecture, Scenario scenario) {
		return new ArrayList<Element>(scenario.getResponsibilities());
	}
	
	@Override
	public Element getElementToApplyTactic(Architecture architecture, Element element) {
		List<Responsibility> newResponsibilities = new ArrayList<Responsibility>(architecture.getResponsibilities());
		return newResponsibilities.get(newResponsibilities.indexOf((Responsibility) element));
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
		Responsibility responsibilityToSplit = (Responsibility) element;
		//System.out.println("  Applied Element : Responsibility " + responsibilityToSplit.getName());
		responsibilityToSplit.setComplexity(responsibilityToSplit.getComplexity() * 0.5); //Divide complexity in two

		changes.add(new ElementChange(null, ElementType.RESPONSIBILITY
				.toString(), ChangeType.UPDATE.toString(), null,
				responsibilityToSplit, true, false));
		
		//Create new Module
		Module newModule = new Module();
		newModule.setId(new Long(architecture.getModules().size() + 1) * (-1));
		newModule.setName(new String("New Module"));
		newModule.setArchitecture(architecture);
		
		changes.add(new ElementChange(null, ElementType.MODULE
				.toString(), ChangeType.INSERT.toString(), null,
				newModule, false, false));
		
		//Create new Responsibility
		Responsibility newResponsibility = new Responsibility();
		newResponsibility.setId(new Long(architecture.getResponsibilities().size() + 1) * (-1));
		newResponsibility.setName(new String("New Responsibility"));
		newResponsibility.setDescription(new String("Auto-created"));
		newResponsibility.setComplexity(responsibilityToSplit.getComplexity());
		newResponsibility.setExecutionTime(responsibilityToSplit.getExecutionTime());
		newResponsibility.setArchitecture(architecture);
		newResponsibility.getScenarios().add(scenario);
		newResponsibility.setModule(newModule);
		
		architecture.getResponsibilities().add(newResponsibility);
		scenario.getResponsibilities().add(newResponsibility);
		newModule.getResponsibilities().add(newResponsibility);
		architecture.getModules().add(newModule);
		
		changes.add(new ElementChange(null, ElementType.RESPONSIBILITY
				.toString(), ChangeType.INSERT.toString(), null,
				newResponsibility, false, false));
	
		//Duplicates child dependencies and update coupling
		Set<Dependency> depAsChild = responsibilityToSplit.getDependenciesAsChild();
		for (Dependency childDep : depAsChild) {
			childDep.setCouplingcost(childDep.getCouplingcost() * 0.5); //Divide coupling in two
			Dependency newChildDependency = childDep.clone();
			newChildDependency.setParentResponsibility(childDep.getParentResponsibility());
			newChildDependency.setChildResponsibility(newResponsibility);
			newChildDependency.getId().setChild(new Long(newResponsibility.getId()));
			newChildDependency.setArchitecture(architecture);
			newResponsibility.getDependenciesAsChild().add(newChildDependency);
			childDep.getParentResponsibility().getDependenciesAsParent().add(newChildDependency);
			architecture.getDependencies().add(newChildDependency);
			
			changes.add(new ElementChange(null, ElementType.DEPENDENCY
					.toString(), ChangeType.UPDATE.toString(), null,
					childDep, false, false));
			
			changes.add(new ElementChange(null, ElementType.DEPENDENCY
					.toString(), ChangeType.INSERT.toString(), null,
					newChildDependency, false, false));
		}
		
		//Duplicates parent dependencies and update coupling
		Set<Dependency> depAsParent = responsibilityToSplit.getDependenciesAsParent();
		for (Dependency parentDep : depAsParent) {
			parentDep.setCouplingcost(parentDep.getCouplingcost() * 0.5); //Divide coupling in two
			Dependency newParentDependency = parentDep.clone();
			newParentDependency.setParentResponsibility(newResponsibility);
			newParentDependency.setChildResponsibility(parentDep.getChildResponsibility());
			newParentDependency.getId().setParent(new Long(newResponsibility.getId()));
			newParentDependency.setArchitecture(architecture);
			newResponsibility.getDependenciesAsParent().add(newParentDependency);
			parentDep.getChildResponsibility().getDependenciesAsChild().add(newParentDependency);
			architecture.getDependencies().add(newParentDependency);
			
			changes.add(new ElementChange(null, ElementType.DEPENDENCY
					.toString(), ChangeType.UPDATE.toString(), null,
					parentDep, false, false));
			
			changes.add(new ElementChange(null, ElementType.DEPENDENCY
					.toString(), ChangeType.INSERT.toString(), null,
					newParentDependency, false, false));
		}
		
		//Create new dependency between the split responsibility and the new one
		Dependency newDependency = new Dependency();
		newDependency.setId(new DependencyId(new Long(responsibilityToSplit.getId()), new Long(newResponsibility.getId())));
		newDependency.setParentResponsibility(responsibilityToSplit);
		newDependency.setChildResponsibility(newResponsibility);
		newDependency.setCouplingcost(Dependency.DEFAULT_COUPLING_COST);
		newDependency.setArchitecture(architecture);
		
		changes.add(new ElementChange(null, ElementType.DEPENDENCY
				.toString(), ChangeType.INSERT.toString(), null,
				newDependency, false, false));
		
		responsibilityToSplit.getDependenciesAsParent().add(newDependency);
		newResponsibility.getDependenciesAsChild().add(newDependency);
		architecture.getDependencies().add(newDependency);
		
	}

}
