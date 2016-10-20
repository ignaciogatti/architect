package architect.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import architect.dao.ArchitectureDAO;
import architect.engine.architecture.ChangeType;
import architect.engine.architecture.ElementType;
import architect.error.BlockedArchitectureException;
import architect.error.InvalidArchitectureException;
import architect.model.Architecture;
import architect.model.ElementChange;
import architect.model.Group;
import architect.model.User;

@Service("architectureService")
public class ArchitectureServiceImpl implements
		ArchitectureService {

	@Autowired
	private ArchitectureDAO architectureDAO;
	
	@Autowired
	private ElementChangeService elementChangeService;

	public void setArchitectureDAO(ArchitectureDAO architectureDAO) {
		this.architectureDAO = architectureDAO;
	}

	@Override
	public List<Architecture> listArchitectures() {
		return architectureDAO.listArchitectures();
	}

	@Override
	public Architecture getArchitectureById(Long id) {
		return architectureDAO.getArchitectureById(id);
	}

	@Override
	public Architecture validateArchitecture(Long id_architecture, String username,
			boolean modificationAction) throws Exception {
		Architecture architecture = architectureDAO.getArchitectureById(id_architecture);
		if (architecture != null) {
			if (modificationAction && architecture.isBlocked()) {
				throw new BlockedArchitectureException("Architecture is blocked.");
			}
			if (architecture.getGroup() != null) {
				Set<User> users = architecture.getGroup().getUsers();
				if (users != null) {
					for (User user : users) {
						if (user.getUsername().equals(username)) {
							return architecture;
						}
					}
				}
			}
		}
		throw new InvalidArchitectureException("Error validating architecture.");
	}

	@Override
	public Group getArchitectureGroup(Long id_architecture) {
		return architectureDAO.getArchitectureById(id_architecture).getGroup();
	}

	@Override
	public List<Architecture> listArchitecturesByGroups(Set<Group> userGroups) {
		List<Architecture> architectures = new ArrayList<Architecture>();
		for (Group group : userGroups) {
			List<Architecture> subSetArchitectures = architectureDAO.getArchitectureByGroupId(group
					.getId());
			architectures.addAll(subSetArchitectures);
		}
		return architectures;
	}

	@Override
	public ElementChange deleteArchitecture(Long id_architecture) {
		Architecture architecture = (Architecture) architectureDAO
				.getArchitectureById(id_architecture);
		architectureDAO.deleteArchitecture(architecture);
		ElementChange change = new ElementChange(null,
				ElementType.ARCHITECTURE.toString(), ChangeType.DELETE.toString(), architecture,
				null, new Boolean(true), new Boolean(false));
		postChange(change, architectureQueueTemplate);
		return change;
	}

	@Override
	public ElementChange updateArchitecture(Architecture architecture) {
		architectureDAO.updateArchitecture(architecture);
		ElementChange change = new ElementChange(null,
				ElementType.ARCHITECTURE.toString(), ChangeType.UPDATE.toString(), null,
				architecture, new Boolean(true), new Boolean(false));
		postChange(change, architectureQueueTemplate);
		return change;
	}

	@Override
	public ElementChange addArchitecture(Architecture newArchitecture) {
		// architectureDAO.addArchitecture(newArchitecture);
		Long addedArchitectureId = architectureDAO.addArchitecture(newArchitecture);
		Architecture addedArchitecture = (Architecture) this
				.getArchitectureById(addedArchitectureId);
		ElementChange change = null;
		if (addedArchitecture != null) {
			change = new ElementChange(null,
					ElementType.ARCHITECTURE.toString(), ChangeType.INSERT.toString(), null,
					addedArchitecture, new Boolean(true), new Boolean(false));
			postChange(change, architectureQueueTemplate);
		}
		return change;
	}

	@Override
	public int blockArchitecture(Long architectureId, String blockReason) {
		return architectureDAO.blockArchitecture(architectureId, blockReason);
	}

	@Override
	public int unblockArchitecture(Long architectureId) {
		return architectureDAO.unblockArchitecture(architectureId);
	}

	/**
	 * Architecture sync properties and methods
	 */

	@Autowired
	private JmsTemplate architectureQueueTemplate;

	@Autowired
	private JmsTemplate architectureTopicTemplate;

	private void postChange(ElementChange change, JmsTemplate jsmTemplate) {
		if (change != null) {
			// Get last scenario change
			Long lastChangeNumber = elementChangeService.lastChangeNumberByElementType(
					null, change.getElement_type());
			lastChangeNumber++;
			change.setChange_number(lastChangeNumber);
			// Store the change in DB
			elementChangeService.addElementChange(change);
			generateMessages(change, jsmTemplate);
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
	
	public void doProcessing(final String msg) {
		try {
			Session session = architectureTopicTemplate.getConnectionFactory()
					.createConnection()
					.createSession(false, Session.AUTO_ACKNOWLEDGE);

			final TextMessage message = session.createTextMessage(msg
					.toString());
			architectureTopicTemplate.send(new MessageCreator() {
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