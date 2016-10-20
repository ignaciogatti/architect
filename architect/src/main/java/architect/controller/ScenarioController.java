package architect.controller;

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
import architect.model.QualityAttribute;
import architect.model.Scenario;
import architect.model.Scenario.ResponseType;
import architect.service.ElementChangeService;
import architect.service.QualityAttributeService;
import architect.service.ResponsibilityService;
import architect.service.ScenarioService;

@Controller
public class ScenarioController extends BaseController {

	@Autowired
	private ScenarioService scenarioService;

	@Autowired
	private ResponsibilityService responsibilityService;
	
	@Autowired
	private QualityAttributeService qualityAttributeService;

	@Autowired
	private ElementChangeService elementChangeService;

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/listScenarios", method = RequestMethod.GET)
	public ModelAndView listScenarios(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("scenarios", scenarioService.listScenariosByArchitectureId(architecture.getId()));
		mav.addObject("lastChangeNumber",elementChangeService.lastChangeNumberByElementType(architecture.getId(), ElementType.SCENARIO.toString()));
		mav.setViewName("/listScenarios.tiles");

		return mav;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/newScenario", method = RequestMethod.GET)
	public ModelAndView newScenario(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		ModelAndView mav = new ModelAndView();
		mav.addObject("types", qualityAttributeService.listQualityAttributes());
		mav.setViewName("/newscenario.tiles");
		return mav;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/editScenario", method = RequestMethod.GET)
	public ModelAndView editScenario(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		Long id = Long.parseLong(request.getParameter("id"));
		
		Scenario scenario = scenarioService.getScenario(id);
		List<QualityAttribute> types = qualityAttributeService.listQualityAttributes();
		ModelAndView mav = new ModelAndView();
		mav.addObject("currentScenario", scenario);
		mav.addObject("types", types);
		mav.setViewName("/editscenario.tiles");
		return mav;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/viewScenario", method = RequestMethod.GET)
	public ModelAndView viewScenario(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		Long id = Long.parseLong(request.getParameter("id"));
		
		Scenario scenario = scenarioService.getScenario(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("currentScenario", scenario);
		mav.addObject("responsibilities", scenario.getResponsibilities());
		mav.setViewName("/viewscenario.tiles");
		return mav;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/viewScenarioResponsibilities", method = RequestMethod.GET)
	public ModelAndView viewScenarioResponsibilities(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		Long id = Long.parseLong(request.getParameter("id"));
		
		Scenario scenario = scenarioService.getScenario(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("responsibilities", scenario.getResponsibilities());
		mav.setViewName("/viewResponsabilities.tiles");
		return mav;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/syncScenarios", method = RequestMethod.GET)
	public @ResponseBody ElementChangeList syncScenarios(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Long lastChangeNumber = Long.parseLong(request.getParameter("lastChangeNumber"));
		
		List<ElementChange> changes = elementChangeService.lastChangesListByElementType(architecture.getId(), lastChangeNumber,ElementType.SCENARIO.toString());
		ElementChangeList changesList = new ElementChangeList();
		changesList.setChanges(changes);
		return changesList;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/insertScenario", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange insertScenario(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		Long id_qualityAttribute = Long.parseLong(request.getParameter("type"));
		
		QualityAttribute type = qualityAttributeService.getQualityAttribute(id_qualityAttribute);
		Scenario scenarioToAdd = new Scenario();
		scenarioToAdd.setName(name);
		scenarioToAdd.setDescription(description);
		scenarioToAdd.setQualityAttribute(type);
		scenarioToAdd.setStimulus(request.getParameter("stimulus"));
		scenarioToAdd.setArtifact(request.getParameter("artifact"));
		scenarioToAdd.setSource(request.getParameter("source"));
		scenarioToAdd.setEnviroment(request.getParameter("enviroment"));
		scenarioToAdd.setMeasure(Long.parseLong(request.getParameter("measure")));
		scenarioToAdd.setResponse(ResponseType.values()[Integer.valueOf(request.getParameter("response"))]);
		scenarioToAdd.setPriority(Long.parseLong(request.getParameter("priority")));
		scenarioToAdd.setArchitecture(architecture);

		// Add scenarioToAdd to DB and send it
		ElementChange change = scenarioService.add(scenarioToAdd,true,false);
		return change;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteScenario", method = RequestMethod.GET)
	public synchronized @ResponseBody
	ElementChange deleteScenario(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long id_scenario = Long.parseLong(request.getParameter("id"));

		// Delete scenarioToDelete from DB
		ElementChange change = scenarioService.delete(id_scenario,true,false);
		return change;
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/saveScenario", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange saveScenario(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		Long id_qualityAttribute = Long.parseLong(request.getParameter("type"));
		String id = request.getParameter("id");
		
		QualityAttribute type = qualityAttributeService.getQualityAttribute(id_qualityAttribute);
		Scenario scenarioToUpdate = scenarioService.getScenario(Long.parseLong(id));
		
		Scenario oldScenario =  scenarioToUpdate.clone();
		oldScenario.setArchitecture(scenarioToUpdate.getArchitecture());
		oldScenario.setResponsibilities(scenarioToUpdate.getResponsibilities());
		
		scenarioToUpdate.setName(name);
		scenarioToUpdate.setDescription(description);
		scenarioToUpdate.setQualityAttribute(type);
		scenarioToUpdate.setStimulus(request.getParameter("stimulus"));
		scenarioToUpdate.setPriority(Long.parseLong(request.getParameter("priority")));
		scenarioToUpdate.setArtifact(request.getParameter("artifact"));
		scenarioToUpdate.setSource(request.getParameter("source"));
		scenarioToUpdate.setEnviroment(request.getParameter("enviroment"));
		scenarioToUpdate.setMeasure(Long.parseLong(request.getParameter("measure")));
		scenarioToUpdate.setResponse(ResponseType.values()[Integer.valueOf(request.getParameter("response"))]);

		// Update scenarioToUpdate in DB
		ElementChange change = scenarioService.update(oldScenario, scenarioToUpdate,true, false);
		return change;
	}

}
