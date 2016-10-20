package architect.engine.architecture.comparators;

import java.util.Comparator;

import architect.model.Element;
import architect.model.Responsibility;

public class ResponsibilityComplexityComparator implements
		Comparator<Element> {

	@Override
	public int compare(Element o1, Element o2) {
		Double complexityR1=((Responsibility) o1).getComplexity(); 
		Double complexityR2=((Responsibility) o2).getComplexity();
		return complexityR2.compareTo(complexityR1);
	}

}
