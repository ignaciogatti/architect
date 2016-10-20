package architect.graph.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Shape extends mxElement {

	public Shape() {
		super();
	}
	
	public Shape(String id, String label, String href) {
		super(id,label,href);
	}
	
}
