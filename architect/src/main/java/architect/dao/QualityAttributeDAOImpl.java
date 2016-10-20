package architect.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import architect.model.QualityAttribute;

@Repository
public class QualityAttributeDAOImpl extends SessionFactoryDAO implements QualityAttributeDAO {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<QualityAttribute> listQualityAttributes() {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return session.createQuery(
						"from QualityAttribute").list();
	}

	@Override
	@Transactional (readOnly = true)
	public QualityAttribute getQualityAttribute(Long id_qualityAttribute) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return (QualityAttribute) session.createQuery("from QualityAttribute where id = " + id_qualityAttribute)
				.uniqueResult();
	}
	

}
