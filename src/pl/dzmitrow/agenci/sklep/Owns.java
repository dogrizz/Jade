package pl.dzmitrow.agenci.sklep;

import pl.dzmitrow.agenci.sklep.stuff.Item;
import jade.content.Predicate;
import jade.core.AID;

@SuppressWarnings("serial")
public class Owns implements Predicate {

    protected AID owner;
    protected Item item;

    public Owns() {
        ;
    }

    public Owns(AID owner, Item item) {
        this.owner = owner;
        this.item = item;
    }

    public AID getOwner() {
        return owner;
    }

    public void setOwner(AID owner) {
        this.owner = owner;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
