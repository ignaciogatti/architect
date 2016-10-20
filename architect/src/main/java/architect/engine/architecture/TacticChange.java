package architect.engine.architecture;

import java.util.ArrayList;
import java.util.List;

import architect.model.Element;
import architect.model.ElementChange;

public class TacticChange implements Comparable<TacticChange> {
	
	private Long id;
	private String tacticName;
	private Element appliedTo;
	private List<ElementChange> appliedChanges;
	
	public TacticChange() {
		this.appliedChanges = new ArrayList<ElementChange>();
	}
	
	public TacticChange(Long id, String tacticName, Element elementAppliedTo, List<ElementChange> appliedChanges) {
		this.id = id;
		this.tacticName = tacticName;
		this.appliedTo = elementAppliedTo;
		this.setAppliedChanges(appliedChanges);
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	public Element getAppliedTo() {
		return appliedTo;
	}

	public void setAppliedTo(Element appliedTo) {
		this.appliedTo = appliedTo;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o!=null && o instanceof TacticChange) {
			Long id = ((TacticChange) o).getId();
			if (this.id!=null && id!=null && this.id.equals(id))
				return true;
			else 
				return false;
		} else
			return false;
	}

	@Override
	public int compareTo(TacticChange o) {
		if (this.id == null)
			return -1;
		if (o == null)
			return 1;
		return id.compareTo(o.getId());
	}

	public String getTacticName() {
		return tacticName;
	}

	public void setTacticName(String tacticName) {
		this.tacticName = tacticName;
	}

	public List<ElementChange> getAppliedChanges() {
		return appliedChanges;
	}

	public void setAppliedChanges(List<ElementChange> appliedChanges) {
		this.appliedChanges = appliedChanges;
	}

}
