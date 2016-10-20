package architect.service;

import java.util.List;

import architect.model.Group;

public interface GroupService {

	Group getGroupById(Long id);
	
	Group getGroupByUsername(String username);

	List<Group> listGroups();
}
