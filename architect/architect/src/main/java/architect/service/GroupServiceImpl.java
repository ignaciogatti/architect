package architect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import architect.dao.GroupDAO;
import architect.model.Group;

@Service("groupService")
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupDAO groupDAO;

	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	@Override
	@Transactional(readOnly = true)
	public Group getGroupById(Long id) {
		return groupDAO.getGroupById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Group getGroupByUsername(String username) {
		return groupDAO.getGroupByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Group> listGroups() {
		return groupDAO.listGroups();
	}

}