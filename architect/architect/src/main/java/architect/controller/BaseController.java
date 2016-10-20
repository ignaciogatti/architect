package architect.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import architect.error.BlockedArchitectureException;
import architect.error.ErrorMessage;
import architect.error.GenericArchitectureException;
import architect.error.InvalidArchitectureException;
import architect.model.Architecture;
import architect.service.ArchitectureService;

public class BaseController {

	@Autowired
	protected ArchitectureService architectureService;

	protected Architecture validateArchitectureReadOnlyAction(HttpServletRequest request) throws Exception {
		return validateArchitecture(request, false);
	}

	protected Architecture validateArchitectureModificationAction(HttpServletRequest request) throws Exception {
		return validateArchitecture(request, true);
	}

	private Architecture validateArchitecture(HttpServletRequest request, boolean modificationAction) throws Exception {
		Long architectureId = (Long) request.getSession().getAttribute("architecture");
		String username = request.getUserPrincipal().getName();
		Architecture architecture = architectureService.validateArchitecture(architectureId, username, modificationAction);
		request.getSession().setAttribute("group", architecture.getGroup().getId());
		return architecture;
	}

	@ExceptionHandler(InvalidArchitectureException.class)
	public String handleInvalidArchitectureException(InvalidArchitectureException ex) {
		return "redirect:/";
	}
	
	@ExceptionHandler(BlockedArchitectureException.class)
	public @ResponseBody ErrorMessage handleBlockedArchitectureException(BlockedArchitectureException ex) {
		return new ErrorMessage(ex.getMessage());
	}
	
	@ExceptionHandler(GenericArchitectureException.class)
	public @ResponseBody ErrorMessage handleGenericArchitectureException(GenericArchitectureException ex) {
		return new ErrorMessage(ex.getMessage());
	}

}
