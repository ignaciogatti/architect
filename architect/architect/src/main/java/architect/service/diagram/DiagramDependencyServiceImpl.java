package architect.service.diagram;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import architect.graph.model.Connector;
import architect.graph.model.Message;
import architect.graph.model.mxCell;
import architect.graph.model.mxChildChange;
import architect.graph.model.mxElement;
import architect.graph.model.mxGeometry;
import architect.graph.model.mxTerminalChange;
import architect.model.Dependency;

import com.mxgraph.sharing.mxSession;
import com.mxgraph.sharing.mxSharedState;
import com.mxgraph.util.mxXmlUtils;

@Service("diagramDependencyService")
public class DiagramDependencyServiceImpl implements DiagramDependencyService {

	@Override
	public void add(Dependency dependency) {
		mxSharedState sharedDiagram = DiagramServiceImpl.diagrams.get(dependency.getArchitecture().getId());
		if (sharedDiagram!=null) {
			mxSession session = new mxSession("id1", sharedDiagram);
			String dependencyId = "D" + dependency.getId().getParent() + "-" + dependency.getId().getChild();
			String parentId = "R" + dependency.getId().getParent();
			String childId = "R" + dependency.getId().getChild();
			mxElement dependencyElement = new Connector(dependencyId, "", "");
			Message responsibilityMessage = insertConnectorMessage(dependencyElement, "1", 2, 1, "geometry", 1, "1", parentId, childId);
			session.receive(mxXmlUtils.parseXml(DiagramUtils.messageToXML(responsibilityMessage)).getDocumentElement());
		}
	}
	
	@Override
	public void delete(Dependency dependency) {
		mxSharedState sharedDiagram = DiagramServiceImpl.diagrams.get(dependency.getArchitecture().getId());
		if (sharedDiagram!=null) {
			mxSession session = new mxSession("id1", sharedDiagram);
			String dependencyId = "D" + dependency.getId().getParent() + "-" + dependency.getId().getChild();
			Message moduleMessage = DiagramUtils.deleteElementMessage("1",dependencyId);
			session.receive(mxXmlUtils.parseXml(DiagramUtils.messageToXML(moduleMessage)).getDocumentElement());
		}
	}
	
	private Message insertConnectorMessage(mxElement element, String parent,
			Integer index, Integer relative, String name, Integer cellEgde,
			String cellParent, String cellSource, String cellTarget) {
		mxChildChange childChange = new mxChildChange(parent, index);
		mxGeometry geometry = new mxGeometry(relative, name);
		mxCell cell = new mxCell(cellEgde, cellParent, cellSource, cellTarget);
		cell.setGeometry(geometry);
		element.setCell(cell);
		childChange.setElement(element);
		
		List<mxTerminalChange> teminals = new ArrayList<mxTerminalChange>();
		teminals.add(new mxTerminalChange(element.getId(), cellSource, 1));
		teminals.add(new mxTerminalChange(element.getId(), cellTarget, 0));
		return DiagramUtils.editMessage(childChange,null);
	}
	
}
