package architect.engine.architecture.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import architect.model.Module;
import architect.model.Responsibility;
import architect.model.Scenario;

public class ScenarioStatistics {

	private Scenario scenario;
	private List<ModuleStatistics> modulesStatistics;
	private double scenarioCoupling;
	private double scenarioAvgComplexity;
	private double scenarioModifiabilityCost;
	
	
	public ScenarioStatistics(Scenario scenario) {
		this.setScenario(scenario);
		//initModulesStatistics();
		initModulesStatistics2();
		//calculateScenarioModificabilityCost();
	}

	/*
	private void initModulesStatistics() {
		
		List<Responsibility> scenarioResponsibilities = new ArrayList<Responsibility>(this.scenario.getResponsibilities());
		List<Module> scenarioModules = new ArrayList<Module>();
		for(Responsibility scenarioResponsibility : scenarioResponsibilities) {
			if (!scenarioModules.contains(scenarioResponsibility.getModule())) {
				scenarioModules.add(scenarioResponsibility.getModule());
			}
		}
		
		modulesStatistics = new ArrayList<ModuleStatistics>();

		int scenarioExternalDependeciesCount = 0;
		for (Module module : scenarioModules) {
			ModuleStatistics moduleStats = new ModuleStatistics(module);
			scenarioExternalDependeciesCount += moduleStats.getModuleExternalDependeciesCount();
			modulesStatistics.add(moduleStats);
		}
		
		scenarioCoupling = 0;
		scenarioAvgComplexity = 0;
		for (ModuleStatistics module : modulesStatistics) {
			module.calculateModuleCoupling(scenarioExternalDependeciesCount);
			scenarioCoupling += module.getModuleCoupling();
			scenarioAvgComplexity += module.getModuleAverageComplexity();
		}
		
		if (modulesStatistics.size() > 0) 
			scenarioAvgComplexity = scenarioAvgComplexity / modulesStatistics.size();
			
	}
	*/
	
	private void initModulesStatistics2() {

		this.modulesStatistics = new ArrayList<ModuleStatistics>();
		
		List<Responsibility> scenarioResponsibilities = new ArrayList<Responsibility>(this.scenario.getResponsibilities());
		List<Module> scenarioModules = new ArrayList<Module>();
		int scenarioExternalDependeciesCount = 0;
		for(Responsibility scenarioResponsibility : scenarioResponsibilities) {
			if (!scenarioModules.contains(scenarioResponsibility.getModule())) {
				Module module = scenarioResponsibility.getModule();
				scenarioModules.add(module);
				ModuleStatistics moduleStats = new ModuleStatistics(module);
				modulesStatistics.add(moduleStats);
				scenarioExternalDependeciesCount += moduleStats.getModuleExternalDependeciesCount();
			}
		}
		
		scenarioModifiabilityCost = 0;
		for (ModuleStatistics module : modulesStatistics) {
			scenarioModifiabilityCost += module.getModuleModifiabilityCost();
		}
		
		scenarioCoupling = 0;
		scenarioAvgComplexity = 0;
		for (ModuleStatistics module : modulesStatistics) {
			module.calculateModuleCoupling(scenarioExternalDependeciesCount);
			scenarioCoupling += module.getModuleCoupling();
			scenarioAvgComplexity += module.getModuleAverageComplexity();
		}
		
		if (modulesStatistics.size() > 0) 
			scenarioAvgComplexity = scenarioAvgComplexity / modulesStatistics.size();
			
	}
	
	private void calculateScenarioModificabilityCost() {
		
		for (Responsibility responsibility : this.scenario.getResponsibilities()) {
			ResponsibilityStatistics responsibilityStatistics = new ResponsibilityStatistics(responsibility);
			scenarioModifiabilityCost += responsibilityStatistics.getModifiabilityCost();
		}
	}
	
	public ModuleStatistics getGreaterCouplingModule() {
		Collections.sort(modulesStatistics, new Comparator<ModuleStatistics>() {
			@Override
			public int compare(ModuleStatistics o1, ModuleStatistics o2) {
				return (new Double(o2.getModuleCoupling()).compareTo(new Double(o1.getModuleCoupling())));
			}
		});
		return modulesStatistics.get(0);
	}
	
	public List<ModuleStatistics> getModulesStatistics() {
		return modulesStatistics;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public Double getScenarioCoupling() {
		return scenarioCoupling;
	}

	public void setScenarioCoupling(Double scenarioCoupling) {
		this.scenarioCoupling = scenarioCoupling;
	}

	public Responsibility getGreaterComplexityResposibility() {
		List<Responsibility> scenarioResponsibilities = new ArrayList<Responsibility>(
				this.scenario.getResponsibilities());

		if (scenarioResponsibilities.isEmpty())
			return null;

		Responsibility greaterComplexityResponsibility = scenarioResponsibilities.get(0);
		for (int i = 1; i < scenarioResponsibilities.size(); i++)
			greaterComplexityResponsibility = getGreaterComplexityResposibility(
					greaterComplexityResponsibility, scenarioResponsibilities.get(i));

		return greaterComplexityResponsibility;
	}
	
	private Responsibility getGreaterComplexityResposibility(Responsibility r1, Responsibility r2) {
		if (r1.getComplexity() > r2.getComplexity())
			return r1;
		else if (r1.getComplexity() < r2.getComplexity())
			return r2;
		else {
			Double r1CouplingCost = ResponsibilityStatistics.getAllDependenciesCouplingCost(r1);
			Double r2CouplingCost = ResponsibilityStatistics.getAllDependenciesCouplingCost(r2);
			if (r1CouplingCost > r2CouplingCost)
				return r1;
			else if (r1CouplingCost < r2CouplingCost)
				return r2;
			else
				return r1; // Equal complexity and coupling, just return first
		}
			
	}

	public void setScenarioAvgComplexity(double scenarioComplexity) {
		this.scenarioAvgComplexity = scenarioComplexity;
	}
	
	public Double getScenarioAvgComplexity() {
		return this.scenarioAvgComplexity;
	}
	
	public Double getScenarioModifiabilityCost() {
		return this.scenarioModifiabilityCost;
	}
	
}
