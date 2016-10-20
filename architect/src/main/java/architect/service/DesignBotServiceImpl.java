package architect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import architect.dao.DesignBotDAO;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.model.DesignBot;
import architect.model.ElementChange;

@Service("designBotService")
public class DesignBotServiceImpl extends MessageProcessorServiceImpl implements DesignBotService {

	@Autowired
    private DesignBotDAO designBotDAO;

	@Override
	public List<DesignBot> listDesignBotsByArchietctureId(Long architectureId) {
		return designBotDAO.listDesignBotsByArchietctureId(architectureId);
	}
	
	@Override
	public DesignBot getDesignBot(Long id_designBot) {
		return designBotDAO.getDesignBot(id_designBot);
	}

	@Override
	public ElementChange add(DesignBot designBot, boolean undo) {
		Long addedModuleId = designBotDAO.add(designBot);
		DesignBot addedDesignBot = (DesignBot) this.getDesignBot(addedModuleId);
		ElementChange change = null;
		if (addedDesignBot != null) {
			change = new ElementChange(designBot.getArchitecture().getId(), ElementType.DESIGNBOT.toString(),
					ChangeType.INSERT.toString(), null, addedDesignBot,
					new Boolean(true), new Boolean(undo));
			postChange(change,designBotQueueTemplate);
		}
		return change;
	}

	@Override
	public ElementChange delete(Long id_designBot, boolean undo) {
		DesignBot designBotToDelete = (DesignBot) designBotDAO.getDesignBot(id_designBot);
		designBotDAO.delete(designBotToDelete);
		ElementChange change = new ElementChange(designBotToDelete.getArchitecture().getId(), ElementType.DESIGNBOT.toString(),
				ChangeType.DELETE.toString(), designBotToDelete, null,
				new Boolean(true), new Boolean(undo));
		postChange(change,designBotQueueTemplate);
		return change;
	}

	@Override
	public ElementChange update(DesignBot oldDesignBot, DesignBot newDesignBot, boolean undo) {
		designBotDAO.update(newDesignBot);
		ElementChange change = new ElementChange(newDesignBot.getArchitecture().getId(), ElementType.DESIGNBOT.toString(),
				ChangeType.UPDATE.toString(), oldDesignBot, newDesignBot,
				new Boolean(true), new Boolean(undo));
		postChange(change,designBotQueueTemplate);
		return change;
	}
	
	/**
	 * Modules sync properties and methods
	 */

	@Autowired
	private JmsTemplate designBotQueueTemplate;

	@Autowired
	private JmsTemplate designBotTopicTemplate;

	@Override
	public void doProcessing(final String msg) {
		this.doProcessing(msg, designBotTopicTemplate);
	}
	
}
