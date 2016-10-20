package architect.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import architect.model.DesignBot;

@Repository
public class DesignBotDAOImpl extends SessionFactoryDAO implements DesignBotDAO {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<DesignBot> listDesignBotsByArchietctureId(Long architectureId) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return session.createQuery(
						"from DesignBot where id_architecture = " + architectureId
								+ " order by id asc").list();
	}

	@Override
	@Transactional (readOnly = true)
	public DesignBot getDesignBot(Long id_designBot) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return (DesignBot) session.createQuery("from DesignBot where id = " + id_designBot)
				.uniqueResult();
	}

	@Override
	@Transactional
	public Long add(DesignBot designBot) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return (Long) session.save(designBot);
	}

	@Override
	@Transactional
	public void delete(DesignBot designBot) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		session.delete(designBot);
	}

	@Override
	@Transactional
	public void update(DesignBot designBot) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		session.update(designBot);
	}

}
