package architect.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import architect.engine.architecture.AnalysisResults;
import architect.engine.architecture.ArchitectureResults;
import architect.engine.architecture.DesignBotResults;
import architect.engine.architecture.ElementChangeList;
import architect.engine.architecture.ElementType;
import architect.engine.architecture.TacticChange;
import architect.engine.architecture.algorithms.Backtracking;
import architect.engine.architecture.algorithms.Predictive;
import architect.engine.architecture.algorithms.TrackAlgorithm;
import architect.model.Architecture;
import architect.model.ArchitectureAnalysis;
import architect.model.Dependency;
import architect.model.DesignBot;
import architect.model.ElementChange;
import architect.model.Module;
import architect.model.Responsibility;
import architect.model.Scenario;
import architect.service.ArchitectureAnalysisService;
import architect.service.DesignBotService;
import architect.service.ElementChangeService;
import architect.service.ModuleService;
import architect.service.ResponsibilityService;
import architect.service.ScenarioService;
import architect.service.analysis.AnalysisService;

@Controller
public class AnalysisPanelController extends BaseController {
		
	@Autowired
    private ScenarioService scenarioService;
	
	@Autowired
    private ArchitectureAnalysisService architectureAnalysisService;
	
	@Autowired
	private ResponsibilityService responsibilityService;
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private DesignBotService designBotService;
	
	@Autowired
	private ElementChangeService elementChangeService;
	
	@Autowired
	private AnalysisService analysisService;

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/analysisPanel", method = RequestMethod.GET)
	public ModelAndView analysisPanel(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);

		architectureService.getArchitectureById(architecture.getId());
		ArchitectureResults architectureResults = new ArchitectureResults(architecture,null);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("actualArchitectureResults", architectureResults);
		mav.addObject("architectureAnalysis", architectureAnalysisService.listArchitectureAnalysisByArchitectureId(architecture.getId()));
		mav.addObject("lastChangeNumber",elementChangeService.lastChangeNumberByElementType(architecture.getId(), ElementType.ARCHITECTUREANALYSIS.toString()));
		mav.setViewName("/analysisPanel.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/newArchitectureAnalysis", method = RequestMethod.GET)
	public ModelAndView newArchitectureAnalysis(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
			
		ModelAndView mav = new ModelAndView();
		mav.addObject("scenarios", scenarioService.listScenariosByArchitectureId(architecture.getId()));
		mav.addObject("designBots", designBotService.listDesignBotsByArchietctureId(architecture.getId()));
		mav.setViewName("/newArchitectureAnalysis.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/editArchitectureAnalysis", method = RequestMethod.GET)
	public ModelAndView editArchitectureAnalysis(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Long architectureAnalysisId = Long.parseLong(request.getParameter("id"));

		ModelAndView mav = new ModelAndView();
		mav.addObject("architectureAnalysis", architectureAnalysisService.getArchitectureAnalysis(architectureAnalysisId));
		mav.addObject("scenarios", scenarioService.listScenariosByArchitectureId(architecture.getId()));
		mav.addObject("designBots", designBotService.listDesignBotsByArchietctureId(architecture.getId()));
		mav.setViewName("/editArchitectureAnalysis.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/syncArchitectureAnalysis", method = RequestMethod.GET)
	public @ResponseBody ElementChangeList syncArchitectureAnalysis(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);		
		Long lastChangeNumber = Long.parseLong(request.getParameter("lastChangeNumber"));
		
		List<ElementChange> changes = elementChangeService.lastChangesListByElementType(architecture.getId(), lastChangeNumber,ElementType.ARCHITECTUREANALYSIS.toString());
		ElementChangeList changesList = new ElementChangeList();
		changesList.setChanges(changes);
		return changesList;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/insertArchitectureAnalysis", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange insertArchitectureAnalysis(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		Long id_scenario = Long.parseLong(request.getParameter("scenario"));
		Long id_designBot = Long.parseLong(request.getParameter("designBot"));
		Boolean enable = Boolean.valueOf(request.getParameter("enable"));
		
		ArchitectureAnalysis newArchitectureAnalysis = new ArchitectureAnalysis();
		newArchitectureAnalysis.setEnable(enable);
		newArchitectureAnalysis.setScenario(scenarioService.getScenario(id_scenario));
		newArchitectureAnalysis.setDesignBot(designBotService.getDesignBot(id_designBot));
		newArchitectureAnalysis.setArchitecture(architectureService.getArchitectureById(architecture.getId()));
		
		ElementChange change = architectureAnalysisService.add(newArchitectureAnalysis, false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteArchitectureAnalysis", method = RequestMethod.GET)
	public synchronized @ResponseBody
	ElementChange deleteArchitectureAnalysis(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long architectureAnalysisId = Long.parseLong(request.getParameter("id"));

		ElementChange change = architectureAnalysisService.delete(architectureAnalysisId, false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/saveArchitectureAnalysis", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange saveArchitectureAnalysis(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long id_architectureAnalysis = Long.parseLong(request.getParameter("id"));
		Long id_scenario = Long.parseLong(request.getParameter("scenario"));
		Long id_designBot = Long.parseLong(request.getParameter("designBot"));
		Boolean enable = Boolean.valueOf(request.getParameter("enable"));
		
		Scenario scenario = scenarioService.getScenario(id_scenario);
		DesignBot designBot = designBotService.getDesignBot(id_designBot);
		
		ArchitectureAnalysis architectureAnalysis = (ArchitectureAnalysis) architectureAnalysisService.getArchitectureAnalysis(id_architectureAnalysis);
		
		ArchitectureAnalysis oldArchitectureAnalysis = architectureAnalysis.clone();
		oldArchitectureAnalysis.setArchitecture(architectureAnalysis.getArchitecture());
		oldArchitectureAnalysis.setDesignBot(architectureAnalysis.getDesignBot());
		oldArchitectureAnalysis.setScenario(architectureAnalysis.getScenario());
		
		architectureAnalysis.setScenario(scenario);
		architectureAnalysis.setDesignBot(designBot);
		architectureAnalysis.setEnable(enable);

		ElementChange change = architectureAnalysisService.update(oldArchitectureAnalysis,architectureAnalysis,false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/syncOutput", method = RequestMethod.GET)
	public @ResponseBody AnalysisResults syncOutput(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		AnalysisResults results = analysisService.getAnalysisResultsByArchitectureId(architecture.getId());
		return results;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/startFullAnalysis", method = RequestMethod.POST)
	public synchronized @ResponseBody
	String startFullAnalysis(HttpServletRequest request) throws Exception{
		Architecture architecture = validateArchitectureModificationAction(request);
		String selectedAlgorithm = request.getParameter("algorithm");
		Boolean negotiation = new Boolean(request.getParameter("negotiation"));
		
		TrackAlgorithm selectedTrackAlgorithm = new Predictive();
		if (selectedAlgorithm!=null && selectedAlgorithm.equals("backtracking")){
			selectedTrackAlgorithm = new Backtracking();
		}		
		
		String result = analysisService.startFullAnalysis(architecture.getId(), selectedTrackAlgorithm, negotiation);
	
		return result;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/startIndividualArchitectureAnalysis", method = RequestMethod.POST)
	public synchronized @ResponseBody
	String startIndividualArchitectureAnalysis(HttpServletRequest request) throws Exception{
		validateArchitectureModificationAction(request);
		Long id_architectureAnalysis = Long.parseLong(request.getParameter("id"));
		String selectedAlgorithm = request.getParameter("algorithm");
		
		TrackAlgorithm selectedTrackAlgorithm = new Predictive();
		if (selectedAlgorithm!=null && selectedAlgorithm.equals("backtracking")){
			selectedTrackAlgorithm = new Backtracking();
		}
		
		ArchitectureAnalysis architectureAnalysis = (ArchitectureAnalysis) architectureAnalysisService.getArchitectureAnalysisEAGER(id_architectureAnalysis);
		String result = analysisService.startIndividualArchitectureAnalysis(architectureAnalysis,selectedTrackAlgorithm);
	
		return result;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/stopIndividualArchitectureAnalysis", method = RequestMethod.POST)
	public synchronized @ResponseBody
	String stopIndividualArchitectureAnalysis(HttpServletRequest request) throws Exception{
		validateArchitectureReadOnlyAction(request);
		Long id_architectureAnalysis = Long.parseLong(request.getParameter("id"));
		
		ArchitectureAnalysis architectureAnalysis = (ArchitectureAnalysis) architectureAnalysisService.getArchitectureAnalysisEAGER(id_architectureAnalysis);
		String result = analysisService.stopIndividualArchitectureAnalysis(architectureAnalysis);
		
		return result;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/appliedChangesList", method = RequestMethod.GET)
	public ModelAndView appliedChangesList(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Long idDesignBot = Long.parseLong(request.getParameter("id"));

		AnalysisResults results = analysisService.getAnalysisResultsByArchitectureId(architecture.getId());
		DesignBotResults designBotResults = results.getDesignBotResultsById(idDesignBot);
		List<TacticChange> tacticsChanges = designBotResults.getChanges();
		
		for (int i=0; i<tacticsChanges.size(); i++) {
			TacticChange tChange = tacticsChanges.get(i);
			tChange.setId(new Long(i));
			for(int j=0; j<tChange.getAppliedChanges().size(); j++) {
				tChange.getAppliedChanges().get(j).setId(new Long(j));
			}
		}
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("designBotResults", designBotResults);
		mav.setViewName("/appliedChangesList.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/updateResponsibilityChange", method = RequestMethod.POST)
	public @ResponseBody String updateResponsibilityChange(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		Long designBotId = Long.parseLong(request.getParameter("designBotId"));
		Integer tacticId = Integer.parseInt(request.getParameter("tacticId"));
		Integer changeId = Integer.parseInt(request.getParameter("changeId"));
		Double complexity = Double.parseDouble(request.getParameter("complexity"));
		String name = request.getParameter("name");
		
		AnalysisResults results = analysisService.getAnalysisResultsByArchitectureId(architecture.getId());
		DesignBotResults designBotResults = results.getDesignBotResultsById(designBotId);
		List<TacticChange> tacticsChanges = designBotResults.getChanges();
		
		TacticChange tChange = tacticsChanges.get(tacticId);
		Responsibility element = (Responsibility) tChange.getAppliedChanges().get(changeId).getNewElement();
		element.setComplexity(new Double(complexity));
		element.setName(new String(name));
		return "OK";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/updateDependencyChange", method = RequestMethod.POST)
	public @ResponseBody String updateDependencyChange(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		Long designBotId = Long.parseLong(request.getParameter("designBotId"));
		Integer tacticId = Integer.parseInt(request.getParameter("tacticId"));
		Integer changeId = Integer.parseInt(request.getParameter("changeId"));
		Double couplingCost = Double.parseDouble(request.getParameter("couplingCost"));
		
		AnalysisResults results = analysisService.getAnalysisResultsByArchitectureId(architecture.getId());
		DesignBotResults designBotResults = results.getDesignBotResultsById(designBotId);
		List<TacticChange> tacticsChanges = designBotResults.getChanges();
		
		TacticChange tChange = tacticsChanges.get(tacticId);
		Dependency element = (Dependency) tChange.getAppliedChanges().get(changeId).getNewElement();
		element.setCouplingcost(new Double(couplingCost));
		
		return "OK";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/updateModuleChange", method = RequestMethod.POST)
	public @ResponseBody String updateModuleChange(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		Long designBotId = Long.parseLong(request.getParameter("designBotId"));
		Integer tacticId = Integer.parseInt(request.getParameter("tacticId"));
		Integer changeId = Integer.parseInt(request.getParameter("changeId"));
		String name = request.getParameter("name");
				
		AnalysisResults results = analysisService.getAnalysisResultsByArchitectureId(architecture.getId());
		DesignBotResults designBotResults = results.getDesignBotResultsById(designBotId);
		List<TacticChange> tacticsChanges = designBotResults.getChanges();
		
		TacticChange tChange = tacticsChanges.get(tacticId);
		Module element = (Module) tChange.getAppliedChanges().get(changeId).getNewElement();
		element.setName(new String(name));
		
		return "OK";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/updateScenarioChange", method = RequestMethod.POST)
	public @ResponseBody String updateScenarioChange(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		Long designBotId = Long.parseLong(request.getParameter("designBotId"));
		Integer tacticId = Integer.parseInt(request.getParameter("tacticId"));
		Integer changeId = Integer.parseInt(request.getParameter("changeId"));
		Long scenarioPeriod = Long.parseLong(request.getParameter("scenarioPeriod"));
				
		AnalysisResults results = analysisService.getAnalysisResultsByArchitectureId(architecture.getId());
		DesignBotResults designBotResults = results.getDesignBotResultsById(designBotId);
		List<TacticChange> tacticsChanges = designBotResults.getChanges();
		
		TacticChange tChange = tacticsChanges.get(tacticId);
		Scenario element = (Scenario) tChange.getAppliedChanges().get(changeId).getNewElement();
		element.setMeasure(scenarioPeriod);
		
		return "OK";
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/applyChangesDesignBot", method = RequestMethod.POST)
	public synchronized @ResponseBody
	String applyChangesDesignBot(HttpServletRequest request) throws Exception{
		Architecture architecture = validateArchitectureModificationAction(request);
		Long idDesignBot = Long.parseLong(request.getParameter("id"));
		
		AnalysisResults results = analysisService.getAnalysisResultsByArchitectureId(architecture.getId());
		if (results==null) {
			return "Architecture " + architecture.getId() + " not saved, results for design bot " + idDesignBot + " not found.";
		}
		
		DesignBotResults designBotResults = results.getDesignBotResultsById(idDesignBot);
		if (designBotResults==null) {
			return "Architecture " + architecture.getId() + " not saved, results for design bot " + idDesignBot + " not found.";
		}
		
		List<TacticChange> tacticsChanges = designBotResults.getChanges();
		List<ElementChange> allAppliedChanges = new ArrayList<ElementChange>();
		for(TacticChange tacticChange : tacticsChanges) {
			allAppliedChanges.addAll(tacticChange.getAppliedChanges());
		}
		
		elementChangeService.applyElementsChangesList(architecture.getId(), allAppliedChanges);
		analysisService.deleteAllAnalysis();
		
		return "Architecture " + architecture.getId() + " saved";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/blockArchitecture", method = RequestMethod.POST)
	public synchronized @ResponseBody
	String blockArchitecture(HttpServletRequest request) throws Exception{
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Boolean blockValue = Boolean.parseBoolean(request.getParameter("blockValue"));
		
		if (blockValue)
			architectureService.blockArchitecture(architecture.getId(), "");
		else
			architectureService.unblockArchitecture(architecture.getId());
		
		return "OK";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/cleanAllAnalisys", method = RequestMethod.POST)
	public synchronized @ResponseBody ModelAndView cleanAllAnalisys(HttpServletRequest request) throws Exception{
		Architecture architecture = validateArchitectureModificationAction(request);
	
		analysisService.deleteAllAnalysis();
		ArchitectureResults architectureResults = new ArchitectureResults(architecture,null);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("actualArchitectureResults", architectureResults);
		mav.setViewName("analysisPanel/AnalysisResult");
		return mav;
	}

}
