package architect.dao;

import java.util.List;

import architect.model.QualityAttribute;

public interface QualityAttributeDAO {
	
	List<QualityAttribute> listQualityAttributes();

	QualityAttribute getQualityAttribute(Long id_qualityAttribute);

	
	
}
