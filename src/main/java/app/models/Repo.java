package app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Repo {


    //Collection<Contributor> contributors;
    //Collection<Commit> commits;
    private String title, summary, url, owner, platform;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy="repo")
    private Set<Commit> commits = new HashSet<>();

    @JsonIgnore
    private Date lastUpdated;
    private Repo(){}

    public Repo(String title, String summary, String url, String owner, String platform) {
        this.title = title;
        this.summary = summary;
        this.url = url;
        this.owner = owner;
        this.platform = platform;
        this.setLastUpdated(Date.from(Instant.ofEpochSecond(0)));
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
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
