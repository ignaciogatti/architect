package architect.service;

import java.util.List;

import architect.model.Tactic;

public interface TacticService {

	List<Tactic> listTacticsByQualityAttributeId(Long id_qualityAttribute);

	Tactic getTactic(Long id_tactic);
	
}
