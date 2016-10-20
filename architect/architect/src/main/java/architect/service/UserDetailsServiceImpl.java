package architect.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;    
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import architect.dao.UserDAO;

@Service("userDetailsService") 
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired 
  private UserDAO userDAO;
  
  public void setUserDAO(UserDAO userDAO){
	  this.userDAO = userDAO;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException, DataAccessException {

    architect.model.User userEntity = userDAO.findByName(username);
    
    if (userEntity == null)
      throw new UsernameNotFoundException("User not found");

    return buildUserFromUserEntity(userEntity);
  }
  
  private User buildUserFromUserEntity(architect.model.User userEntity) {

	    String username = userEntity.getUsername();
	    String password = userEntity.getPassword();
	    boolean enabled = userEntity.getEnabled();
	    boolean accountNonExpired = !userEntity.getAccount_expired();
	    boolean credentialsNonExpired = !userEntity.getCredentials_expired();
	    boolean accountNonLocked = !userEntity.getAccount_locked();

	    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//	    for (SecurityRoleEntity role : userEntity.getRoles()) {
//	      authorities.add(new GrantedAuthorityImpl(role.getRoleName()));
//	    }

	    return new User(username, password, enabled,
	  	      accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	  }
}