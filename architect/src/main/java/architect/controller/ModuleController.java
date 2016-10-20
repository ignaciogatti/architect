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
import architect.model.Module;
import architect.service.ElementChangeService;
import architect.service.ModuleService;

@Controller
public class ModuleController extends BaseController {
	
	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private ElementChangeService elementChangeService;
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/listModules", method = RequestMethod.GET)
	public ModelAndView listModules(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("modules", moduleService.listModulesByArchietctureId(architecture.getId()));
		mav.addObject("lastChangeNumber",elementChangeService.lastChangeNumberByElementType(architecture.getId(), ElementType.MODULE.toString()));
		mav.setViewName("/listModules.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/moduleDetails", method = RequestMethod.GET)
	public ModelAndView moduleDetails(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		Long id_module = Long.parseLong(request.getParameter("id"));
		
		Module module = moduleService.getModule(id_module);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("module", module );
		mav.addObject("responsibilities", module.getResponsibilities());
		mav.setViewName("/moduleDetails.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/newModule", method = RequestMethod.GET)
	public ModelAndView newModule(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/newModule.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/editModule", method = RequestMethod.GET)
	public ModelAndView editModule(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		Long id_module = Long.parseLong(request.getParameter("id"));

		ModelAndView mav = new ModelAndView();
		mav.addObject("module", moduleService.getModule(id_module));
		mav.setViewName("/editModule.tiles");
		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/syncModules", method = RequestMethod.GET)
	public @ResponseBody ElementChangeList syncModules(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Long lastChangeNumber = Long.parseLong(request.getParameter("lastChangeNumber"));
		
		List<ElementChange> changes = elementChangeService.lastChangesListByElementType(architecture.getId(), lastChangeNumber, ElementType.MODULE.toString());
		ElementChangeList changesList = new ElementChangeList();
		changesList.setChanges(changes);
		return changesList;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/insertModule", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange insertModule(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		
		Module newModule = new Module();
		newModule.setName(name);
		newModule.setDescription(description);
		newModule.setArchitecture(architecture);
		
		ElementChange change = moduleService.add(newModule,true,false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteModule", method = RequestMethod.GET)
	public synchronized @ResponseBody
	ElementChange deleteModule(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long id_module = Long.parseLong(request.getParameter("id"));

		ElementChange change = moduleService.delete(id_module,true,false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/saveModule", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange saveModule(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long id_module = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		
		Module moduleToUpdate = (Module) moduleService.getModule(id_module);
		
		Module oldModule = moduleToUpdate.clone();
		oldModule.setArchitecture(moduleToUpdate.getArchitecture());
		oldModule.setResponsibilities(moduleToUpdate.getResponsibilities());
		
		moduleToUpdate.setName(name);
		moduleToUpdate.setDescription(description);

		ElementChange change = moduleService.update(oldModule,moduleToUpdate,true,false);
		return change;
	}

	
}
