package architect.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import architect.model.ElementChange;

@Repository
public class ElementChangeDAOImpl extends SessionFactoryDAO implements
		ElementChangeDAO {

	@Override
	@Transactional (readOnly = true)
	public ElementChange getElementChange(Long id) {
		return (ElementChange) sessionFactory.getCurrentSession()
				.createQuery("from ElementChange where id = " + id)
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<ElementChange> listElementChangeByElementType(
			String id_architecture, String elementType) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"from ElementChange where ( id_architecture = "
								+ id_architecture + " AND element_type = '"
								+ elementType + "') order by id asc").list();
	}

	@Override
	@Transactional (readOnly = true)
	public Long lastChangeNumberByElementType(Long id_architecture,
			String elementType) {
		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"from ElementChange where ( id_architecture = "
								+ id_architecture + " AND element_type = '"
								+ elementType
								+ "') order by change_number desc")
				.setMaxResults(1);
		ElementChange element = (ElementChange) query.uniqueResult();
		if (element != null)
			return element.getChange_number();
		return new Long(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<ElementChange> lastChangesListByElementType(
			Long id_architecture, Long changeNumber, String elementType) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"from ElementChange where ( id_architecture = "
								+ id_architecture + " AND change_number > "
								+ changeNumber + " AND element_type = '"
								+ elementType + "') order by change_number asc")
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<ElementChange> listConsistentStatusByArchitectureId(
			Long id_architecture) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"from ElementChange where ( id_architecture = "
								+ id_architecture + " AND consistent = 1 AND undo_change = 0 ) order by id desc").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<ElementChange> listElementChangeByArchitectureId(
			Long id_architecture) {
		return sessionFactory
				.getCurrentSession()
				.createQuery(
						"from ElementChange where ( id_architecture = "
								+ id_architecture + " AND undo_change = 0 ) order by id desc").list();
	}
	
	@Override
	@Transactional
	public Long addElementChange(ElementChange elementChange) {
		return (Long) sessionFactory.getCurrentSession().save(elementChange);
	}

	@Override
	@Transactional
	public void delete(ElementChange change) {
		sessionFactory.getCurrentSession().delete(change);
	}

}
