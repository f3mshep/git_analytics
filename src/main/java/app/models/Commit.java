package app.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Commit {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private Date timestamp;

    //private Contributor contributor;
    private String url, status;

    public Commit(Date timestamp, Contributor contributor, String url, String status) {
        this.timestamp = timestamp;
        //this.contributor = contributor;
        this.url = url;
        this.status = status;
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

    public String getUrl() {
        return url;
    }

    public String getStatus() {
        return status;
    }
}
