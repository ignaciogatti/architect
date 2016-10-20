package architect.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import architect.engine.architecture.ElementChangeList;
import architect.engine.architecture.ElementType;
import architect.model.Architecture;
import architect.model.ElementChange;
import architect.model.Group;
import architect.service.ElementChangeService;
import architect.service.GroupService;
import architect.service.UserService;

@Controller
public class ArchitectureController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private ElementChangeService elementChangeService;

	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/")
	public ModelAndView listArchitectures(Model model, HttpServletRequest request) {
		String username = request.getUserPrincipal().getName();
		Set<Group> userGroups = userService.getUserByUserName(username).getGroups();
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("architectures", architectureService.listArchitecturesByGroups(userGroups));
		mav.addObject("lastChangeNumber",elementChangeService.lastChangeNumberByElementType(null, ElementType.ARCHITECTURE.toString()));
		mav.setViewName("/listArchitectures.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/newArchitecture", method = RequestMethod.GET)
	public ModelAndView newArchitecture() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("groups", groupService.listGroups());
		mav.setViewName("/newArchitecture.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/editArchitecture", method = RequestMethod.GET)
	public ModelAndView editArchitecture(HttpServletRequest request) throws Exception {
		Long architectureId = Long.parseLong(request.getParameter("id"));
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("architecture", architectureService.getArchitectureById(architectureId));
		mav.addObject("groups", groupService.listGroups());
		mav.setViewName("/editArchitecture.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/syncArchitecture", method = RequestMethod.GET)
	public @ResponseBody ElementChangeList syncArchitecture(HttpServletRequest request) throws Exception {
		Long lastChangeNumber = Long.parseLong(request.getParameter("lastChangeNumber"));
		
		List<ElementChange> changes = elementChangeService.lastChangesListByElementType(null, lastChangeNumber, ElementType.ARCHITECTURE.toString());
		ElementChangeList changesList = new ElementChangeList();
		changesList.setChanges(changes);
		return changesList;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/insertArchitecture", method = RequestMethod.GET)
	public synchronized @ResponseBody
	ElementChange insertArchitecture(HttpServletRequest request) throws IOException {
		String name = request.getParameter("name");
		Long groupId = Long.parseLong(request.getParameter("group"));

		Group group = groupService.getGroupById(groupId);
		
		Architecture newArchitecture = new Architecture();
		newArchitecture.setName(name);	
		newArchitecture.setGroup(group);
		newArchitecture.setBlocked(new Boolean(false));
		
		ElementChange change = architectureService.addArchitecture(newArchitecture);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/saveArchitecture", method = RequestMethod.GET)
	public synchronized @ResponseBody
	ElementChange saveArchitecture(HttpServletRequest request) throws Exception {
		Long architectureId = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
		Long groupId = Long.parseLong(request.getParameter("group"));
		
		Architecture architecture = architectureService.getArchitectureById(architectureId);
		Group group = groupService.getGroupById(groupId);
		architecture.setName(name);
		architecture.setGroup(group);
		
		ElementChange change = architectureService.updateArchitecture(architecture);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteArchitecture", method = RequestMethod.GET)
	public synchronized @ResponseBody
	ElementChange deleteArchitecture(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		
		ElementChange change = architectureService.deleteArchitecture(architecture.getId());
		return change;
	}

}
