package architect.dao;

import java.util.List;

import architect.model.Module;

public interface ModuleDAO {
	
	List<Module> listModulesByArchietctureId(Long architectureId);

	Module getModule(Long id_module);

	Long add(Module module);

	void delete(Module moduleToDelete);

	void update(Module module);
	
}
