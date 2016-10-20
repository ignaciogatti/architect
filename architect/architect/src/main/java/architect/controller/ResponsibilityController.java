package architect.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import architect.engine.architecture.ElementChangeList;
import architect.engine.architecture.ElementType;
import architect.model.Architecture;
import architect.model.ElementChange;
import architect.model.Module;
import architect.model.Responsibility;
import architect.model.Scenario;
import architect.service.ElementChangeService;
import architect.service.ModuleService;
import architect.service.ResponsibilityService;
import architect.service.ScenarioService;

@Controller
public class ResponsibilityController extends BaseController {
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Autowired
	private ResponsibilityService responsibilityService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private ElementChangeService elementChangeService;
	
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/listResponsibilities", method = RequestMethod.GET)
	public ModelAndView listResponsibilities(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("responsibilities", responsibilityService.listResponsibilitiesByArchietctureId(architecture.getId()));
		mav.addObject("lastChangeNumber",elementChangeService.lastChangeNumberByElementType(architecture.getId(), ElementType.RESPONSIBILITY.toString()));
		mav.setViewName("/listResponsibilities.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/responsibilityDetails", method = RequestMethod.GET)
	public ModelAndView responsibilityDetails(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		Long responsibilityId = Long.parseLong(request.getParameter("id"));
		
		Responsibility responsibility = responsibilityService.getResponsibility(responsibilityId);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("responsibility", responsibility );
		mav.addObject("dependencies", responsibility.getDependenciesAsChild());
		mav.setViewName("/responsibilityDetails.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/newResponsibility", method = RequestMethod.GET)
	public ModelAndView newResponsibility(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("scenarios", scenarioService.listScenariosByArchitectureId(architecture.getId()));
		mav.addObject("modules", moduleService.listModulesByArchietctureId(architecture.getId()));
		mav.setViewName("/newResponsibility.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/editResponsibilityDialog", method = RequestMethod.GET)
	public ModelAndView editResponsibilityDialog(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Long responsibilityId = Long.parseLong(request.getParameter("id"));

		ModelAndView mav = new ModelAndView();
		mav.addObject("responsibility", responsibilityService.getResponsibility(responsibilityId));
		mav.addObject("modules", moduleService.listModulesByArchietctureId(architecture.getId()));
		mav.setViewName("/editResponsibilityDialog.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/editScenariosDialog", method = RequestMethod.GET)
	public ModelAndView editScenariosDialog(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Long responsibilityId = Long.parseLong(request.getParameter("id"));
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("responsibility", responsibilityService.getResponsibility(responsibilityId));
		mav.addObject("scenarios", scenarioService.listScenariosByArchitectureId(architecture.getId()));
		mav.setViewName("/editScenariosDialog.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/editDependenciesDialog", method = RequestMethod.GET)
	public ModelAndView editDependenciesDialog(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		Long responsibilityId = Long.parseLong(request.getParameter("id"));
		
		Responsibility responsibility = responsibilityService.getResponsibility(responsibilityId);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("responsibility", responsibility );
		mav.addObject("dependencies", responsibility.getDependenciesAsChild());
		mav.setViewName("/editDependenciesDialog.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/syncResponsibilities", method = RequestMethod.GET)
	public @ResponseBody ElementChangeList syncResponsibilities(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Long lastChangeNumber = Long.parseLong(request.getParameter("lastChangeNumber"));
		
		List<ElementChange> changes = elementChangeService.lastChangesListByElementType(architecture.getId(), lastChangeNumber, ElementType.RESPONSIBILITY.toString());
		ElementChangeList changesList = new ElementChangeList();
		changesList.setChanges(changes);
		return changesList;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/insertResponsibility", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange insertResponsibility(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String executionTimeString = request.getParameter("executionTime");
		String complexityString = request.getParameter("complexity");
		String moduleString = request.getParameter("module");
		
		Module module = null;
		if (moduleString!=null && !moduleString.equals("")) 
			module = moduleService.getModule(Long.parseLong(moduleString));
		
		Double complexity = Responsibility.DEFAULT_COMPLEXITY_COST;
		if (complexityString!=null && !complexityString.equals("")) {
			complexity = Double.valueOf(complexityString);
		}
		
		Long executionTime = Responsibility.DEFAULT_EXECUTION_TIME;
		if (executionTimeString!=null && !executionTimeString.equals("")) {
			executionTime = Long.valueOf(executionTimeString);
		}
		
		Responsibility newResponsibility = new Responsibility();
		newResponsibility.setName(name);
		newResponsibility.setDescription(description);
		newResponsibility.setComplexity(complexity);
		newResponsibility.setExecutionTime(executionTime);
		newResponsibility.setModule(module);
		newResponsibility.setArchitecture(architecture);
		
		ElementChange change = responsibilityService.add(newResponsibility,true,false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteResponsibility", method = RequestMethod.GET)
	public synchronized @ResponseBody
	ElementChange deleteResponsibility(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long responsibilityId = Long.parseLong(request.getParameter("id"));

		ElementChange change = responsibilityService.delete(responsibilityId,true,false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/saveResponsibility", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange saveResponsibility(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long responsibilityId = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String moduleString = request.getParameter("module");
		String complexityString = request.getParameter("complexity");
		String executionTimeString = request.getParameter("executionTime");
		
		Module module = null;
		if (moduleString!=null && !moduleString.equals("")) 
			module = moduleService.getModule(Long.parseLong(moduleString));
		
		Double complexity = Responsibility.DEFAULT_COMPLEXITY_COST;
		if (complexityString!=null && !complexityString.equals("")) {
			complexity = Double.valueOf(complexityString);
		}
		
		Long executionTime = Responsibility.DEFAULT_EXECUTION_TIME;
		if (executionTimeString!=null && !executionTimeString.equals("")) {
			executionTime = Long.valueOf(executionTimeString);
		}
		
		Responsibility responsibilityToUpdate = (Responsibility) responsibilityService.getResponsibility(responsibilityId);
		
		Responsibility oldResponsibility = responsibilityToUpdate.clone();
		oldResponsibility.setArchitecture(responsibilityToUpdate.getArchitecture());
		oldResponsibility.setDependenciesAsChild(responsibilityToUpdate.getDependenciesAsChild());
		oldResponsibility.setDependenciesAsParent(responsibilityToUpdate.getDependenciesAsParent());
		oldResponsibility.setScenarios(responsibilityToUpdate.getScenarios());
		oldResponsibility.setModule(responsibilityToUpdate.getModule());
		
		responsibilityToUpdate.setName(name);
		responsibilityToUpdate.setDescription(description);
		responsibilityToUpdate.setModule(module);
		responsibilityToUpdate.setComplexity(complexity);
		responsibilityToUpdate.setExecutionTime(executionTime);
		
		ElementChange change = responsibilityService.update(oldResponsibility, responsibilityToUpdate,true,false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/saveScenarios", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange saveScenarios(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long responsibilityId = Long.parseLong(request.getParameter("id"));
		String scenarios = request.getParameter("scenarios");
		
		Responsibility responsibilityToUpdate = (Responsibility) responsibilityService.getResponsibility(responsibilityId);
		
		Responsibility oldResponsibility = responsibilityToUpdate.clone();
		oldResponsibility.setArchitecture(responsibilityToUpdate.getArchitecture());
		oldResponsibility.setDependenciesAsChild(responsibilityToUpdate.getDependenciesAsChild());
		oldResponsibility.setDependenciesAsParent(responsibilityToUpdate.getDependenciesAsParent());
		oldResponsibility.setScenarios(new LinkedHashSet<Scenario>(responsibilityToUpdate.getScenarios()));
		oldResponsibility.setModule(responsibilityToUpdate.getModule());
		
		List<Scenario> newScenarios = new ArrayList<Scenario>();
		if (!scenarios.equals("")) {
			String[] scenariosIds = scenarios.split(",");
			for (int i = 0; i < scenariosIds.length; i++) {
				Scenario s = scenarioService.getScenario(Long.parseLong(scenariosIds[i]));
				newScenarios.add(s);
			}
		}	
		
		responsibilityToUpdate.getScenarios().clear();
		responsibilityToUpdate.getScenarios().addAll(newScenarios);

		ElementChange change = responsibilityService.update(oldResponsibility, responsibilityToUpdate,true,false);
		return change;
	}
	
}
