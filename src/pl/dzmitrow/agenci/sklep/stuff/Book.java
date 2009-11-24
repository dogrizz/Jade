package pl.dzmitrow.agenci.sklep.stuff;

@SuppressWarnings("serial")
public class Book extends Item {

    protected String author;
    protected String title;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Book() {
        ;
    }

    public Book(String title, String author) {
        this.author = author;
        this.title = title;
        this.serial_number = Long.valueOf(title.hashCode());
    }
}
