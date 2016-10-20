package architect.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import architect.model.Module;

@Repository
public class ModuleDAOImpl extends SessionFactoryDAO implements ModuleDAO {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<Module> listModulesByArchietctureId(Long architectureId) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"from Module where id_architecture = " + architectureId
								+ " order by id asc").list();
	}

	@Override
	@Transactional (readOnly = true)
	public Module getModule(Long id_module) {
		return (Module) sessionFactory.getCurrentSession()
				.createQuery("from Module where id = " + id_module)
				.uniqueResult();
	}

	@Override
	@Transactional
	public Long add(Module module) {
		return (Long) sessionFactory.getCurrentSession().save(module);
	}

	@Override
	@Transactional
	public void delete(Module module) {
		sessionFactory.getCurrentSession().delete(module);
	}

	@Override
	@Transactional
	public void update(Module module) {
		sessionFactory.getCurrentSession().update(module);
	}

}
