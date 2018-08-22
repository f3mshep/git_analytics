package app.models.helpers;

import app.models.Commit;
import app.models.Contributor;
import app.models.Repo;
import app.models.repositories.CommitRepository;
import app.models.repositories.RepoRepository;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        String owner = repo.getOwnerName();
        String platform = "GitHub";
        return new RepoBuilder().setTitle(name).setSummary(desc).setUrl(url).setOwner(owner).setPlatform(platform).createRepo();
    }

    public List<Contributor> buildContributors(){
        return null;
    }

    public List<Commit> buildCommits(Repo myRepo) throws IOException{
        PagedIterable<GHCommit> gHCommits =  this.repo.listCommits();
        List<Commit> commits = new ArrayList<>();
        for (GHCommit ghCommit : gHCommits ){
            Commit commit = new CommitBuilder()
                    .setRepo(myRepo)
                    .setStatus(ghCommit.getLastStatus().toString())
                    .setTimestamp(ghCommit.getCommitDate())
                    .setUrl(ghCommit.getHtmlUrl().toString())
                    .createCommit();
            commits.add(commit);
        }
        return commits;
    }

}
