package app.models;

import javax.persistence.*;

@Table(name="Repo")
@Entity
public class Repo {

    //Collection<Contributor> contributors;
    //Collection<Commit> commits;
    private String name, desc, url;


    @Id
    @GeneratedValue
    private Long id;

    private Repo(){}

    public Repo(String name, String desc, String url) {
        this.name = name;
        this.desc = desc;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrl() {
        return url;
    }
}
