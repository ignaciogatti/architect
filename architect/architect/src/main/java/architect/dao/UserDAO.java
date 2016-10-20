package architect.dao;

import org.hibernate.HibernateException;

import architect.model.User;

public interface UserDAO {

	User findByName(String username) throws HibernateException ;
		
}
