package app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collection;

@Entity
public class Contributor {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private Contributor(){}

    // private Collection<Commit> commits;

    private String name;

    public Contributor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
