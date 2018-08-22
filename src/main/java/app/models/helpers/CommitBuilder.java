package app.models.helpers;

import app.models.Commit;
import app.models.Contributor;

import java.util.Date;

public class CommitBuilder {
    private Date timestamp;
    private Contributor contributor;
    private String url;
    private String status;

    public CommitBuilder setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public CommitBuilder setContributor(Contributor contributor) {
        this.contributor = contributor;
        return this;
    }

    public CommitBuilder setURL(String url) {
        this.url = url;
        return this;
    }

    public CommitBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public Commit createCommit() {
        return new Commit(timestamp, contributor, url, status);
    }
}