package app.models;

import javax.persistence.*;
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

    public Commit(Date timestamp, String url, String status, Repo repo) {
        this.timestamp = timestamp;
        this.url = url;
        this.status = status;
        this.repo = repo;
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

    public String getStatus() {
        return status;
    }
}
