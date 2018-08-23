package app.models.helpers;

import app.models.Commit;
import app.models.Contributor;
import app.models.Repo;
import app.models.repositories.CommitRepository;
import app.models.repositories.ContributorRepository;
import app.models.repositories.RepoRepository;
import org.kohsuke.github.*;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GithubWrapper {

    private final String PLATFORM = "GitHub";
    private GitHub github;
    private GHRepository gHrepo;
    private String url;
    private Repo repo;

    public GithubWrapper(String url) throws IOException {
        this.url = url;
        github = GitHub.connect();
        // think about throwing an error
        // if gHrepo not found
        gHrepo = github.getRepository(url);
    }

    public GithubWrapper(Repo repo) throws IOException{
        this.repo = repo;
        github = GitHub.connect();
        // think about throwing an error
        // if gHrepo not found
        gHrepo = github.getRepository(repo.getOwner() +"/" + repo.getTitle());
    }


    public Repo buildRepo(RepoRepository repoRepository){
        String name = gHrepo.getName();
        String desc = gHrepo.getDescription();
        String url = gHrepo.getHtmlUrl().toString();
        String owner = gHrepo.getOwnerName();
        String platform = PLATFORM;
        this.repo =  new RepoBuilder().setTitle(name).setSummary(desc).setUrl(url).setOwner(owner).setPlatform(platform).createRepo();
        repoRepository.save(repo);
        return repo;
    }


    public void updateCommits(RepoRepository repoRepository, CommitRepository commitRepository, ContributorRepository contributorRepository) throws IOException{
        PagedIterable<GHCommit> gHCommits =  gHrepo.queryCommits().since(repo.getLastUpdated()).list();
        for (GHCommit ghCommit : gHCommits ){
            if(ghCommit.getCommitDate().after(repo.getLastUpdated())){
                Contributor contributor = findOrCreateUser(ghCommit.getAuthor(), contributorRepository);
                Commit commit = new CommitBuilder()
                    .setContributor(contributor)
                    .setRepo(repo)
                    .setStatus(ghCommit.getCommitShortInfo().getMessage())
                    .setTimestamp(ghCommit.getCommitDate())
                    .setUrl(ghCommit.getHtmlUrl().toString())
                    .createCommit();
                commit.addAssociation();
                commitRepository.save(commit);
            }

        }
        repo.setLastUpdated(Date.from(Instant.now()));
        repoRepository.save(repo);
    }

    private Contributor findOrCreateUser(GHUser user, ContributorRepository contributorRepository){
        String username;
        if (user == null) username = "anonymous";
        else username =  user.getLogin();
        Optional<Contributor> optional = contributorRepository.findByUsername(username);
        if (optional.isPresent()){
            return optional.get();
        } else {
            Contributor contributor = new Contributor(username, PLATFORM);
            contributorRepository.save(contributor);
            return contributor;
        }
    }


}
