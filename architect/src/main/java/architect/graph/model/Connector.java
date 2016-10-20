package architect.graph.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Connector extends mxElement {

	public Connector() {
		super();
	}
	
	public Connector(String id, String label, String href) {
		super(id,label,href);
	}
	
}
