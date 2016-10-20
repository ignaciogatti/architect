package architect.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import architect.dao.ElementChangeDAO;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.error.GenericArchitectureException;
import architect.model.ArchitectureAnalysis;
import architect.model.Dependency;
import architect.model.DesignBot;
import architect.model.ElementChange;
import architect.model.Module;
import architect.model.Responsibility;
import architect.model.Scenario;
import architect.model.Tactic;

@Service("elementChangeService")
public class ElementChangeServiceImpl implements ElementChangeService {

	@Autowired
	private ElementChangeDAO elementChangeDAO;
	
	@Autowired
	private ArchitectureService architectureService;
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Autowired
	private ResponsibilityService responsibilityService;
	
	@Autowired
	private DependencyService dependencyService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private TacticService tacticService;
	
	@Autowired
	private DesignBotService designBotService;
	
	@Autowired
    private ArchitectureAnalysisService architectureAnalysisService;

	@Override
	public ElementChange getElementChange(Long id) {
		return elementChangeDAO.getElementChange(id);
	}

	@Override
	public List<ElementChange> listElementChangeByElementType(String id, String elementType ) {
		return elementChangeDAO.listElementChangeByElementType(id,elementType);
	}

	@Override
	public Long addElementChange(ElementChange elementChange) {
		return elementChangeDAO.addElementChange(elementChange);
	}

	@Override
	public Long lastChangeNumberByElementType(Long id_architecture, String elementType) {
		return elementChangeDAO.lastChangeNumberByElementType(id_architecture,elementType);
	}

	@Override
	public List<ElementChange> lastChangesListByElementType(
			Long id_architecture, Long changeNumber, String elementType) {
		return elementChangeDAO.lastChangesListByElementType(id_architecture, changeNumber, elementType);
	}

	@Override
	public List<ElementChange> listConsistentStatusByArchitectureId(
			Long id_architecture) {
		return elementChangeDAO.listConsistentStatusByArchitectureId(id_architecture);
	}

	@Override
	public List<ElementChange> listElementChangeByArchitectureId(
			Long id_architecture) {
		return elementChangeDAO.listElementChangeByArchitectureId(id_architecture);
	}

	@Override
	public void delete(ElementChange change) {
		elementChangeDAO.delete(change);
	}

	@Override
	public void applyElementsChangesList(Long architectureId, List<ElementChange> changeslist) throws Exception {
		int blockedResult = architectureService.blockArchitecture(architectureId,"Applying Batch Changes Process.");
		if (blockedResult>0) {
			int applyedChangesCount = 0;
			try {
				for (ElementChange change : changeslist) {
					ElementChange result = this.appplyElementChange(change);
					applyedChangesCount++;
					updateIds(change,result,changeslist);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// RollBack changes made
				List<ElementChange> changes = this.listElementChangeByArchitectureId(architectureId);
				for (ElementChange change : changes) {
					if (applyedChangesCount==0) {
						break;
					} else {
						revertChange(change);
					}
					applyedChangesCount--;
				}
				throw new GenericArchitectureException("An error occured while applying changes to Architecture " + architectureId + ". Changes rolled back.");
			} finally {
				architectureService.unblockArchitecture(architectureId);
			}
		} else {
			throw new GenericArchitectureException("Unable to block Architecture " + architectureId + ". Changes not applyed.");
		}
	}
	
	private void updateIds(ElementChange change, ElementChange result, List<ElementChange> changeslist) {
		if (result.getChange_type().toString().equals(ChangeType.INSERT.toString())
				&& result.getElement_type().toString()
						.equals(ElementType.RESPONSIBILITY.toString())) {
			Long oldResponsibilityId = ((Responsibility) change.getNewElement())
					.getId();
			Responsibility addedResponsibility = (Responsibility) result
					.getNewElement();
			for (ElementChange change2 : changeslist) {
				updateChangesIds(change2, oldResponsibilityId,
						addedResponsibility.getId());
			}
		}
		if (result.getChange_type().toString().equals(ChangeType.INSERT.toString())
				&& result.getElement_type().toString()
						.equals(ElementType.MODULE.toString())) {
			Long oldModuleId = ((Module) change.getNewElement()).getId();
			Module newModule = (Module) result.getNewElement();
			for (ElementChange change3 : changeslist) {
				updateModulesIds(change3, oldModuleId, newModule.getId());
			}
		}
	}
	
	private void updateChangesIds(ElementChange change, Long oldResponsibilityId, Long newResponsibilityId) {
		if (change.getElement_type().toString().equals(ElementType.DEPENDENCY.toString())) {
			Dependency dependency = (Dependency) change.getNewElement();
			if (dependency.getId().getChild().equals(oldResponsibilityId))
				dependency.getId().setChild(newResponsibilityId);
			if (dependency.getId().getParent().equals(oldResponsibilityId))
				dependency.getId().setParent(newResponsibilityId);							
		}
		if (change.getElement_type().toString().equals(ElementType.RESPONSIBILITY.toString())) {
			Responsibility responsibility = (Responsibility) change.getNewElement();
			if (responsibility.getId().equals(oldResponsibilityId)) {
				responsibility.setId(newResponsibilityId);
			}
		}
	}
	
	private void updateModulesIds(ElementChange change, Long oldModuleId, Long newModuleId) {
		if (change.getElement_type().toString().equals(ElementType.RESPONSIBILITY.toString())) {
			Responsibility responsibility = (Responsibility) change.getNewElement();
			if (responsibility.getModule().getId().equals(oldModuleId)) {
				responsibility.getModule().setId(newModuleId);
			}
		}
	}
	
	@Override
	public void revertChanges(Long id_architecture, Long lastChangeToRevert) {
		List<ElementChange> changes = this.listElementChangeByArchitectureId(id_architecture);
		for (ElementChange change : changes) {
			if (change.getId() >= lastChangeToRevert) {
				revertChange(change);
			}
		}
	}
	
	private void revertChange(ElementChange change) {
		switch(ElementType.valueOf(change.getElement_type())) {
			case SCENARIO : 
				revertScenarioChange(change); 
				break;
			case RESPONSIBILITY : 
				revertResponsibilityChange(change); 
				break;
			case MODULE : 
				revertModuleChange(change); 
				break;
			case DEPENDENCY : 
				revertDependencyChange(change); 
				break;
			case DESIGNBOT : 
				revertDesignBotChange(change); 
				break;
			case ARCHITECTUREANALYSIS : 
				revertArchitectureAnalysisChange(change); 
				break;
			default: 
				break;
		}
		this.delete(change);
	}

	private void revertScenarioChange(ElementChange change) {
		switch (ChangeType.valueOf(change.getChange_type())) {
		case INSERT:
			scenarioService.delete(((Scenario) change.getNewElement()).getId(),true,true);
			break;
		case DELETE:
			scenarioService.add((Scenario) change.getOldElement(),true, true);
			break;
		case UPDATE:
			scenarioService.update((Scenario) change.getNewElement(),(Scenario) change.getOldElement(),true, true);
			break;
		default:
			break;
		}
	}
	
	private void revertResponsibilityChange(ElementChange change) {
		switch (ChangeType.valueOf(change.getChange_type())) {
		case INSERT:
			responsibilityService.delete(((Responsibility) change.getNewElement()).getId(),true,true);
			break;
		case DELETE:
			Responsibility responsibilityToInsert = (Responsibility) change.getOldElement();
			Set<Scenario> scenarios = new LinkedHashSet<Scenario>(responsibilityToInsert.getScenarios());
			for (Scenario scenario : scenarios) {
				Scenario scenarioFromDatabase = scenarioService.getScenario(scenario.getId());
				responsibilityToInsert.getScenarios().remove(scenario);
				responsibilityToInsert.getScenarios().add(scenarioFromDatabase);
			}
			responsibilityService.add(responsibilityToInsert,true, true);
			break;
		case UPDATE:
			responsibilityService.update((Responsibility) change.getNewElement(),(Responsibility) change.getOldElement(),true, true);
			break;
		default:
			break;
		}
	}
	
	private void revertModuleChange(ElementChange change) {
		switch (ChangeType.valueOf(change.getChange_type())) {
		case INSERT:
			moduleService.delete(((Module) change.getNewElement()).getId(),true,true);
			break;
		case DELETE:
			moduleService.add((Module) change.getOldElement(),true, true);
			break;
		case UPDATE:
			moduleService.update((Module) change.getNewElement(),(Module) change.getOldElement(),true, true);
			break;
		default:
			break;
		}		
	}
	
	private void revertDependencyChange(ElementChange change) {
		switch (ChangeType.valueOf(change.getChange_type())) {
		case INSERT:
			dependencyService.delete(((Dependency) change.getNewElement()).getId(),true,true);
			break;
		case DELETE:
			dependencyService.add((Dependency) change.getOldElement(),true, true);
			break;
		case UPDATE:
			dependencyService.update((Dependency) change.getNewElement(),(Dependency) change.getOldElement(),true, true);
			break;
		default:
			break;
		}		
	}
	
	private void revertDesignBotChange(ElementChange change) {
		switch (ChangeType.valueOf(change.getChange_type())) {
		case INSERT:
			designBotService.delete(((DesignBot) change.getNewElement()).getId(),true);
			break;
		case DELETE:
			DesignBot designBotToInsert = (DesignBot) change.getOldElement();
			Set<Tactic> tactics = new LinkedHashSet<Tactic>(designBotToInsert.getTactics());
			for (Tactic tactic : tactics) {
				Tactic tacticFromDatabase = tacticService.getTactic(tactic.getId());
				designBotToInsert.getTactics().remove(tactic);
				designBotToInsert.getTactics().add(tacticFromDatabase);
			}
			designBotService.add(designBotToInsert, true);
			break;
		case UPDATE:
			designBotService.update((DesignBot) change.getNewElement(),(DesignBot) change.getOldElement(), true);
			break;
		default:
			break;
		}		
	}
	
	private void revertArchitectureAnalysisChange(ElementChange change) {
		switch (ChangeType.valueOf(change.getChange_type())) {
		case INSERT:
			architectureAnalysisService.delete(((ArchitectureAnalysis) change.getNewElement()).getId(),true);
			break;
		case DELETE:
			architectureAnalysisService.add((ArchitectureAnalysis) change.getOldElement(), true);
			break;
		case UPDATE:
			architectureAnalysisService.update((ArchitectureAnalysis) change.getNewElement(),(ArchitectureAnalysis) change.getOldElement(), true);
			break;
		default:
			break;
		}	
	}

	@Override
	public ElementChange appplyElementChange(ElementChange change) {
		switch(ElementType.valueOf(change.getElement_type())) {
		case RESPONSIBILITY : 
			return applyResponsibilityChange(change); 
		case MODULE : 
			return applyModuleChange(change); 
		case DEPENDENCY : 
			return applyDependencyChange(change);
		case SCENARIO : 
			return applyScenarioChange(change);
		default: 
			return null;
		}
	}

	private ElementChange applyScenarioChange(ElementChange change) {
		switch (ChangeType.valueOf(change.getChange_type())) {
		case UPDATE:
			Scenario oldScenario = scenarioService.getScenario(((Scenario) change.getNewElement()).getId());
			return scenarioService.update(oldScenario,(Scenario) change.getNewElement(), change.getConsistent(), change.getUndoChange());
		default:
			return null;
		}
	}
	
	private ElementChange applyDependencyChange(ElementChange change) {
		switch (ChangeType.valueOf(change.getChange_type())) {
		case INSERT:
			return dependencyService.add((Dependency) change.getNewElement(), change.getConsistent(), change.getUndoChange());
		case DELETE:
			return dependencyService.delete(((Dependency) change.getNewElement()).getId(), change.getConsistent(), change.getUndoChange());
		case UPDATE:
			Dependency oldDependency = dependencyService.getDependencyById(((Dependency) change.getNewElement()).getId());
			return dependencyService.update(oldDependency,(Dependency) change.getNewElement(), change.getConsistent(), change.getUndoChange());
		default:
			return null;
		}
	}

	private ElementChange applyModuleChange(ElementChange change) {
		switch (ChangeType.valueOf(change.getChange_type())) {
		case INSERT:
			return moduleService.add(((Module) change.getNewElement()).cloneEmptyId(), change.getConsistent(), change.getUndoChange());
		case DELETE:
			return moduleService.delete(((Module) change.getNewElement()).getId(), change.getConsistent(), change.getUndoChange());
		case UPDATE:
			Module oldModule = moduleService.getModule(((Module) change.getNewElement()).getId());
			return moduleService.update(oldModule,(Module) change.getNewElement(), change.getConsistent(), change.getUndoChange());
		default:
			return null;
		}
	}

	private ElementChange applyResponsibilityChange(ElementChange change) {
		switch (ChangeType.valueOf(change.getChange_type())) {
		case INSERT:
			Responsibility responsibilityToInsert = ((Responsibility) change.getNewElement()).cloneEmptyId();
			Set<Scenario> scenarios = new LinkedHashSet<Scenario>(responsibilityToInsert.getScenarios());
			for (Scenario scenario : scenarios) {
				Scenario scenarioFromDatabase = scenarioService.getScenario(scenario.getId());
				responsibilityToInsert.getScenarios().remove(scenario);
				responsibilityToInsert.getScenarios().add(scenarioFromDatabase);
			}
			return responsibilityService.add(responsibilityToInsert, change.getConsistent(), change.getUndoChange());
		case DELETE:
			return responsibilityService.delete(((Responsibility) change.getNewElement()).getId(), change.getConsistent(), change.getUndoChange());
		case UPDATE:
			Responsibility oldResponsibility = responsibilityService.getResponsibility(((Responsibility) change.getNewElement()).getId());
			return responsibilityService.update(oldResponsibility,(Responsibility) change.getNewElement(), change.getConsistent(), change.getUndoChange());
		default:
			return null;
		}
	}

}
