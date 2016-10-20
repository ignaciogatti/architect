package architect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import architect.dao.ArchitectureAnalysisDAO;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.model.ArchitectureAnalysis;
import architect.model.ElementChange;

@Service("architectureAnalysisService")
public class ArchitectureAnalysisServiceImpl extends MessageProcessorServiceImpl implements ArchitectureAnalysisService {

	@Autowired
    private ArchitectureAnalysisDAO architectureAnalysisDAO;

	@Override
	public List<ArchitectureAnalysis> listArchitectureAnalysisByArchitectureId(Long architectureId) {
		return architectureAnalysisDAO.listArchitectureAnalysisByArchietctureId(architectureId);
	}
	
	@Override
	public ArchitectureAnalysis getArchitectureAnalysis(Long id_architectureAnalysis) {
		return architectureAnalysisDAO.getArchitectureAnalysis(id_architectureAnalysis);
	}
	
	@Override
	public ArchitectureAnalysis getArchitectureAnalysisEAGER(Long id_architectureAnalysis) {
		return architectureAnalysisDAO.getArchitectureAnalysisEAGER(id_architectureAnalysis);
	}

	@Override
	public ElementChange add(ArchitectureAnalysis architectureAnalysis, boolean undo) {
		Long addedArchitectureAnalysisId = architectureAnalysisDAO.add(architectureAnalysis);
		ArchitectureAnalysis addedArchitectureAnalysis = (ArchitectureAnalysis) this.getArchitectureAnalysis(addedArchitectureAnalysisId);
		ElementChange change = null;
		if (addedArchitectureAnalysis != null) {
			change = new ElementChange(architectureAnalysis.getArchitecture()
					.getId(), ElementType.ARCHITECTUREANALYSIS.toString(),
					ChangeType.INSERT.toString(), null,
					addedArchitectureAnalysis, new Boolean(true), new Boolean(
							undo));
			postChange(change,architectureAnalysisQueueTemplate);
		}
		return change;
	}

	@Override
	public ElementChange delete(Long architectureAnalysisId, boolean undo) {
		ArchitectureAnalysis architectureAnalysisToDelete = (ArchitectureAnalysis) this.getArchitectureAnalysis(architectureAnalysisId);
		architectureAnalysisDAO.delete(architectureAnalysisToDelete);
		ElementChange change = new ElementChange(architectureAnalysisToDelete
				.getArchitecture().getId(),
				ElementType.ARCHITECTUREANALYSIS.toString(),
				ChangeType.DELETE.toString(), architectureAnalysisToDelete,
				null, new Boolean(true), new Boolean(undo));
		postChange(change,architectureAnalysisQueueTemplate);
		return change;
	}

	@Override
	public ElementChange update(ArchitectureAnalysis oldArchitectureAnalysis, ArchitectureAnalysis newArchitectureAnalysis, boolean undo) {
		architectureAnalysisDAO.update(newArchitectureAnalysis);
		ElementChange change = new ElementChange(newArchitectureAnalysis
				.getArchitecture().getId(),
				ElementType.ARCHITECTUREANALYSIS.toString(),
				ChangeType.UPDATE.toString(), oldArchitectureAnalysis,
				newArchitectureAnalysis, new Boolean(true), new Boolean(undo));
		postChange(change,architectureAnalysisQueueTemplate);
		return change;
	}
	
	/**
	 * Architecture Analysis sync properties and methods
	 */

	@Autowired
	private JmsTemplate architectureAnalysisQueueTemplate;

	@Autowired
	private JmsTemplate architectureAnalysisTopicTemplate;

	@Override
	public void doProcessing(final String msg) {
		this.doProcessing(msg, architectureAnalysisTopicTemplate);
	}
	
}
