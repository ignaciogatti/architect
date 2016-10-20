package architect.service.diagram;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import architect.model.Architecture;
import architect.model.Dependency;
import architect.model.Module;
import architect.model.Responsibility;
import architect.service.ArchitectureService;

import com.mxgraph.io.mxCodecRegistry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.sharing.mxSharedGraphModel;
import com.mxgraph.sharing.mxSharedState;

@Service("diagramService")
public class DiagramServiceImpl implements DiagramService {

	public static HashMap<Long,mxSharedState> diagrams = new HashMap<Long, mxSharedState>();
	
	@Autowired
	private ArchitectureService architectureService;
	
	@Autowired
	private DiagramModuleService diagramModuleService;
	
	@Autowired
	private DiagramResponsibilityService diagramResponsibilityService;
	
	@Autowired
	private DiagramDependencyService diagramDependencyService;
	
	static {
		mxCodecRegistry.addAlias("architect.service.diagram.DiagramServiceImpl$1",
				"mxGraphModel");
	}
	
	@Override
	public mxSharedState getSharedDiagram(Long architectureId) {
		mxSharedState sharedDiagram = diagrams.get(architectureId);
		if (sharedDiagram==null) {
			synchronized (this) {
				sharedDiagram = diagrams.get(architectureId);
				if (sharedDiagram==null) {
					sharedDiagram = new mxSharedGraphModel(new mxGraphModel());
					diagrams.put(architectureId, sharedDiagram);
					initArchitectureDiagram(architectureId, sharedDiagram);
					return sharedDiagram;
				}
				return sharedDiagram;
			}
		} 
		return sharedDiagram;
	}
	
	private void initArchitectureDiagram(Long architectureId, mxSharedState sharedDiagram) {
		Architecture architecture = architectureService.getArchitectureById(architectureId);
		
		for (Module module : architecture.getModules()) {
			diagramModuleService.add(module);
			
			for (Responsibility responsibility : module.getResponsibilities()) {
				diagramResponsibilityService.add(responsibility);
			}
		}
		
		for (Dependency dependency : architecture.getDependencies()) {
			diagramDependencyService.add(dependency);
		}
		
		DiagramUtils.layoutDiagram(sharedDiagram, architecture);
	}
	
}
