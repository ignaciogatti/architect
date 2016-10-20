package architect.service.diagram;

import com.mxgraph.sharing.mxSharedState;

public interface DiagramService {

	mxSharedState getSharedDiagram(Long architectureId);

}
