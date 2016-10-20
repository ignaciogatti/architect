package architect.dao;

import java.util.List;

import architect.model.DesignBot;

public interface DesignBotDAO {
	
	List<DesignBot> listDesignBotsByArchietctureId(
			Long architectureId);

	DesignBot getDesignBot(Long id_designBot);

	Long add(DesignBot designBot);

	void delete(DesignBot designBot);

	void update(DesignBot designBot);
	
}
