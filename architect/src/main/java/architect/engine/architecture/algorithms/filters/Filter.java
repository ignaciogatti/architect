package architect.engine.architecture.algorithms.filters;

import java.util.List;

import architect.model.Element;

public interface Filter {
	
	public abstract List<Element> getFilteredElements(Element toAnalize);

}
