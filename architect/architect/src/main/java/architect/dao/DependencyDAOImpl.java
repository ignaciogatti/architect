package architect.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import architect.model.Dependency;
import architect.model.DependencyId;

@Repository
public class DependencyDAOImpl extends SessionFactoryDAO implements
		DependencyDAO {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<Dependency> listDependenciesByArchietctureId(
			Long architectureId) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						" from Dependency dep where (dep.parentResponsibility  in (select resp.id from Responsibility resp where resp.architecture=" + architectureId +
						" )) or (dep.childResponsibility in (select resp.id from Responsibility resp where resp.architecture=" + architectureId + "))").list();
	}

	@Override
	@Transactional (readOnly = true)
	public Dependency getDependencyById(DependencyId idDep) {
		return (Dependency) sessionFactory.getCurrentSession().createQuery(
				"from Dependency where childResponsibility=" + idDep.getChild() + " and parentResponsibility=" + idDep.getParent()).uniqueResult();
	}

	@Override
	@Transactional
	public DependencyId add(Dependency dependency) {
		return (DependencyId) sessionFactory.getCurrentSession().save(dependency);
	}
	
	@Override
	@Transactional
	public void update(Dependency dependency) {
		Dependency dependencyToUpdate = getDependencyById(dependency.getId());
		dependencyToUpdate.setCouplingcost(dependency.getCouplingcost());
		sessionFactory.getCurrentSession().update(dependencyToUpdate);
	}

	@Override
	@Transactional
	public void delete(Dependency dependency) {
		sessionFactory.getCurrentSession().delete(dependency);
	}

}
