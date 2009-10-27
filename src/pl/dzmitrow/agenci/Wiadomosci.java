package pl.dzmitrow.agenci;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class Wiadomosci extends Agent {

	private boolean isFree;

	@Override
	public void setup() {

		Object[] args = getArguments();
		if (args.length == 1) {
			isFree = Boolean.parseBoolean((String) args[0]);
		} else {
			System.out.println("Ups");
			doDelete();
			return;
		}

		Behaviour b1 = new CyclicBehaviour() {
			@Override
			public void action() {
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
				ACLMessage msg = receive(mt);
				if (msg != null) {
					ACLMessage re = msg.createReply();
					switch (msg.getPerformative()) {

					case ACLMessage.INFORM:
						System.out.println("Dostalem informacje: " + msg.getContent());
						re.setPerformative(ACLMessage.INFORM);
						re.setContent("Thx");
						send(re);
						break;
					case ACLMessage.REQUEST:
						re.setPerformative(ACLMessage.NOT_UNDERSTOOD);
						send(re);
						break;
					case ACLMessage.QUERY_IF:
						re.setPerformative(ACLMessage.INFORM);
						re.setContent("" + isFree);
						send(re);
						break;
					default:
						re.setPerformative(ACLMessage.NOT_UNDERSTOOD);
						send(re);
					}
				} else {
					block();
				}
			}
		};

		this.addBehaviour(b1);
	}

}