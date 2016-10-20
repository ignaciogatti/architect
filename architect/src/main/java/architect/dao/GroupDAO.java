package architect.dao;

import java.util.List;

import architect.model.Group;

public interface GroupDAO {

	Group getGroupById(Long id);

	Group getGroupByUsername(String username);

	List<Group> listGroups();
		
}
