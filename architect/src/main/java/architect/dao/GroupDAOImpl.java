package architect.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import architect.model.Group;

@Repository
public class GroupDAOImpl extends SessionFactoryDAO implements GroupDAO {

	@Override
	public Group getGroupById(Long id) {
		return (Group) sessionFactory.getCurrentSession()
				.createQuery("from Group where id = " + id).uniqueResult();
	}

	@Override
	public Group getGroupByUsername(String username) {
		return (Group) sessionFactory.getCurrentSession()
				.createQuery("from Group where groupname = '" + username + "'").uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Group> listGroups() {
		return sessionFactory.getCurrentSession().createQuery("from Group").list();
	}
}
