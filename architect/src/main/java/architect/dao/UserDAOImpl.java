package architect.dao;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import architect.model.User;

@Repository
public class UserDAOImpl extends SessionFactoryDAO implements UserDAO {

	@Override
	public User findByName(String name) throws HibernateException {
		return (User) sessionFactory.getCurrentSession()
				.createQuery("from User where username = '" + name + "'").uniqueResult();
	}
}
