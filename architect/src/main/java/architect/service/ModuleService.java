package architect.service;

import java.util.List;

import architect.model.ElementChange;
import architect.model.Module;

public interface ModuleService extends MessageProcessorService {

	List<Module> listModulesByArchietctureId(Long architectureId);
	
	Module getModule(Long id_module);

	ElementChange add(Module module, boolean consistentState, boolean undo);
	
	ElementChange delete(Long id_module, boolean consistentState, boolean undo);
	
	ElementChange update(Module oldModule, Module newModule, boolean consistentState, boolean undo);
			
}
