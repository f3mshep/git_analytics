package repo;

import commit.Commit;
import contributor.Contributor;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.ArrayList;
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

    public Collection<Contributor> buildContributors(){
        Collection<Contributor> contributors = new ArrayList<>();
        repo
    }

    public Collection<Commit> buildCommits(){

    }

}
