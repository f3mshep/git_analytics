package app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Repo {

    //Collection<Contributor> contributors;
    //Collection<Commit> commits;
    private String title, summary, url, owner, platform;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Repo(){}

    public Repo(String title, String summary, String url, String owner, String platform) {
        this.title = title;
        this.summary = summary;
        this.url = url;
        this.owner = owner;
        this.platform = platform;
    }

    public String getOwner() {
        return owner;
    }

    public String getPlatform() {
        return platform;
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
