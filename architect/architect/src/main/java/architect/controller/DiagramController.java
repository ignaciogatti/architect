package architect.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;

import architect.error.InvalidArchitectureException;
import architect.service.diagram.DiagramService;

import com.mxgraph.sharing.mxSession;
import com.mxgraph.sharing.mxSharedState;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;

@Controller
public class DiagramController extends BaseController {
	
	protected static String SESSION_ID = "MXSESSIONID";
	
	@Autowired
	private DiagramService diagramService;

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/diagram", method = RequestMethod.GET)
	public ModelAndView diagram(HttpServletRequest request) throws Exception {
		validateArchitectureReadOnlyAction(request);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/diagram.tiles");
		return mav;
	}

	@RequestMapping(value = "/diagramConfig", method = RequestMethod.GET)
	public @ResponseBody
	String diagramConfig(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		ServletContext sc = session.getServletContext();
		String basePath = sc.getRealPath("/");
		return mxUtils.readFile(basePath
				+ "resources/mxgraph/config/diagrameditor-backend.xml");
	}

	@RequestMapping(value = "/diagramStart", method = RequestMethod.GET)
	public ModelAndView diagramStart(HttpServletRequest request)
			throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("diagrameditor");
		return mav;
	}

	@RequestMapping(value = "/diagramInit", method = RequestMethod.GET)
	public @ResponseBody
	String diagramInit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		mxSession session = getSession(request);
		String xml = session.init();
		return xml;
	}

	@RequestMapping(value = "/Share", method = RequestMethod.POST)
	public @ResponseBody
	String SharePOST(HttpServletRequest request)
			throws UnsupportedEncodingException, InvalidArchitectureException {

		String xml = URLDecoder.decode(request.getParameter("xml"), "UTF-8");
		mxSession session = getSession(request);

		Document doc = mxXmlUtils.parseXml(xml);
		session.receive(doc.getDocumentElement());

		return "OK";
	}

	@RequestMapping(value = "/Share", method = RequestMethod.GET)
	public @ResponseBody
	String ShareGET(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, InvalidArchitectureException {
		
		mxSession session = getSession(request);
		try {
			String xml = session.poll();
			return xml;
		} catch (InterruptedException e) {
			throw new ServletException(e);
		}
		
	}

	protected mxSession getSession(HttpServletRequest req) throws InvalidArchitectureException {
		//TODO validar la arquitectura?? performance
		Long id_architecture = (Long) req.getSession().getAttribute("architecture");
		if (id_architecture==null)
			throw new InvalidArchitectureException("No Architecture Id Found.");
		
		HttpSession httpSession = req.getSession(true);
		mxSession session = (mxSession) httpSession.getAttribute(SESSION_ID);

		if (session == null) {
			session = new mxBoundSession(httpSession.getId(), diagramService.getSharedDiagram(id_architecture));
			httpSession.setAttribute(SESSION_ID, session);
			httpSession.setMaxInactiveInterval(20);
		}

		return session;
	}
	
	public class mxBoundSession extends mxSession implements
			HttpSessionBindingListener {

		public mxBoundSession(String id, mxSharedState diagram) {
			super(id, diagram);
		}

		public void valueBound(HttpSessionBindingEvent arg0) {

		}

		public void valueUnbound(HttpSessionBindingEvent arg0) {
			destroy();
		}

	}

}
