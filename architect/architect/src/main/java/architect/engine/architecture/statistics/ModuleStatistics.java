package architect.engine.architecture.statistics;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import architect.model.Dependency;
import architect.model.Module;
import architect.model.Responsibility;

public class ModuleStatistics {

	private Module module;
	private double moduleAverageComplexity;
	private double moduleCoupling;
	private double moduleExternalDependenciesSum;
	private int moduleExternalDependeciesCount;
	private List<Dependency> moduleOuterDependencies;
	private Hashtable<Responsibility, List<Responsibility>> hashOuterDependencies=null;
	private List<Module> neighborModules = null;
	private double initialModuleCost;
	
	public ModuleStatistics(Module module) {
		this.module = module;
		this.moduleExternalDependenciesSum = 0;
		this.moduleExternalDependeciesCount = 0;
		this.moduleAverageComplexity = 0;
		
		List<Responsibility> innerResponsibilities = new ArrayList<Responsibility>(this.module.getResponsibilities());
		for (Responsibility resp : innerResponsibilities) {
			this.moduleAverageComplexity += resp.getComplexity();
		}
		this.moduleAverageComplexity = 1 - (1/(1+this.moduleAverageComplexity));
		calculateOuterDependencies();
	}
	
	private void calculateOuterDependencies() {
		this.neighborModules = new ArrayList<Module>();
		this.moduleOuterDependencies = new ArrayList<Dependency>();
		for (Responsibility moduleResponsability : module.getResponsibilities()) {
			for (Dependency dependency : moduleResponsability
					.getDependenciesAsChild()) {
				if (dependency.getParentResponsibility() != null
						&& dependency.getParentResponsibility().getModule() != null
						&& !dependency.getParentResponsibility().getModule()
								.equals(this.module)) {
					moduleOuterDependencies.add(dependency);
					if (!this.neighborModules.contains(dependency.getParentResponsibility().getModule()))
						this.neighborModules.add(dependency.getParentResponsibility().getModule());
				}
			}
			for (Dependency dependency : moduleResponsability
					.getDependenciesAsParent()) {
				if (dependency.getChildResponsibility() != null
						&& dependency.getChildResponsibility().getModule() != null
						&& !dependency.getChildResponsibility().getModule()
								.equals(this.module)) {
					moduleOuterDependencies.add(dependency);
					if (!this.neighborModules.contains(dependency.getChildResponsibility().getModule()))
						this.neighborModules.add(dependency.getChildResponsibility().getModule());
				}
			}
		}
		for (Dependency relationship : moduleOuterDependencies) {
			if (relationship.getCouplingcost() > 0) {
				moduleExternalDependenciesSum += relationship.getCouplingcost();
				moduleExternalDependeciesCount ++;
			}
		}
	}
	
	public void calculateModuleCoupling(int scenarioExternalDependeciesCount) {
		double moduleDependenciesAvg = 0;
		
		if (moduleExternalDependeciesCount > 0)
			moduleDependenciesAvg = moduleExternalDependenciesSum / moduleExternalDependeciesCount;

		double moduleCallsVsTotalCalls = 0;
		if (scenarioExternalDependeciesCount > 0)
			moduleCallsVsTotalCalls = (double) moduleExternalDependeciesCount / (double) scenarioExternalDependeciesCount;

		if (moduleDependenciesAvg == 0 || moduleCallsVsTotalCalls == 0)
			this.moduleCoupling = 0;
		else
			this.moduleCoupling = (moduleDependenciesAvg + moduleCallsVsTotalCalls) / 2;
	}
	
	public Hashtable<Responsibility, List<Responsibility>> getHashOuterDependencies() {
		return hashOuterDependencies;
	}

	public List<Dependency> getOuterDependencies() {
		return moduleOuterDependencies;
	}
		
	public double getModuleCoupling() {
		return moduleCoupling;
	}

	public double getModuleExternalDependenciesSum() {
		return moduleExternalDependenciesSum;
	}

	public void setModuleExternalDependenciesSum(double moduleExternalDependenciesSum) {
		this.moduleExternalDependenciesSum = moduleExternalDependenciesSum;
	}

	public int getModuleExternalDependeciesCount() {
		return moduleExternalDependeciesCount;
	}

	public void setModuleExternalDependeciesCount(int moduleExternalDependeciesCount) {
		this.moduleExternalDependeciesCount = moduleExternalDependeciesCount;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public double getModuleAverageComplexity() {
		return moduleAverageComplexity;
	}

	public void setModuleAverageComplexity(double moduleAverageComplexity) {
		this.moduleAverageComplexity = moduleAverageComplexity;
	}

	/**
	 * Principal Modifiability Calculation
	 * @return
	 */
	public double getModuleModifiabilityCost() {
		int totalModuleResposibilitesCount = this.module.getResponsibilities().size();
		double x = (double) totalModuleResposibilitesCount / this.module.getArchitecture().getResponsibilities().size();
		double y = (1 - x) * 0.5;
		double z = (1 - x) * 0.5;
		
		
		double internalResponsibilitiesCost = getModuleResponsibilitiesCost() / totalModuleResposibilitesCount;
		
		double neighborModulesCost = 0;
		for (Module module : this.neighborModules) {
			ModuleStatistics modStats = new ModuleStatistics(module);
			neighborModulesCost += modStats.getModuleDependenciesSumWith(this.module);// * modStats.getModuleResponsibilitiesCost();
		}
		
		double modulesDependenciesCost = neighborModulesCost / this.neighborModules.size();
		
		return (x * this.initialModuleCost) + (x * internalResponsibilitiesCost) + (z * modulesDependenciesCost);
	}
	
	public double getModuleResponsibilitiesCost() {
		double moduleResponsibilitiesCost = 0;
		for (Responsibility responsibility : this.module.getResponsibilities()) {
			moduleResponsibilitiesCost += responsibility.getComplexity();
		}
		return moduleResponsibilitiesCost;
	}
	
	public double getModuleDependenciesSumWith(Module m) {
		double dependenciesSum = 0;
		for (Responsibility moduleResponsability : module.getResponsibilities()) {
			for (Dependency dependency : moduleResponsability
					.getDependenciesAsChild()) {
				if (dependency.getParentResponsibility() != null
						&& dependency.getParentResponsibility().getModule() != null
						&& dependency.getParentResponsibility().getModule()
								.equals(m)) {
					dependenciesSum += dependency.getCouplingcost();
				}
			}
			for (Dependency dependency : moduleResponsability
					.getDependenciesAsParent()) {
				if (dependency.getChildResponsibility() != null
						&& dependency.getChildResponsibility().getModule() != null
						&& dependency.getChildResponsibility().getModule()
								.equals(m)) {
					dependenciesSum += dependency.getCouplingcost();
				}
			}
		}
		
		return dependenciesSum;
	}

}
