package architect.interceptor;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import architect.model.User;
import architect.service.UserService;

public class RequestInitializeInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserService userService;

	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			setUserAttribute(request);
			setArchitectureAttribute(request);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void setUserAttribute(HttpServletRequest request) throws Exception {
		Long userId = null;
		String requestParam = request.getParameter("user");
		if (requestParam != null) {
			userId = Long.parseLong(requestParam);
			request.getSession().setAttribute("user", userId);
		} else {
			Object userAttribute = request.getSession().getAttribute("user");
			if (userAttribute==null) {
				Principal userPricipal = request.getUserPrincipal();
				if (userPricipal != null && userPricipal.getName() != null && !userPricipal.getName().equals("")) {
					User user = userService.getUserByUserName(request.getUserPrincipal().getName());
					request.getSession().setAttribute("user", user.getId());
					request.getSession().setAttribute("userName", user.getUsername());
				}
			}
		}
	}

	private void setArchitectureAttribute(HttpServletRequest request)
			throws Exception {
		Long architectureId = null;
		String requestParamArchitecture = request.getParameter("architecture");
		if (requestParamArchitecture != null) {
			architectureId = Long.parseLong(requestParamArchitecture);
			request.getSession().setAttribute("architecture", architectureId);
		} else {
			Object archtectureAttribute = request.getSession().getAttribute("architecture");
			if (archtectureAttribute==null) {
			
			}
		}
	}

}
