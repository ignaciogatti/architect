package architect.service;

import java.util.List;

import architect.model.ElementChange;
import architect.model.Responsibility;

public interface ResponsibilityService extends MessageProcessorService {

	List<Responsibility> listResponsibilitiesByArchietctureId(Long architectureId);
		
	Responsibility getResponsibility(Long responsibilityId);

	ElementChange add(Responsibility newResponsibility, boolean consistentState, boolean undo);
	
	ElementChange delete(Long responsibilityId, boolean consistentState, boolean undo);
	
	ElementChange update(Responsibility oldResponsibility, Responsibility newResponsibility, boolean consistentState, boolean undo);
		
}
