package app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.sql.Date;
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
    private long lastUpdated;
    private Repo(){}

    public Repo(String title, String summary, String url, String owner, String platform) {
        this.title = title;
        this.summary = summary;
        this.url = url;
        this.owner = owner;
        this.platform = platform;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
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
