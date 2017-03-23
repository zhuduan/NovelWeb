package live.foreach.novel.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ITEM")
public class Item {
    @Id
    @Column(name = "id", nullable = false, length = 30)
    private String id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public Item(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item() {

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
