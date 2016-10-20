package architect.error;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="errorMessage")
@XmlAccessorType(XmlAccessType.NONE)
public class ErrorMessage {

	@XmlAttribute
	private String message;
	
	public ErrorMessage() {
	
	}
	
	public ErrorMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
