package architect.engine.mast;

import java.util.List;

public class Transaction {
	
	public static final String NAME = "Name";
	public static final String RESULTS = "Results";
	public static final String TYPE = "Type";
	public static final String EVENT_NAME = "Event_Name";
	public static final String WORST_LOCAL_RT = "Worst_Local_Response_Time";
	public static final String BEST_LOCAL_RT = "Best_Local_Response_Time";
	public static final String WORST_BT = "Worst_Blocking_Time";
	public static final String NUM_SUSPENSIONS = "Num_Of_Suspensions";
	public static final String WORST_GLOBAL_RT = "Worst_Global_Response_Times";
	public static final String BEST_GLOBAL_RT = "Best_Global_Response_Times";
	public static final String JITTERS = "Jitters";
	
	private String name;
	private List<Result> results;
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	

}


