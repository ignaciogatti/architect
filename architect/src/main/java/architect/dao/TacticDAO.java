package architect.dao;

import java.util.List;

import architect.model.Tactic;

public interface TacticDAO {
	
	List<Tactic> listTacticsByQualityAttributeId(Long id_qualityAttribute);

	Tactic getTactic(Long id_tactic);
	
}
