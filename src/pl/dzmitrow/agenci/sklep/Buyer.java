package pl.dzmitrow.agenci.sklep;

import pl.dzmitrow.agenci.sklep.stuff.Book;
import pl.dzmitrow.agenci.sklep.stuff.CD;
import pl.dzmitrow.agenci.sklep.stuff.Item;
import pl.dzmitrow.agenci.sklep.stuff.Track;

import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

@SuppressWarnings("serial")
public class Buyer extends Agent {

    private Codec codec = new SLCodec();
    private Ontology onto = Onto.getInstance();
    private List iWant = new ArrayList();
    private List iHave = new ArrayList();
    private AID seller = new AID("Seller", AID.ISLOCALNAME);
    private Object want2buy;

    @Override
    public void setup() {
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(onto);
        setupIWant();

        Behaviour checkSeller = new TickerBehaviour(this, 10000) {

            @Override
            protected void onTick() {
                want2buy = iWant.get((int) (Math.random() * 1000) % iWant.size());
                Owns owns;
                if (want2buy instanceof Book) {
                    owns = new Owns(seller, (Book) want2buy);
                } else {
                    owns = new Owns(seller, (CD) want2buy);
                }

                ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);
                msg.addReceiver(seller);
                msg.setOntology(onto.getName());
                msg.setLanguage(codec.getName());
                try {
                    getContentManager().fillContent(msg, owns);
                } catch (CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }
                send(msg);
            }
        };

        Behaviour anyoneHazMyItemz = new CyclicBehaviour(this) {

            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM), MessageTemplate
                        .MatchPerformative(ACLMessage.DISCONFIRM));
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    if (msg.getPerformative() == ACLMessage.DISCONFIRM) {
                        System.out.println("Seller nie miał czego chce");
                        block(1);
                        return;
                    }
                    Sell sell;
                    if (want2buy instanceof Book) {
                        sell = new Sell(new AID(myAgent.getAID().getLocalName(), AID.ISLOCALNAME), (Book) want2buy);
                    } else {
                        sell = new Sell(new AID(myAgent.getAID().getLocalName(), AID.ISLOCALNAME), (CD) want2buy);
                    }
                    ACLMessage re = msg.createReply();
                    re.setPerformative(ACLMessage.PROPOSE);
                    re.setOntology(onto.getName());
                    re.setLanguage(codec.getName());
                    try {
                        myAgent.getContentManager().fillContent(re, sell);
                        send(re);
                    } catch (Exception e) {
                        System.out.println("anyoneHazMyItemz - Blad podczas redagowania wiadomosci");
                        e.printStackTrace();
                    }
                } else {
                    block(1);
                }
            }

        };

        Behaviour canIBuy = new CyclicBehaviour(this) {

            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL), MessageTemplate
                        .MatchPerformative(ACLMessage.REJECT_PROPOSAL));
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                        System.out.println("Seller odmowil");
                        block(1);
                        return;
                    }
                    iHave.add((Item) want2buy);
                    want2buy = null;
                } else {
                    block(1);
                }
            }

        };

        this.addBehaviour(checkSeller);
        this.addBehaviour(anyoneHazMyItemz);
        this.addBehaviour(canIBuy);
    }

    private void setupIWant() {
        iWant.add(new Book("Mroczna Wieża", "Stephen King"));
        iWant.add(new Book("Dżihad Butleriański", "Brian Herbert, Kevin J.Anderson"));

        List listaPiosenek = new ArrayList();
        listaPiosenek.add(new Track("Stay All night", 252));
        iWant.add(new CD("Sweet Tea", listaPiosenek));

        listaPiosenek = new ArrayList();
        listaPiosenek.add(new Track("Come on in my kitchen", 197));
        iWant.add(new CD("Me and Mr Jhonson", listaPiosenek));

    }
}
