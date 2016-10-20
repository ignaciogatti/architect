package architect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import architect.dao.QualityAttributeDAO;
import architect.model.QualityAttribute;

@Service("qualityAttributeService")
public class QualityAttributeServiceImpl implements QualityAttributeService {

	@Autowired
    private QualityAttributeDAO qualityAttributeDAO;

	@Override
	public List<QualityAttribute> listQualityAttributes() {
		return qualityAttributeDAO.listQualityAttributes();
	}
	
	@Override
	public QualityAttribute getQualityAttribute(Long id_qualityAttribute) {
		return qualityAttributeDAO.getQualityAttribute(id_qualityAttribute);
	}

}
