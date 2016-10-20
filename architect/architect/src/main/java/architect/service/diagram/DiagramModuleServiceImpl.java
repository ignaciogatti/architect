package architect.service.diagram;

import org.springframework.stereotype.Service;

import architect.graph.model.Container;
import architect.graph.model.Message;
import architect.graph.model.mxElement;
import architect.model.Module;

import com.mxgraph.sharing.mxSession;
import com.mxgraph.sharing.mxSharedState;
import com.mxgraph.util.mxXmlUtils;

@Service("diagramModuleService")
public class DiagramModuleServiceImpl implements DiagramModuleService {

	@Override
	public void add(Module module) {
		mxSharedState sharedDiagram = DiagramServiceImpl.diagrams.get(module.getArchitecture().getId());
		if (sharedDiagram!=null) {
			mxSession session = new mxSession("id1", sharedDiagram);
			String moduleId = "M" + module.getId();
			mxElement moduleElement = new Container(moduleId, module.getName(), "");
			Message moduleMessage = DiagramUtils.insertElementMessage(moduleElement, "1", 0, 0, 0, 200, 200, "geometry", "swimlane", 1, 0, "1");
			session.receive(mxXmlUtils.parseXml(DiagramUtils.messageToXML(moduleMessage)).getDocumentElement());
			DiagramUtils.layoutDiagram(sharedDiagram, module.getArchitecture());
		}
	}
	
	@Override
	public void delete(Module module) {
		mxSharedState sharedDiagram = DiagramServiceImpl.diagrams.get(module.getArchitecture().getId());
		if (sharedDiagram!=null) {
			mxSession session = new mxSession("id1", sharedDiagram);
			String moduleId = "M" + module.getId();
			Message moduleMessage = DiagramUtils.deleteElementMessage("1",moduleId);
			session.receive(mxXmlUtils.parseXml(DiagramUtils.messageToXML(moduleMessage)).getDocumentElement());
		}
	}
	
}
