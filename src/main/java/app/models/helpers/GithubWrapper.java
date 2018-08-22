package app.models.helpers;

import app.models.Commit;
import app.models.Contributor;
import app.models.Repo;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.Collection;

public class GithubWrapper implements Wrapper {

    private GitHub github;
    private GHRepository repo;
    private String url;

    public GithubWrapper(String url) throws IOException {
        this.url = url;
        github = GitHub.connect();
        // think about throwing an error
        // if repo not found
        repo = github.getRepository(url);
    }

    public Repo buildRepo(){
        String name = repo.getName();
        String desc = repo.getDescription();
        String url = repo.getHtmlUrl().toString();
        return new Repo(name, desc, url);
    }

    public Collection<Contributor> buildContributors(){
        return null;
    }

    public Collection<Commit> buildCommits(){
        return null;
    }

}
