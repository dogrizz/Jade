package pl.dzmitrow.agenci.sklep;

import pl.dzmitrow.agenci.sklep.stuff.Book;
import pl.dzmitrow.agenci.sklep.stuff.CD;
import pl.dzmitrow.agenci.sklep.stuff.Track;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

@SuppressWarnings("serial")
public class Seller extends Agent {

    private List items = new ArrayList();
    private Codec codec = new SLCodec();
    private Ontology onto = Onto.getInstance();

    @Override
    public void setup() {
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(onto);
        prepareData();

        Behaviour respondOwns = new CyclicBehaviour(this) {

            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF);
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    try {
                        Owns owns = (Owns) myAgent.getContentManager().extractContent(msg);
                        ACLMessage re = msg.createReply();
                        if (items.contains(owns.getItem())) {
                            re.setPerformative(ACLMessage.CONFIRM);
                        } else {
                            re.setPerformative(ACLMessage.DISCONFIRM);
                        }
                        send(re);
                    } catch (Exception e) {
                        System.out.println("respondOwns - Blad przy odkodywaniu wiadomosci");
                        e.printStackTrace();
                    }
                } else {
                    block(1);
                }
            }

        };

        Behaviour respondSell = new CyclicBehaviour(this) {

            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    try {
                        Sell sell = (Sell) myAgent.getContentManager().extractContent(msg);
                        ACLMessage re = msg.createReply();
                        if (items.contains(sell.getItem())) {
                            re.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                            items.remove(sell.getItem());
                        } else {
                            re.setPerformative(ACLMessage.REJECT_PROPOSAL);
                        }
                        send(re);
                    } catch (Exception e) {
                        System.out.println("respondSell - Blad przy odkodywaniu wiadomosci");
                        e.printStackTrace();
                    }
                } else {
                    block(1);
                }
            }

        };

        this.addBehaviour(respondOwns);
        this.addBehaviour(respondSell);
    }

    private void prepareData() {
        items.add(new Book("Mroczna Wieża", "Stephen King"));
        items.add(new Book("Dżihad Butleriański", "Brian Herbert, Kevin J.Anderson"));

        List listaPiosenek = new ArrayList();
        listaPiosenek.add(new Track("Stay All night", 252));
        items.add(new CD("Sweet Tea", listaPiosenek));

        listaPiosenek = new ArrayList();
        listaPiosenek.add(new Track("Come on in my kitchen", 197));
        items.add(new CD("Me and Mr Jhonson", listaPiosenek));
    }
}
