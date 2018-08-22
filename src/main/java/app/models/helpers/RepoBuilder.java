package app.models.helpers;

import app.models.Repo;

public class RepoBuilder {
    private String title;
    private String summary;
    private String url;
    private String owner;
    private String platform;

    public RepoBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public RepoBuilder setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public RepoBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public RepoBuilder setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public RepoBuilder setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public Repo createRepo() {
        return new Repo(title, summary, url, owner, platform);
    }
}