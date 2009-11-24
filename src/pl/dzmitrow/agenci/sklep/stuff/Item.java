package pl.dzmitrow.agenci.sklep.stuff;

import jade.content.Concept;

@SuppressWarnings("serial")
public class Item implements Concept {

    protected Long serial_number;

    public Long getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(Long serialNumber) {
        serial_number = serialNumber;
    }

    @Override
    public boolean equals(Object other) {
        Item inny = null;
        if (other instanceof Item) {
            inny = (Item) other;
        } else {
            return false;
        }
        return serial_number.equals(inny.serial_number);
    }

}
