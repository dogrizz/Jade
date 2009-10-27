package pl.dzmitrow.agenci;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class TempAgent extends Agent {

	private int cena = (int) (Math.random() * 100);

	@Override
	public void setup() {

		Behaviour collectJobs = new CyclicBehaviour() {

			@Override
			public void action() {
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
				ACLMessage msg = receive(mt);
				if (msg != null) {
					ACLMessage re = msg.createReply();
					if ((((int) Math.random() * 10) % 2) == 1) {
						re.setPerformative(ACLMessage.REFUSE);
						send(re);
					} else {
						re.setPerformative(ACLMessage.PROPOSE);
						int price = cena * Integer.parseInt(msg.getContent());
						re.setContent("" + price);
						send(re);
					}
				}
			}

		};

		addBehaviour(collectJobs);

		Behaviour executeJob = new CyclicBehaviour() {

			@Override
			public void action() {
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
				ACLMessage msg = receive(mt);
				if (msg != null) {
					System.out.println("Wykonuje zadanie");
				}
			}
		};

		addBehaviour(executeJob);

	}
}
