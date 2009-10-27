package pl.dzmitrow.agenci;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;

@SuppressWarnings("serial")
public class AgentTemperatur extends Agent {

	public void setup() {
		Behaviour b1 = new CyclicBehaviour() {

			@Override
			public void action() {
				System.out.println("Temperatura wewnatrz");
				block(5000);
			}
		};

		Behaviour b2 = new CyclicBehaviour() {

			@Override
			public void action() {
				System.out.println("Temperatura na zewnatrz");
				block(5000);
			}
		};

		this.addBehaviour(b1);
		this.addBehaviour(b2);
	}

	@Override
	public void takeDown() {
		super.takeDown();
	}

}
