package architect.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import architect.model.Architecture;
import architect.model.ArchitectureAnalysis;
import architect.model.Dependency;
import architect.model.DesignBot;
import architect.model.Module;
import architect.model.QualityAttribute;
import architect.model.Responsibility;
import architect.model.Scenario;

@Repository
public class ArchitectureAnalysisDAOImpl extends SessionFactoryDAO implements ArchitectureAnalysisDAO {

	@SuppressWarnings("unchecked")
	@Override
	@Transactional (readOnly = true)
	public List<ArchitectureAnalysis> listArchitectureAnalysisByArchietctureId(Long architectureId) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return session.createQuery(
						"from ArchitectureAnalysis where id_architecture = " + architectureId
								+ " order by id asc").list();
	}

	@Override
	@Transactional(readOnly = true)
	public ArchitectureAnalysis getArchitectureAnalysis(Long id_architectureAnalysis) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		ArchitectureAnalysis architectureAnalysis = (ArchitectureAnalysis) session.createQuery(
				"from ArchitectureAnalysis where id = " + id_architectureAnalysis).uniqueResult();
		return architectureAnalysis;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ArchitectureAnalysis getArchitectureAnalysisEAGER(Long id_architectureAnalysis) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		ArchitectureAnalysis architectureAnalysis = (ArchitectureAnalysis) session.createQuery(
				"from ArchitectureAnalysis where id = " + id_architectureAnalysis).uniqueResult();
		initializaAllObjects(architectureAnalysis);
		return architectureAnalysis;
	}

	@Override
	@Transactional
	public Long add(ArchitectureAnalysis architectureAnalysis) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		return (Long) session.save(architectureAnalysis);
	}

	@Override
	@Transactional
	public void delete(ArchitectureAnalysis architectureAnalysis) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		session.delete(architectureAnalysis);
	}

	@Override
	@Transactional
	public void update(ArchitectureAnalysis architectureAnalysis) {
		Session session = SessionFactoryUtils.getSession(sessionFactory, Boolean.FALSE);
		session.update(architectureAnalysis);
	}
	
	private void initializaAllObjects(ArchitectureAnalysis architectureAnalysis) {		
		Architecture architecture = architectureAnalysis.getArchitecture();
		Hibernate.initialize(architecture);
		Set<Dependency> dependencies = architecture.getDependencies();
		Hibernate.initialize(dependencies);
		for (Dependency dependency : dependencies) {
			Hibernate.initialize(dependency);
		}
		Set<Module> modules = architecture.getModules();
		Hibernate.initialize(modules);
		for (Module moduleA : modules) {
			Hibernate.initialize(moduleA);
			Set<Responsibility> responsibilitiesM = moduleA.getResponsibilities();
			Hibernate.initialize(responsibilitiesM);
			for (Responsibility responsibilityM : responsibilitiesM) {
				Hibernate.initialize(responsibilityM);
			}
		}
		Set<Responsibility> responsibilities = architecture.getResponsibilities();
		Hibernate.initialize(responsibilities);
		for (Responsibility responsibility : responsibilities) {
			Hibernate.initialize(responsibility);
			Set<Dependency> dependenciesC = responsibility.getDependenciesAsChild();
			Hibernate.initialize(dependenciesC);
			for (Dependency dependencyC : dependenciesC) {
				Hibernate.initialize(dependencyC);
			}
			Set<Dependency> dependenciesP = responsibility.getDependenciesAsParent();
			Hibernate.initialize(dependenciesP);
			for (Dependency dependencyP : dependenciesP) {
				Hibernate.initialize(dependencyP);
			}
		}
		Set<Scenario> scenarios = architecture.getScenarios();
		Hibernate.initialize(scenarios);
		for (Scenario scenarioA : scenarios) {
			Hibernate.initialize(scenarioA);
			Set<Responsibility> responsibilitiesS = scenarioA.getResponsibilities();
			Hibernate.initialize(responsibilitiesS);
			for (Responsibility responsibilityS : responsibilitiesS) {
				Hibernate.initialize(responsibilityS);
			}
		}
		
		Scenario scenario = architectureAnalysis.getScenario();
		Hibernate.initialize(scenario);
		
		DesignBot designBot = architectureAnalysis.getDesignBot();
		Hibernate.initialize(designBot);
		Hibernate.initialize(designBot.getId());
		Hibernate.initialize(designBot.getName());
		Architecture architectureD = designBot.getArchitecture();
		Hibernate.initialize(architectureD);
		QualityAttribute qualityAttribute = designBot.getQualityAttribute();
		Hibernate.initialize(qualityAttribute);
	}

}
