package pl.dzmitrow.agenci.sklep.stuff;

import jade.util.leap.List;

@SuppressWarnings("serial")
public class CD extends Item {

    protected String name;
    protected List tracks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getTracks() {
        return tracks;
    }

    public void setTracks(List tracks) {
        this.tracks = tracks;
    }

    public CD() {
        ;
    }

    public CD(String name, List tracks) {
        this.name = name;
        this.tracks = tracks;
        this.serial_number = Long.valueOf(name.hashCode());
    }

    public String toString() {
        return name;
    }

}
