package architect.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("chatService")
public class ChatQueueListenerImpl implements ChatQueueListener {

	@Autowired
	private JmsTemplate chatTopicTemplate;
	
	public void setChatTopicTemplate(JmsTemplate chatTopicTemplate) {
		this.chatTopicTemplate = chatTopicTemplate;
    }
	
	@Override
	@Transactional
	public void doProcessing(final String msg) {
		try {
			Session session = chatTopicTemplate.getConnectionFactory()
					.createConnection()
					.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final TextMessage message = session.createTextMessage(msg.toString());
			message.setJMSCorrelationID("12345678");
			chatTopicTemplate.send(new MessageCreator() {
				public Message createMessage(Session session)
						throws JMSException {
					return message;
				}
			});
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
