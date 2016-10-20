package architect.service;

public interface ChatQueueListener {
	
	public void doProcessing(final String msg);
	
}
