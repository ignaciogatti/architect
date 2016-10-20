package architect.engine.mast;

public class TimeResult {
	private String referenced_event;
	private double time_value;
	
	public TimeResult(String reference_event, double time_value) {
		this.referenced_event = reference_event;
		this.time_value = time_value;
	}
	
	public String getReferenced_event() {
		return referenced_event;
	}
	public void setReferenced_event(String referenced_event) {
		this.referenced_event = referenced_event;
	}
	public double getTime_value() {
		return time_value;
	}
	public void setTime_value(double time_value) {
		this.time_value = time_value;
	}
}
