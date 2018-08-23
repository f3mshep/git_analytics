package app.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Contributor {

    @OneToMany(mappedBy = "contributor")
    private Set<Commit> commits = new HashSet<>();

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private Contributor(){}

    // private Collection<Commit> commits;

    private String username, platform;

    public Contributor(String username, String platform) {
        this.username = username;
        this.platform = platform;
    }

    public String getUsername() {
        return username;
    }
}
