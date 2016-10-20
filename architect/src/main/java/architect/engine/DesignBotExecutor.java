package architect.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.transaction.annotation.Transactional;

import architect.engine.architecture.AnalysisResults;
import architect.engine.architecture.ArchitectureResults;
import architect.engine.architecture.DesignBotResults;
import architect.engine.architecture.TacticChange;
import architect.engine.architecture.algorithms.Predictive;
import architect.engine.architecture.algorithms.TrackAlgorithm;
import architect.model.Architecture;
import architect.model.ArchitectureAnalysis;
import architect.model.Scenario;
import architect.model.Tactic;

public abstract class DesignBotExecutor extends Thread {

	boolean stopped = false;
	TrackAlgorithm trackAlgorithm = new Predictive();
	ArchitectureAnalysis architectureAnalysis;
	AnalysisResults architectureAnalysisResults;
	private static int BACKTRACKING_LIMIT = 4;
	
	protected DesignBotExecutor() {
	}
	
	protected DesignBotExecutor(ArchitectureAnalysis architectureAnalysis) {
		this.architectureAnalysis = architectureAnalysis;
	}
	
	public abstract Double getScenarioCost(Scenario scenario);
	
	protected abstract Architecture selectBestArchitecture(
			Architecture bestArchi, Architecture backArchi, Scenario scenario,
			List<TacticChange> bestChanges, List<TacticChange> localChanges);
	
	@Override
	
	@Transactional
	public void run() {
		long startTime = System.nanoTime();
		List<TacticChange> changes = new ArrayList<TacticChange>();
		List<Tactic> tactics = new ArrayList<Tactic>(architectureAnalysis.getDesignBot().getTactics());
		Architecture resultArchitecture = backtracking(0, this.trackAlgorithm, tactics, this.architectureAnalysis.getScenario(), changes, true, true);
		
		if (architectureAnalysisResults!=null) {
			ArchitectureResults architectureResults = new ArchitectureResults(resultArchitecture,architectureAnalysisResults.getActualArchitectureResult());
			
			DesignBotResults designBotResults = new DesignBotResults();
			designBotResults.setArchitectureAnalysis(architectureAnalysis);
			designBotResults.setChanges(changes);
			designBotResults.setArchitecture(resultArchitecture);
			designBotResults.setArchitectureResults(architectureResults);
		
			architectureAnalysisResults.addDesignBotResult(designBotResults);	
		}
		long runningTime = System.nanoTime() - startTime;
		System.out.println("Total Running Time : " + TimeUnit.MILLISECONDS.convert(runningTime, TimeUnit.NANOSECONDS) + " milliseconds.");
	}
	
	public void stopRun(){
		this.stopped = true;
	}
	
	private Architecture backtracking(int backtrackingLevel, TrackAlgorithm trackAlgorithm, List<Tactic> tacticsList, Scenario scenario,
			List<TacticChange> changes, boolean applyAllTactics, boolean forceOneResult) {

		if (stopped)
			return scenario.getArchitecture();
		Architecture bestArchi = scenario.getArchitecture();
		List<TacticChange> bestArchiChanges = new ArrayList<TacticChange>();
		bestArchiChanges.addAll(changes);
		
		if (backtrackingLevel <= BACKTRACKING_LIMIT){
			for (Tactic tactic : tacticsList) {
				List<TacticChange> localChanges = new ArrayList<TacticChange>();
				localChanges.addAll(changes);
				
				TacticExecutor tacticExecutor = tactic.getTacticExecutor();
				List<Tactic> tacticsSubList = new ArrayList<Tactic>();
				
				TacticChange tacticChange = new TacticChange();
				tacticChange.setTacticName(tactic.getName());
				System.out.println("Applied Tactic: " + tactic.getName() + ", Level: " + backtrackingLevel);
				long startTime = System.nanoTime();
				Architecture archiActual = trackAlgorithm.searchAndApply(tacticExecutor, scenario, tacticChange, null);
				long runningTime = System.nanoTime() - startTime;
				System.out.println("Tactic Running Time : " + TimeUnit.MILLISECONDS.convert(runningTime, TimeUnit.NANOSECONDS) + " milliseconds.");
				
				Scenario scenarioToApplyBacktracking = null;
				if (archiActual!=null) {
					localChanges.add(tacticChange);
					List<Scenario> scenariosArchiActual = new ArrayList<Scenario>(archiActual.getScenarios());
					scenarioToApplyBacktracking = scenariosArchiActual.get(scenariosArchiActual.indexOf(scenario));
				} else {
					scenarioToApplyBacktracking = scenario;
				}
				
				
				Architecture archiBack = backtracking(backtrackingLevel + 1, trackAlgorithm, tacticsList,
						scenarioToApplyBacktracking, localChanges, applyAllTactics,
						forceOneResult);
				
				bestArchi = selectBestArchitecture(bestArchi, archiBack,
							scenario, bestArchiChanges, localChanges);
			}
		}
		changes.clear();
		changes.addAll(bestArchiChanges);
		return bestArchi;

	}
	
//	private boolean containsAllTactics(List<TacticChange> changes,
//			List<TacticExecutor> tactics) {
//		boolean tacticFound = false;
//		for (TacticExecutor tactic : tactics) {
//			tacticFound = false;
//			for (TacticChange change : changes) {
//				if (change.getTacticID().equals(tactic.getId())) {
//					tacticFound = true;
//					break;
//				}
//			}
//			if (!tacticFound)
//				return false;
//		}
//		return true;
//	}
	
	public ArchitectureAnalysis getArchitectureAnalysis(){
		return this.architectureAnalysis;
	}
	
	public void setArchitectureAnalysis(ArchitectureAnalysis architectureAnalysis){
		this.architectureAnalysis = architectureAnalysis;
	}

	public AnalysisResults getAnalysisResults() {
		return architectureAnalysisResults;
	}

	public void setAnalysisResults(AnalysisResults analysisResults) {
		this.architectureAnalysisResults = analysisResults;
	}
	
	public void setTrackAlgorithm(TrackAlgorithm trackAlgorithm) {
		this.trackAlgorithm = trackAlgorithm;
	}

	@Override
	public boolean equals(Object o){
		if (o!=null && o instanceof DesignBotExecutor) {
			DesignBotExecutor dbE = (DesignBotExecutor) o;
			if (this.architectureAnalysis!=null)
				return this.architectureAnalysis.equals(dbE.getArchitectureAnalysis());
		}
		return false;
	}

	
}
