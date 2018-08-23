package app.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Commit {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private Date timestamp;

    //private Contributor contributor;
    private String url, status;


    @ManyToOne
    Repo repo;

    @ManyToOne
    Contributor contributor;

    public Commit(Date timestamp, String url, String status, Repo repo, Contributor contributor) {
        this.timestamp = timestamp;
        this.url = url;
        this.status = status;
        this.repo = repo;
        this.contributor = contributor;
    }

    private Commit(){}

    public long getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

//    public Contributor getContributor() {
//        return contributor;
//    }


    public Repo getRepo() {
        return repo;
    }

    public String getUrl() {
        return url;
    }

    @Length(max=1000000, message="status must be under 100,000 characters")
    public String getStatus() {
        return status;
    }
}
