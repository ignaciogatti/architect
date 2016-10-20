package architect.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import architect.model.Tactic;

@Repository
public class TacticDAOImpl extends SessionFactoryDAO implements TacticDAO {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<Tactic> listTacticsByQualityAttributeId(Long id_qualityAttribute) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"from Tactic where id_quality_attribute=" + id_qualityAttribute).list();
	}

	@Override
	@Transactional (readOnly = true)
	public Tactic getTactic(Long id_tactic) {
		return (Tactic) sessionFactory.getCurrentSession()
				.createQuery("from Tactic where id = " + id_tactic)
				.uniqueResult();
	}
	

}
