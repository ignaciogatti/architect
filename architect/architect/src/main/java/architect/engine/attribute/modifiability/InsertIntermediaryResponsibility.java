package architect.engine.attribute.modifiability;

import java.util.ArrayList;
import java.util.List;

import architect.engine.TacticExecutor;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.engine.architecture.algorithms.filters.Filter;
import architect.engine.architecture.algorithms.filters.GreaterCouplingModuleFilter;
import architect.engine.architecture.statistics.ModuleStatistics;
import architect.engine.architecture.statistics.ScenarioStatistics;
import architect.model.Architecture;
import architect.model.Dependency;
import architect.model.DependencyId;
import architect.model.Element;
import architect.model.ElementChange;
import architect.model.Module;
import architect.model.Responsibility;
import architect.model.Scenario;

public class InsertIntermediaryResponsibility implements TacticExecutor, Cloneable {
	
	@Override
	public Filter getDefaultFilter() {
		return new GreaterCouplingModuleFilter();
	}
	
	@Override
	public List<Element> getElementsToIterateTactic(Architecture architecture, Scenario scenario) {
		return new ArrayList<Element>(architecture.getModules());
	}
	
	@Override
	public Element getElementToApplyTactic(Architecture architecture, Element element) {
		List<Module> newModules = new ArrayList<Module>(architecture.getModules());
		return newModules.get(newModules.indexOf((Module) element));
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
		double dependencyDivisor = 0.2;
		ModuleStatistics moduleStats = new ModuleStatistics((Module) element);
		//System.out.println("  Applied Element : Module " + moduleStats.getModule().getName());
		//Create new Module
		Module newModule = new Module();
		newModule.setId(new Long(architecture.getModules().size() + 1) * (-1));
		newModule.setName(new String("New Module"));
		newModule.setArchitecture(architecture);
		
		changes.add(new ElementChange(null, ElementType.MODULE
				.toString(), ChangeType.INSERT.toString(), null,
				newModule, true, false));
		
		//Create new Responsibility
		Responsibility newResponsibility = new Responsibility();
		newResponsibility.setId(new Long(architecture.getResponsibilities().size() + 1) * (-1));
		newResponsibility.setName(new String("New Responsibility"));
		newResponsibility.setDescription(new String("Auto-created"));
		newResponsibility.setModule(newModule);
		newResponsibility.setArchitecture(architecture);
		newResponsibility.setComplexity(Responsibility.DEFAULT_COMPLEXITY_COST);
		newResponsibility.setExecutionTime(Responsibility.DEFAULT_EXECUTION_TIME);
		newResponsibility.setModule(newModule);
		newResponsibility.getScenarios().add((Scenario) scenario);
		
		changes.add(new ElementChange(null, ElementType.RESPONSIBILITY
				.toString(), ChangeType.INSERT.toString(), null,
				newResponsibility, false, false));
		
		newModule.getResponsibilities().add(newResponsibility);
		architecture.getResponsibilities().add(newResponsibility);
		architecture.getModules().add(newModule);
		scenario.getResponsibilities().add(newResponsibility);		
		
		List<Dependency> outerDependencies = moduleStats.getOuterDependencies();
		for (Dependency dependency : outerDependencies) {
			
			if (moduleStats.getModule().equals(dependency.getParentResponsibility().getModule())) {
				
				List<Dependency> dependenciesList = new ArrayList<Dependency>(architecture.getDependencies());
				int indexDependency = dependenciesList.indexOf(new Dependency(new DependencyId(newResponsibility.getId(),dependency.getId().getChild())));
				if (indexDependency < 0) {
					//Create new dependency and connect it
					Dependency newDependecy = new Dependency();
					newDependecy.setId(new DependencyId(new Long(newResponsibility.getId()),new Long(dependency.getChildResponsibility().getId())));
					newDependecy.setParentResponsibility(newResponsibility);
					newDependecy.setChildResponsibility(dependency.getChildResponsibility());
					newDependecy.setCouplingcost(dependency.getCouplingcost() * dependencyDivisor); //Divide coupling in two
					newDependecy.setArchitecture(architecture);
					newResponsibility.getDependenciesAsParent().add(newDependecy);
					dependency.getChildResponsibility().getDependenciesAsChild().add(newDependecy);
					architecture.getDependencies().add(newDependecy);
					
					changes.add(new ElementChange(null, ElementType.DEPENDENCY
							.toString(), ChangeType.INSERT.toString(), null,
							newDependecy, false, false));
				} else {
					//Update existent dependency cost
					Dependency dependencyToUpdate = dependenciesList.get(indexDependency);
					dependencyToUpdate.setCouplingcost(dependencyToUpdate.getCouplingcost() + dependency.getCouplingcost() * dependencyDivisor);
					
					changes.add(new ElementChange(null, ElementType.DEPENDENCY
							.toString(), ChangeType.UPDATE.toString(), null,
							dependencyToUpdate, false, false));
				}
				
				List<Dependency> dependenciesList2 = new ArrayList<Dependency>(architecture.getDependencies());
				int indexDependency2 = dependenciesList2.indexOf(new Dependency(new DependencyId(dependency.getId().getParent(),newResponsibility.getId())));
				if (indexDependency2 < 0) {
					//Create new dependency and connect it
					Dependency newDependecy = new Dependency();
					newDependecy.setId(new DependencyId(new Long(dependency.getParentResponsibility().getId()),new Long(newResponsibility.getId())));
					newDependecy.setParentResponsibility(dependency.getParentResponsibility());
					newDependecy.setChildResponsibility(newResponsibility);
					newDependecy.setCouplingcost(dependency.getCouplingcost() * dependencyDivisor); //Divide coupling in two
					newDependecy.setArchitecture(architecture);
					newResponsibility.getDependenciesAsChild().add(newDependecy);
					dependency.getParentResponsibility().getDependenciesAsParent().add(newDependecy);
					architecture.getDependencies().add(newDependecy);
					
					changes.add(new ElementChange(null, ElementType.DEPENDENCY
							.toString(), ChangeType.INSERT.toString(), null,
							newDependecy, false, false));
					
				} else {
					//Update existent dependency cost
					Dependency dependencyToUpdate = dependenciesList2.get(indexDependency2);
					dependencyToUpdate.setCouplingcost(dependencyToUpdate.getCouplingcost() + dependency.getCouplingcost() * dependencyDivisor);
					
					changes.add(new ElementChange(null, ElementType.DEPENDENCY
							.toString(), ChangeType.UPDATE.toString(), null,
							dependencyToUpdate, false, false));
				}
							
			} else if (moduleStats.getModule().equals(dependency.getChildResponsibility().getModule())) {
				
				List<Dependency> dependenciesList = new ArrayList<Dependency>(architecture.getDependencies());
				int indexDependency = dependenciesList.indexOf(new Dependency(new DependencyId(dependency.getId().getParent(),newResponsibility.getId())));
				if (indexDependency < 0) {
					//Create new dependency and connect it
					Dependency newDependecy = new Dependency();
					newDependecy.setId(new DependencyId(new Long(dependency.getParentResponsibility().getId()),new Long(newResponsibility.getId())));
					newDependecy.setChildResponsibility(newResponsibility);
					newDependecy.setParentResponsibility(dependency.getParentResponsibility());
					newDependecy.setCouplingcost(dependency.getCouplingcost() * dependencyDivisor); //Divide coupling in two
					newDependecy.setArchitecture(architecture);
					newResponsibility.getDependenciesAsChild().add(newDependecy);
					dependency.getParentResponsibility().getDependenciesAsParent().add(newDependecy);
					architecture.getDependencies().add(newDependecy);
					
					changes.add(new ElementChange(null, ElementType.DEPENDENCY
							.toString(), ChangeType.INSERT.toString(), null,
							newDependecy, false, false));
				} else {
					//Update existent dependency cost
					Dependency dependencyToUpdate = dependenciesList.get(indexDependency);
					dependencyToUpdate.setCouplingcost(dependencyToUpdate.getCouplingcost() + dependency.getCouplingcost() * dependencyDivisor);
					
					changes.add(new ElementChange(null, ElementType.DEPENDENCY
							.toString(), ChangeType.UPDATE.toString(), null,
							dependencyToUpdate, false, false));
				}
				
				List<Dependency> dependenciesList2 = new ArrayList<Dependency>(architecture.getDependencies());
				int indexDependency2 = dependenciesList2.indexOf(new Dependency(new DependencyId(newResponsibility.getId(),dependency.getId().getChild())));
				if (indexDependency2 < 0) {
					//Create new dependency and connect it
					Dependency newDependecy = new Dependency();
					newDependecy.setId(new DependencyId(new Long(newResponsibility.getId()),new Long(dependency.getId().getChild())));
					newDependecy.setParentResponsibility(newResponsibility);
					newDependecy.setChildResponsibility(dependency.getChildResponsibility());
					newDependecy.setCouplingcost(dependency.getCouplingcost() * dependencyDivisor); //Divide coupling in two
					newDependecy.setArchitecture(architecture);
					newResponsibility.getDependenciesAsParent().add(newDependecy);
					dependency.getChildResponsibility().getDependenciesAsChild().add(newDependecy);
					architecture.getDependencies().add(newDependecy);
					
					changes.add(new ElementChange(null, ElementType.DEPENDENCY
							.toString(), ChangeType.INSERT.toString(), null,
							newDependecy, false, false));
					
				} else {
					//Update existent dependency cost
					Dependency dependencyToUpdate = dependenciesList2.get(indexDependency2);
					dependencyToUpdate.setCouplingcost(dependencyToUpdate.getCouplingcost() + dependency.getCouplingcost() * dependencyDivisor);
					
					changes.add(new ElementChange(null, ElementType.DEPENDENCY
							.toString(), ChangeType.UPDATE.toString(), null,
							dependencyToUpdate, false, false));
				}
				
			}
			
			//Delete old dependency
			architecture.getDependencies().remove(dependency);
			dependency.getChildResponsibility().getDependenciesAsChild().remove(dependency);
			dependency.getParentResponsibility().getDependenciesAsParent().remove(dependency);
			
			changes.add(new ElementChange(null, ElementType.DEPENDENCY
					.toString(), ChangeType.DELETE.toString(), null,
					dependency, false, false));
			
		}
		
	}

}
