package app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Repo {

    //Collection<Contributor> contributors;
    //Collection<Commit> commits;
    private String title, summary, url;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Repo(){}

    public Repo(String title, String summary, String url) {
        this.title = title;
        this.summary = summary;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }
}
