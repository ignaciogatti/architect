package architect.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import architect.dao.UserDAO;
import architect.model.User;

@Service("userService") 
public class UserServiceImpl implements UserService {

  @Autowired 
  private UserDAO userDAO;
  
  public void setUserDAO(UserDAO userDAO){
	  this.userDAO = userDAO;
  }

  @Override
  @Transactional(readOnly = true)
  public User getUserByUserName(String username) {
	  	return userDAO.findByName(username);
  }
  
}