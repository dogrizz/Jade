package pl.dzmitrow.agenci;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class Jobber extends Agent {

	private int tempAgentCount;

	@Override
	public void setup() {

		Object[] args = getArguments();
		if (args.length == 1) {
			tempAgentCount = Integer.parseInt((String) args[0]);
		} else {
			System.out.println("Ups");
			doDelete();
			return;
		}

		Behaviour ask = new OneShotBehaviour() {

			@Override
			public void action() {
				doWait(10000);
				ACLMessage msg = new ACLMessage(ACLMessage.CFP);
				msg.setContent("" + ((int) Math.random() * 10 % 10) + 1);

				for (int i = 1; i <= tempAgentCount; i++) {
					msg.addReceiver(new AID("Agent" + i, AID.ISLOCALNAME));
				}

				send(msg);
			}
		};

		Behaviour collectResponse = new CyclicBehaviour() {

			ACLMessage bestOffer = null;
			List<ACLMessage> proposals = new ArrayList<ACLMessage>();
			int bestOfferValue;
			int responses = 0;

			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					responses++;
					ACLMessage re = msg.createReply();
					switch (msg.getPerformative()) {

					case ACLMessage.PROPOSE:
						System.out.println("DOSTALEM PROPOZYCJE");
						proposals.add(msg);
						if (bestOffer == null) {
							bestOffer = msg;
							bestOfferValue = Integer.parseInt(msg.getContent());
						} else {
							int newOffer = Integer.parseInt(msg.getContent());
							if (newOffer < bestOfferValue) {
								bestOffer = msg;
								bestOfferValue = newOffer;
							}
						}
						break;
					case ACLMessage.REFUSE:
						System.out.println("DOSTALEM ODMOWE");
						break;
					default:
						re.setPerformative(ACLMessage.NOT_UNDERSTOOD);
						send(re);
					}

					if (responses == tempAgentCount) {
						for (ACLMessage message : proposals) {
							re = message.createReply();
							if (message == bestOffer) {
								re.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
								send(re);
							} else {

								re.setPerformative(ACLMessage.REJECT_PROPOSAL);
								send(re);
							}
						}
						responses = 0;
						proposals.clear();
					}

				}

				block(500);
			}

		};

		addBehaviour(ask);
		addBehaviour(collectResponse);
	}

}