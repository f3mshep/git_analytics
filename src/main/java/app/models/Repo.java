package app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

import java.util.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Repo {


    //Collection<Contributor> contributors;
    //Collection<Commit> commits;
    private String title, summary, url, platform;

    @ManyToOne()
    private Contributor owner;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy="repo", cascade = javax.persistence.CascadeType.ALL)
    private Set<Commit> commits = new HashSet<>();


    @JsonIgnore
    private Date lastUpdated;

    //Don't touch, for Repository
    private Repo(){}

    public Repo(String title, String summary, String url, Contributor owner, String platform) {
        this.title = title;
        this.summary = summary;
        this.url = url;
        this.owner = owner;
        this.platform = platform;
        this.setLastUpdated(Date.from(Instant.ofEpochSecond(0)));
    }

    @ManyToMany
    private Set<Contributor> contributors = new HashSet<>();

    //this is the most tightly coupled code I hope to write
    public void addContributor(Contributor contributor){
        if (!this.contributors.contains(contributor)) contributors.add(contributor);
    }


    @Override
    public String toString() {
        return "Repo{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", url='" + url + '\'' +
                ", platform='" + platform + '\'' +
                ", owner=" + owner +
                ", id=" + id +
                ", commits=" + commits +
                ", lastUpdated=" + lastUpdated +
                ", contributors=" + contributors +
                '}';
    }

    public Set<Contributor> getContributors() {
        return contributors;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Contributor getOwner() {
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
