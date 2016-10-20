package architect.engine.architecture;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import architect.engine.DesignBotExecutor;
import architect.engine.architecture.statistics.ScenarioStatistics;
import architect.engine.attribute.modifiability.ModifiabilityDbot;
import architect.model.Architecture;
import architect.model.ArchitectureAnalysis;
import architect.model.Dependency;
import architect.model.DependencyId;
import architect.model.DesignBot;
import architect.model.Module;
import architect.model.QualityAttribute;
import architect.model.Responsibility;
import architect.model.Scenario;
import architect.model.Tactic;

public class ArchitectureUtils {
	
	ArchitectureUtils() {
	}
	
	public static void store(Architecture architecture, String fileName){
		try {
			JAXBContext context = JAXBContext.newInstance(Architecture.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			marshaller.marshal(architecture, System.out);
			marshaller.marshal(architecture, new File(fileName));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static Architecture read(String fileName) {
		try {
			JAXBContext context = JAXBContext.newInstance(Architecture.class);
			Unmarshaller um = context.createUnmarshaller();
			Architecture architecture = (Architecture) um.unmarshal(new FileReader(fileName));
			completeArchitecture(architecture);
			return architecture;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static void completeArchitecture(Architecture architecture) {
		List<Responsibility> responsibilities = new ArrayList<Responsibility>(architecture.getResponsibilities());
		List<Scenario> scenarios = new ArrayList<Scenario>(architecture.getScenarios());
		List<Module> modules = new ArrayList<Module>(architecture.getModules());
		
		for (Dependency dependency : architecture.getDependencies()) {
			if (dependency.getChildResponsibility()!=null) {
				Responsibility child = responsibilities.get(responsibilities.indexOf(dependency.getChildResponsibility()));
				if(!child.getDependenciesAsChild().contains(dependency))
					child.getDependenciesAsChild().add(dependency);
			}
			if (dependency.getParentResponsibility()!=null) {
				Responsibility parent = responsibilities.get(responsibilities.indexOf(dependency.getParentResponsibility()));
				if(!parent.getDependenciesAsParent().contains(dependency))
					parent.getDependenciesAsParent().add(dependency);
			}
		}
		
		for (Responsibility responsibility : responsibilities) {
			for (Scenario scenarioR : responsibility.getScenarios()) {
				Scenario scenario = scenarios.get(scenarios.indexOf(scenarioR));
				if (!scenario.getResponsibilities().contains(responsibility))
					scenario.getResponsibilities().add(responsibility);
				responsibility.getScenarios().remove(scenarioR);
				responsibility.getScenarios().add(scenario);
			}
			if (responsibility.getModule()!=null) {
				Module module = modules.get(modules.indexOf(responsibility.getModule()));
				if (!module.getResponsibilities().contains(responsibility))
					module.getResponsibilities().add(responsibility);
				responsibility.setModule(module);
			}
			for (Dependency dependency : responsibility.getDependenciesAsChild()) {
				if (dependency.getId().getChild()!=null && dependency.getChildResponsibility()==null) {
					dependency.setChildResponsibility(getResponsibility(dependency.getId().getChild(),responsibilities));
				}
				if (dependency.getId().getParent()!=null && dependency.getParentResponsibility()==null) {
					dependency.setParentResponsibility(getResponsibility(dependency.getId().getParent(),responsibilities));
				}
			}
			for (Dependency dependency : responsibility.getDependenciesAsParent()) {
				if (dependency.getId().getChild()!=null && dependency.getChildResponsibility()==null) {
					dependency.setChildResponsibility(getResponsibility(dependency.getId().getChild(),responsibilities));
				}
				if (dependency.getId().getParent()!=null && dependency.getParentResponsibility()==null) {
					dependency.setParentResponsibility(getResponsibility(dependency.getId().getParent(),responsibilities));
				}
			}
		}
		
	}
	
	private static Responsibility getResponsibility(Long idResponsibility, List<Responsibility> responsibilities){
		for (Responsibility responsibility : responsibilities) {
			if (responsibility.getId().equals(idResponsibility))
				return responsibility;
		}
		return null;
	}
	
	public static void main(String args[]) {
		System.out.println("Start");
		
		//Modules
		List<Module> modules = new ArrayList<Module>();
		Module moduleA = new Module();
		moduleA.setId(new Long(0));
		Module moduleB = new Module();
		moduleB.setId(new Long(1));
		Module moduleC = new Module();
		moduleC.setId(new Long(2));
		modules.add(moduleA);
		modules.add(moduleB);
		modules.add(moduleC);
		
		//Responsibilities
		Responsibility responsibility1 = new Responsibility();
		responsibility1.setId(new Long(1));
		responsibility1.setComplexity(new Double(0.5));
		responsibility1.setExecutionTime(new Long(10));
		responsibility1.setModule(moduleA);
		moduleA.getResponsibilities().add(responsibility1);
		Responsibility responsibility2 = new Responsibility();
		responsibility2.setId(new Long(2));
		responsibility2.setComplexity(new Double(0.5));
		responsibility2.setExecutionTime(new Long(10));
		responsibility2.setModule(moduleB);
		moduleB.getResponsibilities().add(responsibility2);
		Responsibility responsibility3 = new Responsibility();
		responsibility3.setId(new Long(3));
		responsibility3.setComplexity(new Double(0.5));
		responsibility3.setExecutionTime(new Long(10));
		responsibility3.setModule(moduleC);
		moduleC.getResponsibilities().add(responsibility3);
		Responsibility responsibility4 = new Responsibility();
		responsibility4.setId(new Long(4));
		responsibility4.setComplexity(new Double(0.5));
		responsibility4.setExecutionTime(new Long(10));
		responsibility4.setModule(moduleA);
		moduleA.getResponsibilities().add(responsibility4);

		
		//Scenarios
		List<Scenario> scenarios = new ArrayList<Scenario>();
		Scenario scenario1 = new Scenario();
		scenario1.setId(new Long(0));
		scenario1.getResponsibilities().add(responsibility1);
		scenario1.getResponsibilities().add(responsibility2);
		scenario1.getResponsibilities().add(responsibility3);
		scenario1.getResponsibilities().add(responsibility4);
		scenarios.add(scenario1);
		responsibility1.getScenarios().add(scenario1);
		responsibility2.getScenarios().add(scenario1);
		responsibility3.getScenarios().add(scenario1);
		responsibility4.getScenarios().add(scenario1);
		
		//Dependencies
		Dependency dependency1 = new Dependency();
		dependency1.setParentResponsibility(responsibility1);
		responsibility1.getDependenciesAsParent().add(dependency1);
		dependency1.setChildResponsibility(responsibility2);
		responsibility2.getDependenciesAsChild().add(dependency1);
		dependency1.setCouplingcost(0.9);
		dependency1.setId(new DependencyId(new Long(responsibility1.getId()),new Long(responsibility2.getId())));
		Dependency dependency2 = new Dependency();
		dependency2.setParentResponsibility(responsibility1);
		responsibility1.getDependenciesAsParent().add(dependency2);
		dependency2.setChildResponsibility(responsibility3);
		responsibility3.getDependenciesAsChild().add(dependency2);
		dependency2.setCouplingcost(0.1);
		dependency2.setId(new DependencyId(new Long(responsibility1.getId()), new Long(responsibility3.getId())));
		Dependency dependency3 = new Dependency();
		dependency3.setParentResponsibility(responsibility4);
		responsibility4.getDependenciesAsParent().add(dependency3);
		dependency3.setChildResponsibility(responsibility3);
		responsibility3.getDependenciesAsChild().add(dependency3);
		dependency3.setCouplingcost(0.1);
		dependency3.setId(new DependencyId(new Long(responsibility4.getId()), new Long(responsibility3.getId())));
		Dependency dependency4 = new Dependency();
		dependency4.setParentResponsibility(responsibility2);
		responsibility2.getDependenciesAsParent().add(dependency4);
		dependency4.setChildResponsibility(responsibility3);
		responsibility3.getDependenciesAsChild().add(dependency4);
		dependency4.setCouplingcost(0.9);
		dependency4.setId(new DependencyId(new Long(responsibility2.getId()), new Long(responsibility3.getId())));
		Dependency dependency5 = new Dependency();
		dependency5.setParentResponsibility(responsibility4);
		responsibility4.getDependenciesAsParent().add(dependency5);
		dependency5.setChildResponsibility(responsibility1);
		responsibility1.getDependenciesAsChild().add(dependency5);
		dependency5.setCouplingcost(0.9);
		dependency5.setId(new DependencyId(new Long(responsibility4.getId()), new Long(responsibility1.getId())));
		Dependency dependency6 = new Dependency();
		dependency6.setParentResponsibility(responsibility2);
		responsibility2.getDependenciesAsParent().add(dependency6);
		dependency6.setChildResponsibility(responsibility1);
		responsibility1.getDependenciesAsChild().add(dependency6);
		dependency6.setCouplingcost(0.9);
		dependency6.setId(new DependencyId(new Long(responsibility2.getId()), new Long(responsibility1.getId())));
		
		//Architecture
		Architecture architectureOriginal = new Architecture();
		architectureOriginal.setId(new Long(1));
		responsibility1.setArchitecture(architectureOriginal);
		responsibility2.setArchitecture(architectureOriginal);
		responsibility3.setArchitecture(architectureOriginal);
		responsibility4.setArchitecture(architectureOriginal);
		moduleA.setArchitecture(architectureOriginal);
		moduleB.setArchitecture(architectureOriginal);
		moduleC.setArchitecture(architectureOriginal);
		scenario1.setArchitecture(architectureOriginal);
		architectureOriginal.getResponsibilities().add(responsibility1);
		architectureOriginal.getResponsibilities().add(responsibility2);
		architectureOriginal.getResponsibilities().add(responsibility3);
		architectureOriginal.getResponsibilities().add(responsibility4);
		architectureOriginal.getModules().add(moduleA);
		architectureOriginal.getModules().add(moduleB);
		architectureOriginal.getModules().add(moduleC);
		architectureOriginal.getScenarios().add(scenario1);
		architectureOriginal.getDependencies().add(dependency1);
		architectureOriginal.getDependencies().add(dependency2);
		architectureOriginal.getDependencies().add(dependency3);
		architectureOriginal.getDependencies().add(dependency4);
		architectureOriginal.getDependencies().add(dependency5);
		architectureOriginal.getDependencies().add(dependency6);
		dependency1.setArchitecture(architectureOriginal);
		dependency2.setArchitecture(architectureOriginal);
		dependency3.setArchitecture(architectureOriginal);
		dependency4.setArchitecture(architectureOriginal);
		dependency5.setArchitecture(architectureOriginal);
		dependency6.setArchitecture(architectureOriginal);
		
		printArchitecture(architectureOriginal, "Original");
		
		//Modifiability Tactics
		QualityAttribute qualityAttribute = new QualityAttribute();
		qualityAttribute.setName("Modifiability");
		Tactic tactic1 = new Tactic();
		tactic1.setId(new Long(1));
		tactic1.setName("Insert Intermediary Responsibility");
		tactic1.setQualityAttribute(qualityAttribute);
		Tactic tactic2 = new Tactic();
		tactic2.setId(new Long(2));
		tactic2.setName("Split Responsibility");
		tactic2.setQualityAttribute(qualityAttribute);
		Tactic tactic3 = new Tactic();
		tactic3.setId(new Long(3));
		tactic3.setName("Abstract Common Responsibilities");
		tactic3.setQualityAttribute(qualityAttribute);
		scenario1.setQualityAttribute(qualityAttribute);

		ArchitectureUtils.store(architectureOriginal,"C:/archi.xml");
		Architecture architectureFromFile = ArchitectureUtils.read("C:/archi.xml");
		printArchitecture(architectureFromFile, "From File");
		
//		TacticExecutor tacticExecutor1 = tactic1.getTacticExecutor();
//		Architecture t1 = tacticExecutor1.backtracking(architectureOriginal, scenario1);
//		printArchitecture(t1, " Tactic 1 Insert Intermediary Responsibility");
			
//		TacticExecutor tacticExecutor1 = tactic1.getTacticExecutor();
//		TrackAlgorithm sa1 = new Predictive();
//		Architecture t1 = sa1.searchAndApply(tacticExecutor1, scenario1);
//		printArchitecture(t1, " Tactic 1 Insert Intermediary Responsibility");
		  
//		TacticExecutor tacticExecutor2 = tactic2.getTacticExecutor();
//		Architecture t2 = tacticExecutor2.predictive(architectureOriginal, scenario1);
//		printArchitecture(t2, " Tactic 2 Split Responsibility");

//		TacticExecutor tacticExecutor2 = tactic2.getTacticExecutor();
//		TrackAlgorithm sa2 = new Predictive();
//		Architecture t2 = sa2.searchAndApply(tacticExecutor2, scenario1);
//		printArchitecture(t2, " Tactic 2 Split Responsibility");
		
//		TacticExecutor tacticExecutor = tactic3.getTacticExecutor();
//		Architecture t3 = tacticExecutor.backtracking(architectureFromFile, scenario1);
//		printArchitecture(t3," Tactic 3 Abstract Common Responsibilities");
		
		//Dbots
		DesignBot designBot = new DesignBot();
		designBot.setId(new Long(1));
		designBot.setQualityAttribute(qualityAttribute);
		designBot.setArchitecture(architectureFromFile);
		designBot.getTactics().add(tactic1);
		designBot.getTactics().add(tactic2);
		
		ArchitectureAnalysis architectureAnalysis = new ArchitectureAnalysis();
		architectureAnalysis.setDesignBot(designBot);
		architectureAnalysis.setArchitecture(architectureFromFile);
		architectureAnalysis.setScenario(scenario1);
		DesignBotExecutor designBotExecutor = new ModifiabilityDbot(architectureAnalysis);
		designBotExecutor.start();
		
		System.out.println("End");
	}

	public static synchronized void printArchitecture(Architecture architecture, String name) {
		System.out.println("---- Architecture : " + name + " ----");
		
		ScenarioStatistics scenarioStats = new ScenarioStatistics(new ArrayList<Scenario>(architecture.getScenarios()).get(0));
		System.out.println("Coupling : " + scenarioStats.getScenarioCoupling() );
		System.out.println("Complexity : " + scenarioStats.getScenarioAvgComplexity() );
		
		for (Module moduleX : architecture.getModules()) {
			System.out.println("Module : " + moduleX.getId());
		}
		for (Responsibility responsibilityX : architecture.getResponsibilities()) {
			System.out.println("Responsibility : " + responsibilityX.getId());
		}
		for (Dependency dependencyX : architecture.getDependencies()) {
			System.out.print("Dependency " + dependencyX.getId() + " : ");
			if (dependencyX.getParentResponsibility()!=null)
				System.out.print(dependencyX.getParentResponsibility().getId() + " to ");
			if (dependencyX.getChildResponsibility()!=null)
				System.out.print(dependencyX.getChildResponsibility().getId() + " with ");
			System.out.println("coupling cost " + dependencyX.getCouplingcost());
		}
	}
	
}
