package pl.dzmitrow.agenci.sklep.stuff;

import jade.content.Concept;

@SuppressWarnings("serial")
public class Track implements Concept {

    protected String name;
    protected Integer duration;

    public Track() {
        ;
    }

    public Track(String name, Integer duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

}
