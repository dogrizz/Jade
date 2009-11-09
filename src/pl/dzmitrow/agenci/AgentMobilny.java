package pl.dzmitrow.agenci;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Result;
import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.QueryPlatformLocationsAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class AgentMobilny extends Agent {

    public void setup() {
        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
        getContentManager().registerOntology(MobilityOntology.getInstance());

        Behaviour wannaBeNomad = new TickerBehaviour(this, 10000) {

            @Override
            protected void onTick() {
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(myAgent.getAMS());
                msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
                msg.setOntology(MobilityOntology.NAME);
                msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                try {
                    Action action = new Action();
                    action.setActor(myAgent.getAMS());
                    action.setAction(new QueryPlatformLocationsAction());
                    getContentManager().fillContent(msg, action);
                } catch (Exception fe) {
                    fe.printStackTrace();
                }
                send(msg);
            }
        };

        Behaviour goNomad = new CyclicBehaviour(this) {

            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    try {
                        Result results = (Result) myAgent.getContentManager().extractContent(msg);
                        int size = results.getItems().size();
                        if (size == 1) {
                            System.out.println("nie mam gdzie podrozowac");
                        } else {
                            int destination = (int) (Math.random() * 1000) % size;
                            while (getContainerController().getContainerName().equals(((Location) results.getItems().get(destination)).getName())) {
                                destination = (int) (Math.random() * 1000) % size;
                            }
                            System.out.println("Przenosze sie z " + getContainerController().getContainerName() + " do "
                                    + ((Location) results.getItems().get(destination)).getName());
                            doMove((Location) results.getItems().get(destination));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        addBehaviour(wannaBeNomad);
        addBehaviour(goNomad);

    }

    public void takeDown() {
        super.takeDown();
    }

    public void afterMove() {
        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
        getContentManager().registerOntology(MobilityOntology.getInstance());

    }

}
