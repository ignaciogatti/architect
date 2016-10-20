package architect.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import architect.model.Scenario;

@Repository
public class ScenarioDAOImpl extends SessionFactoryDAO implements ScenarioDAO {
	
	@Override
	@Transactional (readOnly = true)
	public Scenario getScenario(Long id) throws HibernateException  {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return (Scenario) session.createQuery(
				"from Scenario where id = " + id + "order by id asc").uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<Scenario> listScenariosByArchitectureId(Long id_architecture) throws HibernateException  {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return (List<Scenario>) session.createQuery("from Scenario where id_architecture = " + id_architecture + "order by id asc").list();
	}

	@Override
	@Transactional
	public Long add(Scenario scenario) throws HibernateException  {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return (Long) session.save(scenario);
	}
	
	@Override
	@Transactional
	public void delete(Scenario scenario) throws HibernateException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		session.delete(scenario);
	}

	@Override
	@Transactional
	public void update(Scenario scenario) throws HibernateException  {
		Scenario scenarioToUpdate = getScenario(scenario.getId());
		if (scenarioToUpdate!=null) {
//			scenarioToUpdate.setArchitectureAnalysis(scenario.getArchitectureAnalysis());
			scenarioToUpdate.setArtifact(scenario.getArtifact());
			scenarioToUpdate.setDescription(scenario.getDescription());
			scenarioToUpdate.setEnviroment(scenario.getEnviroment());
			scenarioToUpdate.setMeasure(scenario.getMeasure());
			scenarioToUpdate.setName(scenario.getName());
			scenarioToUpdate.setPriority(scenario.getPriority());
			scenarioToUpdate.setQualityAttribute(scenario.getQualityAttribute());
			scenarioToUpdate.setResponse(scenario.getResponse());
			scenarioToUpdate.setResponsibilities(scenario.getResponsibilities());
			scenarioToUpdate.setSource(scenario.getSource());
			scenarioToUpdate.setStimulus(scenario.getStimulus());
			sessionFactory.getCurrentSession().merge(scenarioToUpdate);
		}
	}

}
