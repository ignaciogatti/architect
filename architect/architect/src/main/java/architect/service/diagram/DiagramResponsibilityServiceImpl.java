package architect.service.diagram;

import org.springframework.stereotype.Service;

import architect.graph.model.Message;
import architect.graph.model.Shape;
import architect.graph.model.mxElement;
import architect.model.Responsibility;

import com.mxgraph.sharing.mxSession;
import com.mxgraph.sharing.mxSharedState;
import com.mxgraph.util.mxXmlUtils;

@Service("diagramResponsibilityService")
public class DiagramResponsibilityServiceImpl implements DiagramResponsibilityService {

	@Override
	public void add(Responsibility responsibility) {
		mxSharedState sharedDiagram = DiagramServiceImpl.diagrams.get(responsibility.getArchitecture().getId());
		if (sharedDiagram!=null) {
			mxSession session = new mxSession("id1", sharedDiagram);
			String responsibilityId = "R" + responsibility.getId();
			String moduleId = "M" + responsibility.getModule().getId();
			mxElement responsibilityElement = new Shape(responsibilityId, responsibility.getName(), "");
			Message responsibilityMessage = DiagramUtils.insertElementMessage(responsibilityElement, moduleId, 0, 10, 10, 50, 50, "geometry", "ellipse", 1, 1, moduleId);
			session.receive(mxXmlUtils.parseXml(DiagramUtils.messageToXML(responsibilityMessage)).getDocumentElement());
			
			DiagramUtils.executeFastOrganicLayout(sharedDiagram,moduleId);
		}
	}
	
	@Override
	public void delete(Responsibility responsibility) {
		mxSharedState sharedDiagram = DiagramServiceImpl.diagrams.get(responsibility.getArchitecture().getId());
		if (sharedDiagram!=null) {
			mxSession session = new mxSession("id1", sharedDiagram);
			String moduleId = "M" + responsibility.getModule().getId();
			String responsibilityId = "R" + responsibility.getId();
			Message moduleMessage = DiagramUtils.deleteElementMessage(moduleId,responsibilityId);
			session.receive(mxXmlUtils.parseXml(DiagramUtils.messageToXML(moduleMessage)).getDocumentElement());
		}
	}
	
}
