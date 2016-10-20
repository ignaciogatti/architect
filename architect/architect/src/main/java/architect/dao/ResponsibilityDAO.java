package architect.dao;

import java.util.List;

import architect.model.Responsibility;

public interface ResponsibilityDAO {
	
	List<Responsibility> listResponsibilitiesByArchietctureId(
			Long architectureId);

	Responsibility getResponsibility(Long responsibilityId);
	
	Long add(Responsibility newResponsibility);
	
	void delete(Responsibility respToDelete);

	void update(Responsibility responsibility);

	void saveOrUpdateResponsibility(Responsibility responsibility);
	
}
