package architect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import architect.dao.TacticDAO;
import architect.model.Tactic;

@Service("tacticService")
public class TacticServiceImpl implements TacticService {

	@Autowired
    private TacticDAO tacticDAO;

	@Override
	public List<Tactic> listTacticsByQualityAttributeId(Long id_qualityAttribute) {
		return tacticDAO.listTacticsByQualityAttributeId(id_qualityAttribute);
	}

	@Override
	public Tactic getTactic(Long id_tactic) {
		return tacticDAO.getTactic(id_tactic);
	}

}
