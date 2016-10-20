package architect.engine.mast;

import java.util.List;

//El formato depende del tipo, solo soporta Type: timing_result
public class Result {
	private String type;
	private String event_name;
	private String worst_local_rt;
	private String best_local_rt;
	private double worst_bt;
	private int num_suspensions;
	private List<TimeResult> worst_global_rt;
	private List<TimeResult> best_global_rt;
	private String jitters;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public String getWorst_local_rt() {
		return worst_local_rt;
	}
	public void setWorst_local_rt(String worst_local_rt) {
		this.worst_local_rt = worst_local_rt;
	}
	public String getBest_local_rt() {
		return best_local_rt;
	}
	public void setBest_local_rt(String best_local_rt) {
		this.best_local_rt = best_local_rt;
	}
	public double getWorst_bt() {
		return worst_bt;
	}
	public void setWorst_bt(double worst_bt) {
		this.worst_bt = worst_bt;
	}
	public int getNum_suspensions() {
		return num_suspensions;
	}
	public void setNum_suspensions(int num_suspensions) {
		this.num_suspensions = num_suspensions;
	}
	public List<TimeResult> getWorst_global_rt() {
		return worst_global_rt;
	}
	public void setWorst_global_rt(List<TimeResult> worst_global_rt) {
		this.worst_global_rt = worst_global_rt;
	}
	public List<TimeResult> getBest_global_rt() {
		return best_global_rt;
	}
	public void setBest_global_rt(List<TimeResult> best_global_rt) {
		this.best_global_rt = best_global_rt;
	}
	public String getJitters() {
		return jitters;
	}
	public void setJitters(String jitters) {
		this.jitters = jitters;
	}

}




