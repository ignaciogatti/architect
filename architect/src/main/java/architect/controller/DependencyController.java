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
import architect.model.Dependency;
import architect.model.DependencyId;
import architect.model.ElementChange;
import architect.model.Responsibility;
import architect.service.DependencyService;
import architect.service.ElementChangeService;
import architect.service.ResponsibilityService;

@Controller
public class DependencyController extends BaseController {
	
	@Autowired
	private ResponsibilityService responsibilityService;
	
	@Autowired
	private DependencyService dependencyService;

	@Autowired
	private ElementChangeService elementChangeService;

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/listDependencies", method = RequestMethod.GET)
	public ModelAndView listDependencies(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		
		List<Dependency> dependencies = dependencyService.listDependencyByArchietctureId(architecture.getId());
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("dependencies", dependencies);
		mav.addObject("lastChangeNumber",elementChangeService.lastChangeNumberByElementType(architecture.getId(), ElementType.DEPENDENCY.toString()));
		mav.setViewName("/listDependencies.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/newDependency", method = RequestMethod.GET)
	public ModelAndView newDependency(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
			
		ModelAndView mav = new ModelAndView();
		mav.addObject("responsibilities", responsibilityService.listResponsibilitiesByArchietctureId(architecture.getId()));
		mav.setViewName("/newDependency.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/editDependency", method = RequestMethod.GET)
	public ModelAndView editDependency(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Long idChild = Long.parseLong(request.getParameter("idChild"));
		Long idParent = Long.parseLong(request.getParameter("idParent"));
			
		DependencyId idDep = new DependencyId(idParent,idChild);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("dependency", dependencyService.getDependencyById(idDep));
		mav.addObject("responsibilities", responsibilityService.listResponsibilitiesByArchietctureId(architecture.getId()));
		mav.setViewName("/editDependency.tiles");

		return mav;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/syncDependencies", method = RequestMethod.GET)
	public @ResponseBody ElementChangeList syncDependencies(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureReadOnlyAction(request);
		Long lastChangeNumber = Long.parseLong(request.getParameter("lastChangeNumber"));
		
		List<ElementChange> changes = elementChangeService.lastChangesListByElementType(architecture.getId(), lastChangeNumber,ElementType.DEPENDENCY.toString());
		ElementChangeList changesList = new ElementChangeList();
		changesList.setChanges(changes);
		return changesList;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/deleteDependency", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange deleteDependency(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long idChild = Long.parseLong(request.getParameter("idChild"));
		Long idParent = Long.parseLong(request.getParameter("idParent"));
				
		DependencyId idDep = new DependencyId(idParent,idChild);

		ElementChange change = dependencyService.delete(idDep,true,false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/insertDependency", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange insertDependency(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		Long idChild = Long.parseLong(request.getParameter("idChild"));
		Long idParent = Long.parseLong(request.getParameter("idParent"));
		String couplingcostString = request.getParameter("couplingcost");
		Double couplingcost = null;
		if (couplingcostString!=null && !couplingcostString.equals(""))
			couplingcost = Double.parseDouble(couplingcostString);
		else 
			couplingcost = new Double(Dependency.DEFAULT_COUPLING_COST);
					
		DependencyId idDep = new DependencyId(idParent,idChild);
		
		if (dependencyService.getDependencyById(idDep)!=null)
			return null;
		
		Responsibility parent = responsibilityService.getResponsibility(idParent);
		Responsibility child = responsibilityService.getResponsibility(idChild);
		Dependency newDependency = new Dependency(idDep, parent, child, couplingcost);
		newDependency.setArchitecture(architecture);
		
		ElementChange change = dependencyService.add(newDependency,true,false);
		return change;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/saveDependency", method = RequestMethod.POST)
	public synchronized @ResponseBody
	ElementChange saveDependency(HttpServletRequest request) throws Exception {
		validateArchitectureModificationAction(request);
		Long idChild = Long.parseLong(request.getParameter("idChild"));
		Long idParent = Long.parseLong(request.getParameter("idParent"));
		Double couplingcost = Double.parseDouble(request.getParameter("couplingcost"));
				
		DependencyId idDep = new DependencyId(idParent,idChild);
		Dependency depToUpdate = dependencyService.getDependencyById(idDep);
		
		Dependency oldDependency = depToUpdate.clone();
		oldDependency.setArchitecture(depToUpdate.getArchitecture());
		oldDependency.setChildResponsibility(depToUpdate.getChildResponsibility());
		oldDependency.setParentResponsibility(depToUpdate.getParentResponsibility());
		
		depToUpdate.setCouplingcost(couplingcost);

		ElementChange change = dependencyService.update(oldDependency,depToUpdate,true,false);
		return change;
	}

}
