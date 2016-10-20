package architect.engine.mast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import architect.model.Architecture;
import architect.model.Dependency;
import architect.model.Responsibility;
import architect.model.Scenario;

public class MastInputFile extends Architecture {

	private static final long serialVersionUID = 1L;
	public static Long FILE_COUNT = 0L;
	public static String MAST_INPUT_FILE_BASE = "architecture_#ID_input_#COUNT.txt";
	private static String newLine = System.getProperty("line.separator");
	
	private Architecture architecture;
	private Scenario scenarioToAnalyze;
	private Set<Scenario> allPerformanceScenarios = new LinkedHashSet<Scenario>();
	private Set<Scenario> relativePerformanceScenarios = new LinkedHashSet<Scenario>();
	private HashMap<Scenario,Set<Responsibility>> allResponsibilitiesScenario = new HashMap<Scenario,Set<Responsibility>>();
	private Set<Responsibility> sharedResponsibilities = new LinkedHashSet<Responsibility>();
	private Set<Responsibility> simpleResponsibilities = new LinkedHashSet<Responsibility>();

	private String fileName = "";
	
	public MastInputFile (Scenario scenario){
		this.architecture = scenario.getArchitecture();
		this.scenarioToAnalyze = scenario;
		FILE_COUNT++;
		this.fileName = MastSimConnector.MAST_SIM_ARCHITECT_TEMP + MAST_INPUT_FILE_BASE.replace("#ID", scenarioToAnalyze.getArchitecture().getId().toString()).replace("#COUNT", FILE_COUNT.toString());
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public void deleteFile(){
		 File file = new File(getFileName());
		 if (file.exists() && file.canWrite()) {
			 file.delete();
		 }
	}
	
	public void generateAndSave() {
		this.allPerformanceScenarios = architecture.getPerformanceScenarios();
		initAllResponsabilities();
		initResponsibilities();
		this.relativePerformanceScenarios = this.scenarioToAnalyze.getSharedReponsibilitiesScenarios(allPerformanceScenarios);
		//this.relativePerformanceScenarios = this.allPerformanceScenarios;
		
		PrintWriter file = null;
		try {
			file = new PrintWriter(getFileName());
			file.write(getModel() + newLine);
			file.write(getProcessingResources() + newLine);
			file.write(getSchedulingServers() + newLine);
			file.write(getResources() + newLine);
			file.write(getOperations() + newLine);
			file.write(getTransactions() + newLine);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (file != null)
				file.close();
		}
	}
	
	public String getModel() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String model = "";
		model += "Model (" + newLine;
		model += " Model_Name => architecture_" + this.architecture.getId() + "," + newLine;
		model += " Model_Date => " + sdf.format(new Date()) + ");" + newLine;
		return model;
	}
	
	public String getProcessingResources() {
		String prs = "";
		
//		for (Scenario scenario : relativePerformanceScenarios) {
			String pr = "";
			pr += "Processing_Resource (" + newLine;
			pr += " Type => Fixed_Priority_Processor," + newLine;
			pr += " Name => CPU_1" + "," + newLine; //+ scenario.getId() + "," + newLine;
			pr += " Worst_Context_Switch => 0.25);" + newLine;
			prs += pr + newLine;
//		}
		
		return prs;
	}
	
	public String getSchedulingServers() {
		String servers = "";
		
		for (Scenario scenario : relativePerformanceScenarios) {
			String server = "";
			server += "Scheduling_Server (" + newLine;
			server += " Type => Fixed_Priority," + newLine;
			server += " Name => Server_" + scenario.getName() + "," + newLine;
			server += " Server_Sched_Parameters => (" + newLine;
			server += "  Type => Fixed_Priority_Policy," + newLine;
			server += "  The_Priority => " + (10-scenario.getPriority()) + ")," + newLine;
			server += " Server_Processing_Resource => CPU_1" + ");" + newLine; //+ scenario.getId() + ");" + newLine;
			servers += server + newLine;
		}
		
//		String server = "";
//		server += "Scheduling_Server (" + newLine;
//		server += " Type => Fixed_Priority," + newLine;
//		server += " Name => Server_background," + newLine;
//		server += " Server_Sched_Parameters => (" + newLine;
//		server += "  Type => Fixed_Priority_policy," + newLine;
//		server += "  The_Priority => 1)," + newLine;
//		server += " Server_Processing_Resource => CPU_1" + ");" + newLine; //+ scenario.getId() + ");" + newLine;
//		servers += server + newLine;
			
		return servers;
	}
	
	public String getResources() {
		String resources = "";
		
		for (Responsibility responsibility : this.sharedResponsibilities) {
			String sharedResourceId = (String) (responsibility.getId() > 0 ? responsibility.getName() : "I" + responsibility.getId() * (-1));
			String resource = "";
			resource += "Shared_Resource (" + newLine;
			resource += " Type => Immediate_Ceiling_Resource," + newLine;
			resource += " Name => Shared_Resource_" + sharedResourceId  + ");" + newLine;
			resources += resource + newLine;
		}
		
		return resources;
	}
	
	public String getOperations() {
		String operations = "";
		
		// se agrega una operacion simple por cada repeticion de un recurso compartido sobre un escenario
		for (Scenario scenario : relativePerformanceScenarios) {
			for (Responsibility responsibility : this.sharedResponsibilities) {
				if (scenario.getResponsibilities().contains(responsibility)){
					String sharedResourceId = (String) (responsibility.getId() > 0 ? responsibility.getName() : "I" + responsibility.getId() * (-1));
					String operation = "";
					operation += "Operation (" + newLine;
					operation += " Type => Simple," + newLine;
					operation += " Name => Shared_Operation_" + scenario.getName() + sharedResourceId  + "," + newLine;
					operation += " Worst_Case_Execution_Time => " + (responsibility.getExecutionTime()*0.75) + "," + newLine;
					operation += " Shared_Resources_List => (Shared_Resource_" + sharedResourceId + "));" + newLine;
					operations += operation + newLine;
				}
			}
		}
		
		// se agrega una operacion de tipo "Enclosing" por cada escenario de performance que contenga al menos un recurso compartido. Si el escenario no contiene recursos compartidos, su tipo sera "Simple"
		for (Scenario scenario : relativePerformanceScenarios) {
			String sharedOperationId = scenario.getName();
			String operation = "";
			String compositeOperations = "";
			for (Responsibility responsibility : scenario.getResponsibilities()){
				if (this.sharedResponsibilities.contains(responsibility)){
					String sharedResourceId = (String) (responsibility.getId() > 0 ? "" + responsibility.getName() : "I" + responsibility.getId() * (-1));
					if (compositeOperations != ""){
						compositeOperations = compositeOperations + ",";
					}
					compositeOperations = compositeOperations + "Shared_Operation_" + scenario.getName() + sharedResourceId;
				}
			}			
			int executionTime = 0;
			for (Responsibility responsibility : this.getResponsibilitiesWithDependencies(scenario)){
				executionTime+= responsibility.getExecutionTime();				
			}
			
			operation += "Operation (" + newLine;
			if (compositeOperations == ""){
				operation += " Type => Simple," + newLine;
			}
			else {
				operation += " Type => Enclosing," + newLine;
			}
			operation += " Name => Operation_" + sharedOperationId + "," + newLine;
			operation += " Worst_Case_Execution_Time => " + (executionTime);
			if (compositeOperations != ""){
				operation += "," + newLine + "Composite_Operation_List => (" + compositeOperations + ")";
			}
			operation += ");" + newLine;
			operations += operation + "\n";
		}
		
		// Se crea una operacion simple que reune a todas las responsabilidades no asociadas a escenarios de performance
//		int executionBackground = 0;
//		Set<Responsibility> performanceResponsibilities= getResponsibilitiesPerformance();
//		for (Responsibility responsibility : architecture.getResponsibilities()) {
//			if(!performanceResponsibilities.contains(responsibility)){
//				executionBackground += responsibility.getExecutionTime();					
//			}
//		}
//		String operation = "";
//		operation += "Operation (" + newLine;
//		operation += " Type => Simple," + newLine;
//		operation += " Name => Operation_background," + newLine;
//		operation += " Worst_Case_Execution_Time => " + executionBackground + ");" + newLine;
//		operations += operation + newLine;
//		
		// TODO generar una operacion simple con todas aquellas responsabilidades que no se correspondan con escenarios de performance
		/*for (Responsibility responsibility : simpleResponsibilities) {
			String sharedOperationId = (String) (responsibility.getId() > 0 ? "" + responsibility.getId() : "I" + responsibility.getId() * (-1));
			String operation = "";
			operation += "Operation (" + newLine;
			operation += " Type => Simple," + newLine;
			operation += " Name => Operation_" + sharedOperationId + "," + newLine;
			operation += " Worst_Case_Execution_Time => " + responsibility.getExecutionTime() + ");" + newLine;
			operations += operation + newLine;
		}*/
		
		return operations;
	}
	
	public String getTransactions() {
		String transactions = "";
		int externalEvent = 0;
		
		// One Transaction per Scenario
		for (Scenario scenario : relativePerformanceScenarios) {
			//for (List<Dependency> dependencies : this.getDependenciesGraphs(scenario)) {
			externalEvent++;
			String externalEventName = "External_Event_" + externalEvent;
			String transactionName = "Transaction_" + scenario.getName() + "_" + externalEvent;
			
			String transaction = "";
			transaction += "Transaction (" + newLine;
			transaction += " Type => Regular," + newLine;
			transaction += " Name => " + transactionName + "," + newLine;
			transaction += " External_Events => (" + newLine;
			transaction += "  (Type => Periodic," + newLine;
			transaction += "  Name => E" + externalEvent + "," + newLine;
			transaction += "  Period => " + (scenario.getPeriod()*2) + "))," + newLine;
			
			transaction += " Internal_Events => (" + newLine;
			//transaction += getInternalEvents(externalEventName, dependencies, scenario);
			transaction += "  (Type => regular," + newLine;
			transaction += "   name => O" + externalEvent + "," + newLine;
			
			transaction += "   Timing_Requirements => (" + newLine;
			transaction += "    Type => Hard_Global_Deadline," + newLine;
			transaction += "    Deadline => " + (scenario.getMeasure()*3) + "," + newLine;
			transaction += "    referenced_event => E" + externalEvent + ")))," + newLine;
						
			transaction += " Event_Handlers => (" + newLine;
			transaction += "   (Type => Activity," + newLine;
			transaction += "   Input_Event => E" + externalEvent + "," + newLine;
			transaction += "   Output_Event => O" + externalEvent + "," + newLine;
			transaction += "   Activity_Operation => Operation_" + scenario.getName() + "," + newLine;
			transaction += "   Activity_Server=> Server_" + scenario.getName() + ")));" + newLine;
			//transaction += getEventHandlers(externalEventName, dependencies, scenario);
			
			transactions += transaction + newLine;
			//}
		}
		
		
		// una transaccion mas asociada la tarea background
		
		//for (List<Dependency> dependencies : this.getDependenciesGraphs(scenario)) {
//		externalEvent++;
//		String externalEventName = "External_Event_" + externalEvent;
//		String transactionName = "Transaction_background_" + externalEvent;
//		
//		String transaction = "";
//		transaction += "Transaction (" + newLine;
//		transaction += " Type => Regular," + newLine;
//		transaction += " Name => " + transactionName + "," + newLine;
//		transaction += " External_Events => (" + newLine;
//		transaction += "  (Type => Periodic," + newLine;
//		transaction += "  Name => E" + externalEvent + "," + newLine;
//		transaction += "  Period => 500 ))," + newLine;
//		
//		transaction += " Internal_Events => (" + newLine;
//		//transaction += getInternalEvents(externalEventName, dependencies, scenario);
//		transaction += "  (Type => regular," + newLine;
//		transaction += "   name => O" + externalEvent + "," + newLine;
//		
//		transaction += "   Timing_Requirements => (" + newLine;
//		transaction += "    Type => Soft_Global_Deadline," + newLine;
//		transaction += "    Deadline =>  500," + newLine;
//		transaction += "    referenced_event => E" + externalEvent + ")))," + newLine;
//					
//		transaction += " Event_Handlers => (" + newLine;
//		transaction += "   (Type => Activity," + newLine;
//		transaction += "   Input_Event => E" + externalEvent + "," + newLine;
//		transaction += "   Output_Event => O" + externalEvent + "," + newLine;
//		transaction += "   Activity_Operation => Operation_background," + newLine;
//		transaction += "   Activity_Server=> Server_background)));" + newLine;
//		//transaction += getEventHandlers(externalEventName, dependencies, scenario);
//		
//		transactions += transaction + newLine;
		
		
		
	
		return transactions;
	}
	
	private String getInternalEvents(String externalEventName, List<Dependency> dependencies, Scenario scenario) {
		String events = "";
		List<Responsibility> addedResponsibilities = new ArrayList<Responsibility>();
		
		for(Dependency dependency : dependencies) {
			
			Responsibility parent = dependency.getParentResponsibility();
			String parentId = (String) (parent.getId() > 0 ? "" + parent.getId() : "I" + parent.getId() * (-1));
			if (!addedResponsibilities.contains(parent)) {
				addedResponsibilities.add(parent);
				
				events += "  (Type => regular," + newLine;
				events += "   name => O" + parentId + ")," + newLine;
			}
			
			Responsibility child = dependency.getChildResponsibility();
			String childId = (String) (child.getId() > 0 ? "" + child.getId() : "I" + child.getId() * (-1));
			if (!addedResponsibilities.contains(child)) {
				addedResponsibilities.add(child);
				
				if (!dependency.equals(dependencies.get(dependencies.size()-1))) {
					events += "  (Type => regular," + newLine;
					events += "   name => O" + childId + ")," + newLine;
				} else {
					events += "  (Type => regular," + newLine;
					events += "   name => O" + childId + "," + newLine;
					events += "   Timing_Requirements => (" + newLine;
					events += "    Type => Hard_Global_Deadline," + newLine;
					events += "    Deadline => " + scenario.getMeasure() + "," + newLine;
					events += "    referenced_event => " + externalEventName + "))," + newLine;
				}
			}
		}
		
		if (events.length()>0) {
			events = events.substring(0, events.length()-3);
		}
		return events;
	}
	
	private String getEventHandlers(String externalEventName, List<Dependency> dependencies, Scenario scenario) {
		String handlers = "";
		
		if (dependencies.size()>0) {
			Dependency firstDep = dependencies.get(0);
			String outputEventId = (String) (firstDep.getParentResponsibility().getId() > 0 ? "" + firstDep.getParentResponsibility().getId() : "I" + firstDep.getParentResponsibility().getId() * (-1));
			handlers += "  (Type => Activity," + newLine;
			handlers += "   Input_Event => " + externalEventName + "," + newLine;
			handlers += "   Output_Event => O" + outputEventId + "," + newLine;
			handlers += "   Activity_Operation => Operation_" + scenario.getName() + "," + newLine;
			handlers += "   Activity_Server=> Server_" + scenario.getName() + ")," + newLine;
		}
		
		for(Dependency dependency : dependencies) {
			String inputEventId = (String) (dependency.getParentResponsibility().getId() > 0 ? "" + dependency.getParentResponsibility().getId() : "I" + dependency.getParentResponsibility().getId() * (-1));
			String outputEventId = (String) (dependency.getChildResponsibility().getId() > 0 ? "" + dependency.getChildResponsibility().getId() : "I" + dependency.getChildResponsibility().getId() * (-1));
			handlers += "  (Type => Activity," + newLine;
			handlers += "   Input_Event => O" + inputEventId + "," + newLine;
			handlers += "   Output_Event => O" + outputEventId + "," + newLine;
			handlers += "   Activity_Operation => Operation_" + scenario.getName() + "," + newLine;
			handlers += "   Activity_Server=> Server_" + scenario.getName() + ")," + newLine;
		}
		
		if(handlers.length()>0) {
			handlers = handlers.substring(0, handlers.length()-3);
		}
		return handlers;
	}
	
	private void initAllResponsabilities() {
		for (Scenario scenario : allPerformanceScenarios) {
			//Set<Responsibility>  allResp = getAllResponsabilitiesFrom(scenario);
			Set<Responsibility>  allResp = scenario.getResponsibilities();
			this.allResponsibilitiesScenario.put(scenario, allResp);
		}
	}

	private void initResponsibilities() {
		
		List<Responsibility> allResponsibilities = new ArrayList<Responsibility>();
		for (Map.Entry<Scenario,Set<Responsibility>> entry : this.allResponsibilitiesScenario.entrySet()) {
			allResponsibilities.addAll(entry.getValue());
		}
		for (Responsibility responsibility : allResponsibilities){
			int occurrences = Collections.frequency(allResponsibilities, responsibility);
			if (occurrences > 1) {
				this.sharedResponsibilities.add(responsibility);
			} else if (occurrences == 1){
				this.simpleResponsibilities.add(responsibility);
			}
		}
	}	
	
	private Set<Responsibility> getAllResponsabilitiesFrom(Scenario scenario) {
		Stack<Responsibility> toVisitResponsibilities = new Stack<Responsibility>();
		Set<Responsibility> visitedResponsibilities = new LinkedHashSet<Responsibility>();
		
		toVisitResponsibilities.addAll(scenario.getResponsibilities());
		
		while (!toVisitResponsibilities.isEmpty()) {
			Responsibility responsibility = toVisitResponsibilities.pop();
			if (!visitedResponsibilities.contains(responsibility)) {
				visitedResponsibilities.add(responsibility);
				for (Dependency dep : responsibility.getDependenciesAsParent()) {
					if (!dep.getChildResponsibility().equals(responsibility) && !toVisitResponsibilities.contains(dep.getChildResponsibility())) {
						toVisitResponsibilities.push(dep.getChildResponsibility());
					}	
				}
			}
		}
		
		return visitedResponsibilities;
	}
	
	private List<List<Dependency>> getDependenciesGraphs(Scenario scenario) {
//		Set<Responsibility> allResponsibilities = this.allResponsibilitiesScenario.get(scenario);
//		List<Responsibility> startResponsibilities = getStartResponsibilities(allResponsibilities);
//		List<Responsibility> endResponsibilities = getEndResponsibilities(allResponsibilities);
		Set<Responsibility> startResponsibilities = scenario.getResponsibilities();
		Set<Responsibility> endResponsibilities = scenario.getResponsibilities();
		return getPaths(startResponsibilities, endResponsibilities);
	}

//	private List<Responsibility> getStartResponsibilities(Set<Responsibility> allResponsibilities) {
//		List<Responsibility> result = new ArrayList<Responsibility>();
//		for (Responsibility responsibility : allResponsibilities) {
//			boolean notContainsChilds = true;
//			for (Dependency dependency : responsibility.getDependenciesAsParent()) {
//				if (allResponsibilities.contains(dependency.getChildResponsibility())) {
//					notContainsChilds = false;
//					break;
//				}
//			}
//			if (notContainsChilds)
//				result.add(responsibility);
//		}
//		return result;
//	}
	
//	private List<Responsibility> getEndResponsibilities(Set<Responsibility> allResponsibilities) {
//		List<Responsibility> result = new ArrayList<Responsibility>();
//		for (Responsibility responsibility : allResponsibilities) {
//			boolean notContainsParents = true;
//			for (Dependency dependency : responsibility.getDependenciesAsChild()) {
//				if (allResponsibilities.contains(dependency.getParentResponsibility())) {
//					notContainsParents = false;
//					break;
//				}
//			}
//			if (notContainsParents)
//				result.add(responsibility);
//		}
//		return result;
//	}
	
	private List<List<Dependency>> getPaths(Set<Responsibility> startResponsibilities, Set<Responsibility> endResponsibilities) {
		List<List<Dependency>> paths = new ArrayList<List<Dependency>>();
		for (Responsibility startResponsibility : startResponsibilities) {
//			if (!endResponsibilities.contains(startResponsibility)) {
				paths.addAll(getPaths(startResponsibility, endResponsibilities));
//			}
		}
		return paths;
	}
	
	private List<List<Dependency>> getPaths(Responsibility startResponsibility, Set<Responsibility> endResponsibilities) {
		List<List<Dependency>> paths = new ArrayList<List<Dependency>>();
		for (Responsibility endResponsibility : endResponsibilities) {			
			List<Dependency> newPath = getPath(new ArrayList<Responsibility>(),startResponsibility, endResponsibility);
			if (newPath!=null)
				paths.add(newPath);
		}
		return paths;
	}
	
	private List<Dependency> getPath(List<Responsibility> visited, Responsibility startResponsibility, Responsibility endResponsibility) {
		if (startResponsibility.equals(endResponsibility)) {
			return null;
		}
		if (visited.contains(endResponsibility)) {
			return null;
		}
		if (visited.contains(startResponsibility)) {
			return null;
		} else {
			visited.add(startResponsibility);
		}
		for (Dependency dependency : startResponsibility.getDependenciesAsParent()) {
			if (dependency.getChildResponsibility().equals(endResponsibility)) {
				List<Dependency> newList = new ArrayList<Dependency>();
				newList.add(dependency);
				return newList;
			} else {
				List<Dependency> newList = getPath(visited, dependency.getChildResponsibility(), endResponsibility);
				if (newList!=null) {
					List<Dependency> toReturn = new ArrayList<Dependency>();
					toReturn.add(dependency);
					toReturn.addAll(newList);
					return toReturn;
				}
			}
		}
		return null;
	}
	
	// OPERACIONES DE CONJUNTOS

	public static <T> Set<T> diferenciaGen(Set<T> a, Set<T> b) {
        Set<T> res = new LinkedHashSet<T>(a);
        res.removeAll(b);
        return res;
    }
	
	public static <T> Set<T> diferenciaSimetricaGen(Set<T> a, Set<T> b) {
        Set<T> res = diferenciaGen(a,b);
        Set<T> bmenosa = diferenciaGen(b,a);
        res.addAll(bmenosa);
        return res;
    }
	
	public static <T> Set<T> interseccionGen(Set<T> a, Set<T> b) {
        Set<T> res = new LinkedHashSet<T>(a);
        res.retainAll(b);
        return res;
    }
	
	private Set<Responsibility> getResponsibilitiesPerformance(){
		Set<Responsibility> responsibilities= new LinkedHashSet<Responsibility>();
		for (Map.Entry<Scenario,Set<Responsibility>> entry : this.allResponsibilitiesScenario.entrySet()) {
			responsibilities.addAll(entry.getValue());
		}
		return responsibilities;
	}
	
	private Set<Responsibility> getResponsibilitiesWithDependencies(Scenario s){
		Set<Responsibility> responsibilities= new LinkedHashSet<Responsibility>();
		responsibilities.addAll(s.getResponsibilities());
		for (Responsibility res : s.getResponsibilities()) {
			for (Dependency dep : res.getDependenciesAsParent()){
				responsibilities.add(dep.getChildResponsibility());
			}
		}
		return responsibilities;
	}
	
}
