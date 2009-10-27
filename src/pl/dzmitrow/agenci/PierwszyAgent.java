package pl.dzmitrow.agenci;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.WakerBehaviour;

@SuppressWarnings("serial")
public class PierwszyAgent extends Agent {
	
	@Override
	protected void setup() {
		super.setup();
		Behaviour b = new WakerBehaviour(this,5000){
			@Override
			public void onWake(){
				System.out.println("Jestem jestem!");
			}
			
		};
		this.addBehaviour(b);
	}
	
	@Override
	protected void takeDown(){
		super.takeDown();
	}
	
}
