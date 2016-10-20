package architect.service.diagram;

import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.sharing.mxSharedGraphModel;
import com.mxgraph.sharing.mxSharedState;
import com.mxgraph.view.mxGraph;

import architect.graph.model.Delta;
import architect.graph.model.Edit;
import architect.graph.model.Message;
import architect.graph.model.mxCell;
import architect.graph.model.mxChange;
import architect.graph.model.mxChildChange;
import architect.graph.model.mxElement;
import architect.graph.model.mxGeometry;
import architect.graph.model.mxGeometryChange;
import architect.graph.model.mxTerminalChange;
import architect.model.Architecture;
import architect.model.Module;

public class DiagramUtils {
	
	public static void layoutDiagram(mxSharedState sharedDiagram, Architecture architecture) {
		for (Module module : architecture.getModules()) {
			String moduleId = "M" + module.getId();
			DiagramUtils.executeFastOrganicLayout(sharedDiagram,moduleId);
		}
		DiagramUtils.executeCircleLayout(sharedDiagram,"1");
	}
	
	public static void executeFastOrganicLayout(mxSharedState sharedDiagram, String cellId) {
		mxSharedGraphModel sharedGraphModel = (mxSharedGraphModel) sharedDiagram;
		com.mxgraph.model.mxCell cell = (com.mxgraph.model.mxCell) sharedGraphModel.getModel().getCell(cellId);
		mxGraph graph = new mxGraph(sharedGraphModel.getModel());
		mxIGraphLayout layout = new mxFastOrganicLayout(graph);
		graph.getModel().beginUpdate();
		layout.execute(cell);
		graph.getModel().endUpdate();
	}
	
	public static void executeCircleLayout(mxSharedState sharedDiagram, String cellId) {
		mxSharedGraphModel sharedGraphModel = (mxSharedGraphModel) sharedDiagram;
		com.mxgraph.model.mxCell cell = (com.mxgraph.model.mxCell) sharedGraphModel.getModel().getCell(cellId);
		mxGraph graph = new mxGraph(sharedGraphModel.getModel());
		mxIGraphLayout layout = new mxCircleLayout(graph);
		graph.getModel().beginUpdate();
		layout.execute(cell);
		graph.getModel().endUpdate();
	}

	public static Message insertElementMessage(mxElement element, String parent,
			Integer index, Integer x, Integer y, Integer width, Integer height,
			String name, String cellStyle, Integer cellVertex,
			Integer cellConnectable, String cellParent) {
		mxChildChange childChange = new mxChildChange(parent, index);
		mxCell cell = new mxCell(cellStyle, cellVertex, cellConnectable,
				cellParent);
		mxGeometry geometry = new mxGeometry(x, y, width, height, name);
		cell.setGeometry(geometry);
		element.setCell(cell);
		childChange.setElement(element);
		return editMessage(childChange,null);
	}
	
	public static Message deleteElementMessage(String previous, String child) {
		mxChildChange childChange = new mxChildChange();
		childChange.setPrevious(previous);
		childChange.setChild(child);
		return editMessage(childChange,null);
	}
	
	public static Message updateElementMessage(Integer cell, Integer x, Integer y, Integer width, Integer height,
			String name) {
		mxGeometryChange geometryChange = new mxGeometryChange();
		mxGeometry geometry = new mxGeometry(x, y, width, height, name);
		geometryChange.setGeometry(geometry);
		geometryChange.setCell(cell);
		return editMessage(geometryChange,null);
	}
	
	public static Message editMessage(mxChange change, List<mxTerminalChange> terminalChanges) {
		Message message = new Message();
		Delta delta = new Delta();
		Edit edit = new Edit();

		edit.setTerminalChanges(terminalChanges);
		edit.setChange(change);
		delta.setEdit(edit);
		message.setDelta(delta);
		return message;
	}

	public static String messageToXML(Message message) {
		String result = null;
		try {
			StringWriter stringWriter = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(Message.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(message, stringWriter);
			result = stringWriter
					.toString()
					.replace(
							"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>",
							"");
			result = result.replace("\n", "").replace("\r", "");
			result = result.replaceAll(" {2,}", "");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
