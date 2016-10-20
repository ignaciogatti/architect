package architect.service;

import java.util.List;

import architect.model.QualityAttribute;

public interface QualityAttributeService {

	List<QualityAttribute> listQualityAttributes();
	
	QualityAttribute getQualityAttribute(Long id_qualityAttribute);
	
}
