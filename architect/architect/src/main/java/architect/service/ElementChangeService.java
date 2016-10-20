package architect.service;

import java.util.List;

import architect.model.ElementChange;

public interface ElementChangeService {

	ElementChange getElementChange(Long id);
	
	Long addElementChange(ElementChange elementChange);
	
	List<ElementChange> listElementChangeByElementType(String id_architecture, String elementType);
	
	Long lastChangeNumberByElementType(Long id_architecture, String elementType);

	List<ElementChange> lastChangesListByElementType(Long id_architecture, Long changeNumber, String elementType);

	List<ElementChange> listConsistentStatusByArchitectureId(Long id_architecture);
	
	List<ElementChange> listElementChangeByArchitectureId(Long id_architecture);

	void delete(ElementChange change);
	
	void applyElementsChangesList(Long architectureId, List<ElementChange> changeslist) throws Exception;
	
	void revertChanges(Long id_architecture, Long lastChangeToRevert);
	
	ElementChange appplyElementChange(ElementChange change);

}
