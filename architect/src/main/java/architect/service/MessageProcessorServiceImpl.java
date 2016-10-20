package architect.service;

import java.io.StringWriter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import architect.model.ElementChange;

public abstract class MessageProcessorServiceImpl implements MessageProcessorService {

	@Autowired
	private ElementChangeService elementChangeService;
	
	@Autowired
	protected ArchitectureService architectureService;
		
	public abstract void doProcessing(String msg);

	protected void postChange(ElementChange change, JmsTemplate jsmTemplate) {
		if (change!=null) {
			// Get last scenario change
			Long lastChangeNumber = elementChangeService.lastChangeNumberByElementType(change.getId_architecture(), change.getElement_type());
			lastChangeNumber++;
			change.setChange_number(lastChangeNumber);
			// Store the change in DB
			elementChangeService.addElementChange(change);
			generateMessages(change,jsmTemplate);
		}
	}
	
	private void generateMessages(ElementChange change, JmsTemplate jsmTemplate) {
		try {

			JAXBContext context = JAXBContext.newInstance(ElementChange.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			final StringWriter sw = new StringWriter();
			marshaller.marshal(change, sw);
			jsmTemplate.send(new MessageCreator() {
				public Message createMessage(Session session)
						throws JMSException {
					TextMessage message = session.createTextMessage(sw
							.toString());
					return message;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doProcessing(final String msg, JmsTemplate jsmTemplate) {
		try {
			Session session = jsmTemplate.getConnectionFactory()
					.createConnection()
					.createSession(false, Session.AUTO_ACKNOWLEDGE);

			final TextMessage message = session.createTextMessage(msg
					.toString());
			int openTag = msg.indexOf("<id_architecture>");
			int closeTag = msg.indexOf("</id_architecture>");
			if (openTag != -1 && closeTag != -1) {
				Long id_architecture = Long.parseLong(msg.substring(openTag+17,
						closeTag));
				Long receiverGroup = architectureService.getArchitectureGroup(
						id_architecture).getId();
				message.setJMSCorrelationID("G" + receiverGroup + "A"
						+ id_architecture);
			}

			jsmTemplate.send(new MessageCreator() {
				public Message createMessage(Session session)
						throws JMSException {
					return message;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
