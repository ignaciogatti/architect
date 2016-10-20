package architect.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import architect.model.Architecture;
import architect.model.Group;
import architect.service.DesignBotService;
import architect.service.ElementChangeService;
import architect.service.ResponsibilityService;
import architect.service.ScenarioService;
import architect.service.UserService;

@Controller
public class SidebarController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ResponsibilityService responsibilityService;
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Autowired
	private DesignBotService designBotService;
	
	@Autowired
	private ElementChangeService elementChangeService;
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "sidebar/listArchitectures", method = RequestMethod.GET)
	public ModelAndView listArchitectures(Model model, HttpServletRequest request) {
		String username = request.getUserPrincipal().getName();
		Set<Group> userGroups = userService.getUserByUserName(username).getGroups();
		ModelAndView mav = new ModelAndView();
		mav.addObject("architectures", architectureService.listArchitecturesByGroups(userGroups));
		mav.setViewName("/sidebarArchitectures.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "sidebar/listScenarios", method = RequestMethod.GET)
	public ModelAndView listScenarios(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("scenarios", scenarioService.listScenariosByArchitectureId(architecture.getId()));
		mav.setViewName("/sidebarScenarios.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "sidebar/listResponsibilities", method = RequestMethod.GET)
	public ModelAndView listResponsibilities(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("responsibilities", responsibilityService.listResponsibilitiesByArchietctureId(architecture.getId()));
		mav.setViewName("/sidebarReponsibilities.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "sidebar/listDesignBots", method = RequestMethod.GET)
	public ModelAndView listDesignBots(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("designbots", designBotService.listDesignBotsByArchietctureId(architecture.getId()));
		mav.setViewName("/sidebarDesignBots.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "sidebar/listArchitectureStates", method = RequestMethod.GET)
	public ModelAndView listArchitectureStates(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("architctureStates", elementChangeService.listConsistentStatusByArchitectureId(architecture.getId()));
		mav.setViewName("/sidebarArchitectureStates.tiles");
		return mav;
	}
	
}
