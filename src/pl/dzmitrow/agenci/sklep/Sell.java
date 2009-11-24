package pl.dzmitrow.agenci.sklep;

import jade.content.AgentAction;
import jade.core.AID;
import pl.dzmitrow.agenci.sklep.stuff.Item;

@SuppressWarnings("serial")
public class Sell implements AgentAction {

    protected AID buyer;
    protected Item item;

    public AID getBuyer() {
        return buyer;
    }

    public void setBuyer(AID buyer) {
        this.buyer = buyer;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Sell() {
        ;
    }

    public Sell(AID buyer, Item item) {
        this.buyer = buyer;
        this.item = item;
    }

}
