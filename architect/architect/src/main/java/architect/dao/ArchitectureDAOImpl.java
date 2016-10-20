package architect.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import architect.model.Architecture;

@Repository
public class ArchitectureDAOImpl extends SessionFactoryDAO implements ArchitectureDAO {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<Architecture> listArchitectures() {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return session.createQuery("from Architecture order by id asc").list();
	}

	@Override
	@Transactional (readOnly = true)
	public Architecture getArchitectureById(Long id) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return (Architecture) session.createQuery("from Architecture where id = " + id)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<Architecture> getArchitectureByGroupId(Long id) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return session.createQuery("from Architecture where id_group = " + id + " order by id asc").list();
	}

	@Override
	@Transactional
	public void deleteArchitecture(Architecture architecture) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		session.delete(architecture);
	}

	@Override
	@Transactional
	public void updateArchitecture(Architecture architecture) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		session.update(architecture);
	}

	@Override
	@Transactional
	public Long addArchitecture(Architecture newArchitecture) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return (Long) session.save(newArchitecture);
	}

	@Override
	@Transactional
	public int blockArchitecture(Long architectureId, String blockReason) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		Query query = session.createQuery("update Architecture set blocked = 1" +
				" where id = " + architectureId);
		return query.executeUpdate();
	}

	@Override
	@Transactional
	public int unblockArchitecture(Long architectureId) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		Query query = session.createQuery("update Architecture set blocked = 0" +
				" where id = " + architectureId);
		return query.executeUpdate();
	}
}
