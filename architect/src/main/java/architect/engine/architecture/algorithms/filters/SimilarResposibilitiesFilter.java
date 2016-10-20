package architect.engine.architecture.algorithms.filters;

import java.util.ArrayList;
import java.util.List;

import architect.engine.architecture.statistics.SimilarDependenciesResponsibilities;
import architect.model.Element;
import architect.model.Responsibility;
import architect.model.Scenario;

public class SimilarResposibilitiesFilter implements Filter {

	@Override
	public List<Element> getFilteredElements(Element toAnalize) {
		Scenario scenario = (Scenario) toAnalize;
		List<SimilarDependenciesResponsibilities> candidates = new ArrayList<SimilarDependenciesResponsibilities>();
		for (Responsibility responsibility : scenario.getResponsibilities()) {
			List<SimilarDependenciesResponsibilities> aux = getSimilarDependenciesResponsibilitiesList(responsibility);
			if (aux!=null) {
				candidates.addAll(aux);
			}
		}
		//Remove duplicates
		List<SimilarDependenciesResponsibilities> finalCandidates = new ArrayList<SimilarDependenciesResponsibilities>();
		for (SimilarDependenciesResponsibilities o : candidates) {
			if(!finalCandidates.contains(o)) 
				finalCandidates.add(o);
		}
		return new ArrayList<Element>(finalCandidates);
	}
	
	private List<SimilarDependenciesResponsibilities> getSimilarDependenciesResponsibilitiesList(Responsibility responsibility) {
		List<Responsibility> moduleResponsibilities = new ArrayList<Responsibility>(responsibility.getModule().getResponsibilities());
		return allCombinations(moduleResponsibilities, responsibility);
	}
	
	private List<SimilarDependenciesResponsibilities> allCombinations(List<Responsibility> moduleResponsibilities, Responsibility responsibility) {
		if (moduleResponsibilities.size()>=2 && moduleResponsibilities.contains(responsibility)) {
			List<SimilarDependenciesResponsibilities> resultLists = new ArrayList<SimilarDependenciesResponsibilities>();
			List<Responsibility> auxList = new ArrayList<Responsibility>(moduleResponsibilities);
			for (Responsibility responsibilityIt : auxList) {
				List<Responsibility> auxList2 = new ArrayList<Responsibility>(moduleResponsibilities);
				auxList2.remove(responsibilityIt);
				List<SimilarDependenciesResponsibilities> backtrackingResults = allCombinations(auxList2, responsibility);
				if (backtrackingResults!=null)
					resultLists.addAll(backtrackingResults);
			}
			SimilarDependenciesResponsibilities resps = new SimilarDependenciesResponsibilities();
			resps.addAllResponsibility(moduleResponsibilities);
			if (resps.areGoodCandidates()) {
				resultLists.add(resps);
			}
			return resultLists;
		}
		return null;
	}

}
