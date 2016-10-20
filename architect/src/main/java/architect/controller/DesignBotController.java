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
import architect.model.DesignBot;
import architect.model.ElementChange;
import architect.model.QualityAttribute;
import architect.model.Tactic;
import architect.service.DesignBotService;
import architect.service.ElementChangeService;
import architect.service.QualityAttributeService;
import architect.service.TacticService;

@Controller
public class DesignBotController extends BaseController {
	
	@Autowired
	private DesignBotService designBotService;
	
	@Autowired
	private QualityAttributeService qualityAttributeService;
	
	@Autowired
	private TacticService tacticService;
	
	@Autowired
	private ElementChangeService elementChangeService;
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/listDesignBots", method = RequestMethod.GET)
	public ModelAndView listDesignBots(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("designbots", designBotService.listDesignBotsByArchietctureId(architecture.getId()));
		mav.addObject("lastChangeNumber",elementChangeService.lastChangeNumberByElementType(architecture.getId(), ElementType.DESIGNBOT.toString()));
		mav.setViewName("/listDesignBots.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/designBotDetails", method = RequestMethod.GET)
	public ModelAndView designBotDetails(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		Long id_designBot = Long.parseLong(request.getParameter("id"));
		
		DesignBot designBot = designBotService.getDesignBot(id_designBot);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("designBot", designBot );
		mav.setViewName("/designBotDetails.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/newDesignBot", method = RequestMethod.GET)
	public ModelAndView newDesignBot(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("qualityAttributes", qualityAttributeService.listQualityAttributes());
		mav.setViewName("/newDesignBot.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/editDesignBot", method = RequestMethod.GET)
	public ModelAndView editDesignBot(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		Long id_designBot = Long.parseLong(request.getParameter("id"));

		DesignBot designBot = designBotService.getDesignBot(id_designBot);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("designBot", designBot );
		mav.addObject("tactics", tacticService.listTacticsByQualityAttributeId(designBot.getQualityAttribute().getId()) );
		mav.setViewName("/editDesignBot.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/syncDesignBot", method = RequestMethod.GET)
	public @ResponseBody ElementChangeList syncDesignBot(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Long lastChangeNumber = Long.parseLong(request.getParameter("lastChangeNumber"));
		
		List<ElementChange> changes = elementChangeService.lastChangesListByElementType(architecture.getId(), lastChangeNumber,ElementType.DESIGNBOT.toString());
		ElementChangeList changesList = new ElementChangeList();
		changesList.setChanges(changes);
		return changesList;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/insertDesignBot", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange insertDesignBot(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		String name = request.getParameter("name");
		Long id_qualityAttribute = Long.parseLong(request.getParameter("qualityAttribute"));
		
		QualityAttribute qualityAttribute = qualityAttributeService.getQualityAttribute(id_qualityAttribute);
		
		DesignBot newDesignBot = new DesignBot();
		newDesignBot.setName(name);
		newDesignBot.setQualityAttribute(qualityAttribute);
		newDesignBot.setArchitecture(architecture);
		
		ElementChange change = designBotService.add(newDesignBot,false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteDesignBot", method = RequestMethod.GET)
	public synchronized @ResponseBody
	ElementChange deleteDesignBot(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long id_designBot = Long.parseLong(request.getParameter("id"));

		ElementChange change = designBotService.delete(id_designBot,false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/saveDesignBot", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange saveDesignBot(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long id_designBot = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
		String tactics = request.getParameter("tactics");
		
		DesignBot designBotToUpdate = (DesignBot) designBotService.getDesignBot(id_designBot);
		
		DesignBot oldDesignBot = designBotToUpdate.clone();
		oldDesignBot.setArchitecture(designBotToUpdate.getArchitecture());
		oldDesignBot.setTactics(new LinkedHashSet<Tactic>(designBotToUpdate.getTactics()));
		
		List<Tactic> newTactics = new ArrayList<Tactic>();
		if (!tactics.equals("")) {
			String[] tacticsIds = tactics.split(",");
			for (int i = 0; i < tacticsIds.length; i++) {
				Tactic t = tacticService.getTactic(Long.parseLong(tacticsIds[i]));
				newTactics.add(t);
			}
		}
		
		designBotToUpdate.setName(name);
		designBotToUpdate.getTactics().clear();
		designBotToUpdate.getTactics().addAll(newTactics);
		
		ElementChange change = designBotService.update(oldDesignBot,designBotToUpdate,false);
		return change;
	}
	
}
