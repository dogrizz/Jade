package pl.dzmitrow.agenci.sklep;

import pl.dzmitrow.agenci.sklep.stuff.Book;
import pl.dzmitrow.agenci.sklep.stuff.CD;
import pl.dzmitrow.agenci.sklep.stuff.Item;
import pl.dzmitrow.agenci.sklep.stuff.Track;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

@SuppressWarnings("serial")
public class Onto extends Ontology {

    public static final String ONTOLOGY_NAME = "Music-shop-ontology";

    // SLOWNIK
    public static final String ITEM = "Item";
    public static final String ITEM_SERIAL = "serial_number";
    public static final String CD = "CD";
    public static final String CD_NAME = "name";
    public static final String CD_TRACKS = "tracks";
    public static final String TRACK = "Track";
    public static final String TRACK_NAME = "name";
    public static final String TRACK_DURATION = "duration";
    public static final String BOOK = "Book";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_AUTHOR = "author";

    public static final String OWNS = "Owns";
    public static final String OWNS_OWNER = "owner";
    public static final String OWNS_ITEM = ITEM;

    public static final String SELL = "Sell";
    public static final String SELL_BUYER = "buyer";
    public static final String SELL_ITEM = ITEM;

    private static Ontology theInstance = new Onto();

    public static Ontology getInstance() {
        return theInstance;
    }

    private Onto() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try {
            add(new ConceptSchema(ITEM), Item.class);
            add(new ConceptSchema(CD), CD.class);
            add(new ConceptSchema(TRACK), Track.class);
            add(new ConceptSchema(BOOK), Book.class);

            add(new PredicateSchema(OWNS), Owns.class);
            add(new AgentActionSchema(SELL), Sell.class);
            add(new PredicateSchema(OWNS), Owns.class);
            add(new AgentActionSchema(SELL), Sell.class);

            ConceptSchema cs = (ConceptSchema) getSchema(ITEM);
            cs.add(ITEM_SERIAL, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);

            cs = (ConceptSchema) getSchema(CD);
            cs.addSuperSchema((ConceptSchema) getSchema(ITEM));
            cs.add(CD_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs.add(CD_TRACKS, (ConceptSchema) getSchema(TRACK), 1, ObjectSchema.UNLIMITED);

            cs = (ConceptSchema) getSchema(TRACK);
            cs.add(TRACK_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs.add(TRACK_DURATION, (PrimitiveSchema) getSchema(BasicOntology.INTEGER), ObjectSchema.OPTIONAL);

            cs = (ConceptSchema) getSchema(BOOK);
            cs.addSuperSchema((ConceptSchema) getSchema(ITEM));
            cs.add(BOOK_TITLE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            cs.add(BOOK_AUTHOR, (PrimitiveSchema) getSchema(BasicOntology.STRING));

            PredicateSchema ps = (PredicateSchema) getSchema(OWNS);
            ps.add(OWNS_OWNER, (ConceptSchema) getSchema(BasicOntology.AID));
            ps.add(OWNS_ITEM, (ConceptSchema) getSchema(ITEM));

            AgentActionSchema as = (AgentActionSchema) getSchema(SELL);
            as.add(SELL_ITEM, (ConceptSchema) getSchema(ITEM));
            as.add(SELL_BUYER, (ConceptSchema) getSchema(BasicOntology.AID));

        } catch (Exception e) {
            System.out.println(ONTOLOGY_NAME + " - Blad w konstruktorze:");
            e.printStackTrace();
        }
    }

}
