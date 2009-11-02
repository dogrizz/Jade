package pl.dzmitrow.agenci;

import java.util.logging.Level;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

@SuppressWarnings("serial")
public class TempAgent extends Agent {

	private int cena = (int) (Math.random() * 100);

	@Override
	public void setup() {

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("measuring");
		sd.setName("temperature-measuring");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (Exception e) {
			Logger.getLogger(getLocalName()).log(Level.SEVERE, e.toString());
		}

		Behaviour collectJobs = new CyclicBehaviour() {

			@Override
			public void action() {
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
				ACLMessage msg = receive(mt);
				if (msg != null) {
					ACLMessage re = msg.createReply();
					int busy = (int)(Math.random() * 10 % 2);
					System.out.println(busy);
					if (busy == 1) {
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
					ACLMessage re = msg.createReply();
					re.setPerformative(ACLMessage.INFORM);
					Logger.getMyLogger(getName()).info("WYSYLAM WYNIK");
					re.setContent(""+Math.random()*10);
					send(re);
				}
			}
		};

		addBehaviour(executeJob);

	}

	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (Exception e) {
			Logger.getLogger(getLocalName()).log(Level.SEVERE, e.toString());
		}
	}
}
