package app.models.helpers;

import app.models.Commit;
import app.models.Repo;

import java.util.Date;

public class CommitBuilder {
    private Date timestamp;
    private String url;
    private String status;
    private Repo repo;

    public CommitBuilder setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public CommitBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public CommitBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public CommitBuilder setRepo(Repo repo) {
        this.repo = repo;
        return this;
    }

    public Commit createCommit() {
        return new Commit(timestamp, url, status, repo);
    }
}