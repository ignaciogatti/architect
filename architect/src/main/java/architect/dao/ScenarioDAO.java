package architect.dao;

import java.util.List;

import org.hibernate.HibernateException;

import architect.model.Scenario;

public interface ScenarioDAO {

	Scenario getScenario(Long id) throws HibernateException ;
	
	List<Scenario> listScenariosByArchitectureId(Long id) throws HibernateException ;
	
	Long add(Scenario scenario) throws HibernateException ;

	void delete(Scenario scenario) throws HibernateException ;

	void update(Scenario scenario) throws HibernateException ;
	
}
