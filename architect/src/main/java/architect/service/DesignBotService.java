package architect.service;

import java.util.List;

import architect.model.DesignBot;
import architect.model.ElementChange;

public interface DesignBotService extends MessageProcessorService {

	List<DesignBot> listDesignBotsByArchietctureId(Long architectureId);
	
	DesignBot getDesignBot(Long id_designbot);

	ElementChange add(DesignBot designBot, boolean undo);
	
	ElementChange delete(Long designBotId, boolean undo);
	
	ElementChange update(DesignBot oldDesignBot, DesignBot newDesignBot, boolean undo);
			
}
