package architect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import architect.dao.ModuleDAO;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.model.ElementChange;
import architect.model.Module;
import architect.service.diagram.DiagramModuleService;

@Service("moduleService")
public class ModuleServiceImpl extends MessageProcessorServiceImpl implements ModuleService {

	@Autowired
	private DiagramModuleService diagramModuleService;
	
	@Autowired
    private ModuleDAO moduleDAO;

	@Override
	public List<Module> listModulesByArchietctureId(Long architectureId) {
		return moduleDAO.listModulesByArchietctureId(architectureId);
	}
	
	@Override
	public Module getModule(Long id_module) {
		return moduleDAO.getModule(id_module);
	}

	@Override
	public ElementChange add(Module module, boolean consistentState, boolean undo) {
		Long addedModuleId = moduleDAO.add(module);
		Module addedModule = (Module) this.getModule(addedModuleId);
		ElementChange change = null;
		if (addedModule != null) {
			change = new ElementChange(module.getArchitecture().getId(),
					ElementType.MODULE.toString(),
					ChangeType.INSERT.toString(), null, addedModule,
					new Boolean(consistentState), new Boolean(undo));
			postChange(change,moduleQueueTemplate);
			diagramModuleService.add(addedModule);
		}
		return change;
	}

	@Override
	public ElementChange delete(Long id_module, boolean consistentState, boolean undo) {
		Module moduleToDelete = (Module) moduleDAO.getModule(id_module);
		moduleToDelete.getArchitecture().getModules().remove(moduleToDelete);
		moduleDAO.delete(moduleToDelete);
		ElementChange change = new ElementChange(moduleToDelete
				.getArchitecture().getId(), ElementType.MODULE.toString(),
				ChangeType.DELETE.toString(), moduleToDelete, null,
				new Boolean(consistentState), new Boolean(undo));
		postChange(change,moduleQueueTemplate);
		diagramModuleService.delete(moduleToDelete);
		return change;
	}

	@Override
	public ElementChange update(Module oldModule, Module newModule, boolean consistentState, boolean undo) {
		ElementChange change = new ElementChange(newModule.getArchitecture()
				.getId(), ElementType.MODULE.toString(),
				ChangeType.UPDATE.toString(), oldModule, newModule,
				new Boolean(consistentState), new Boolean(undo));
		moduleDAO.update(newModule);
		postChange(change,moduleQueueTemplate);
		return change;
	}
	
	/**
	 * Modules sync properties and methods
	 */

	@Autowired
	private JmsTemplate moduleQueueTemplate;

	@Autowired
	private JmsTemplate moduleTopicTemplate;

	@Override
	public void doProcessing(final String msg) {
		this.doProcessing(msg, moduleTopicTemplate);
	}
	
}
