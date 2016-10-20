package architect.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import architect.model.Responsibility;

@Repository
public class ResponsibilityDAOImpl extends SessionFactoryDAO implements
		ResponsibilityDAO {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<Responsibility> listResponsibilitiesByArchietctureId(
			Long architectureId) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"from Responsibility where id_architecture = "
								+ architectureId + " order by id asc").list();
	}

	@Override
	@Transactional (readOnly = true)
	public Responsibility getResponsibility(Long responsibilityId) {
		return (Responsibility) sessionFactory.getCurrentSession().createQuery(
				"from Responsibility where id = " + responsibilityId).uniqueResult();
	}

	@Override
	@Transactional
	public Long add(Responsibility newResponsibility) {
		return (Long) sessionFactory.getCurrentSession().save(newResponsibility);
	}
	
	@Override
	@Transactional
	public void delete(Responsibility respToDelete) {
		sessionFactory.getCurrentSession().delete(respToDelete);
	}

	@Override
	@Transactional
	public void update(Responsibility responsibility) {
		Responsibility responsibilityToUpdate = getResponsibility(responsibility.getId());
		if (responsibilityToUpdate!=null) {
			responsibilityToUpdate.setName(responsibility.getName());
			responsibilityToUpdate.setDescription(responsibility.getDescription());
			responsibilityToUpdate.setComplexity(responsibility.getComplexity());
			responsibilityToUpdate.setExecutionTime(responsibility.getExecutionTime());
			responsibilityToUpdate.setModule(responsibility.getModule());
			responsibilityToUpdate.setScenarios(responsibility.getScenarios());
//			responsibilityToUpdate.setDependenciesAsChild(responsibility.getDependenciesAsChild());
//			responsibilityToUpdate.setDependenciesAsParent(responsibility.getDependenciesAsParent());
			sessionFactory.getCurrentSession().merge(responsibilityToUpdate);
		}
	}

	@Override
	@Transactional
	public void saveOrUpdateResponsibility(Responsibility responsibility) {
		sessionFactory.getCurrentSession().saveOrUpdate(responsibility);
	}

}
