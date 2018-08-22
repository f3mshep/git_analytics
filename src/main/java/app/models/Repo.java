package app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collection;


@Entity
public class Repo {

    //Collection<Contributor> contributors;
    //Collection<Commit> commits;
    private String name, desc, url;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private Repo(){}

    public Repo(String name, String desc, String url) {
        this.name = name;
        this.desc = desc;
        this.url = url;
    }

    public long getId() {
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
