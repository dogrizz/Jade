package pl.dzmitrow.agenci;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

@SuppressWarnings("serial")
public class Jobber extends Agent {

	private int tempAgentCount;

	@Override
	public void setup() {

		/*
		 * Object[] args = getArguments(); if (args.length == 1) { tempAgentCount = Integer.parseInt((String) args[0]); } else {
		 * System.out.println("Ups"); doDelete(); return; }
		 */

		Behaviour ask = new TickerBehaviour(this,10000) {

			@Override
			public void onTick() {

				DFAgentDescription dfd = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("measuring");
				dfd.addServices(sd);

				try {
					DFAgentDescription[] result = DFService.search(myAgent, dfd);

					ACLMessage msg = new ACLMessage(ACLMessage.CFP);
					msg.setContent("" + ((int) Math.random() * 10 % 10) + 1);

					for (DFAgentDescription ad : result) {
						msg.addReceiver(ad.getName());
					}
					tempAgentCount = result.length;

					send(msg);

				} catch (Exception e) {
					Logger.getLogger(getLocalName()).log(Level.SEVERE, e.toString());
				}

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
						int newOffer = Integer.parseInt(msg.getContent());
						if (bestOffer == null || newOffer < bestOfferValue) {
							bestOffer = msg;
							bestOfferValue = newOffer;
						}
						break;
					case ACLMessage.REFUSE:
						System.out.println("DOSTALEM ODMOWE");
						break;
					case ACLMessage.INFORM:
						System.out.println("DOSTALEM WYNIK: "+msg.getContent());
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
						bestOffer = null;
					}

				}

				block(500);
			}

		};

		addBehaviour(ask);
		addBehaviour(collectResponse);
	}

}