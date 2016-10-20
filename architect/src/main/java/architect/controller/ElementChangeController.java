package architect.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import architect.model.Architecture;
import architect.service.ElementChangeService;

@Controller
public class ElementChangeController extends BaseController {
	
	@Autowired
	private ElementChangeService elementChangeService;
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/undoChanges", method = RequestMethod.POST)
	public synchronized @ResponseBody
	String undoChanges(HttpServletRequest request) throws Exception {
		Architecture architecture = validateArchitectureModificationAction(request);
		Long lastChangeToRevert = Long.parseLong(request.getParameter("change"));
		
		if (lastChangeToRevert!=null) {
			elementChangeService.revertChanges(architecture.getId(),lastChangeToRevert);
		}
		return "OK";
	}

}
