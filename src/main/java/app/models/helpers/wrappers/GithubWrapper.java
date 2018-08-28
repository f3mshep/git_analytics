package app.models.helpers.wrappers;

import app.models.Commit;
import app.models.Contributor;
import app.models.Repo;
import app.models.helpers.CommitBuilder;
import app.models.helpers.RepoBuilder;
import app.models.repositories.CommitRepository;
import app.models.repositories.ContributorRepository;
import app.models.repositories.RepoRepository;
import org.kohsuke.github.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GithubWrapper implements APIWrapper {

    private GitHub github;
    private final String PLATFORM = "GitHub";
    private ContributorRepository contributorRepository;
    private RepoRepository repoRepository;
    private CommitRepository commitRepository;

    public GithubWrapper(RepoRepository repoRepository, CommitRepository commitRepository, ContributorRepository contributorRepository) throws IOException {
        github = GitHub.connect();
        this.repoRepository = repoRepository;
        this.commitRepository = commitRepository;
        this.contributorRepository = contributorRepository;
    }

    public Repo buildRepo(String gitUrl) throws IOException{
        GHRepository gHrepo = github.getRepository(gitUrl);
        String name = gHrepo.getName();
        String desc = gHrepo.getDescription();
        String url = gHrepo.getHtmlUrl().toString();
        String ownerName = gHrepo.getOwnerName();
        Contributor owner = findOrCreateUser(ownerName);
        String platform = PLATFORM;
        Repo repo =  new RepoBuilder().setTitle(name).setSummary(desc).setUrl(url).setOwner(owner).setPlatform(platform).createRepo();
        repoRepository.save(repo);
        return repo;
    }


    public void updateCommits(Repo repo) throws IOException{
        GHRepository gHrepo = github.getRepository(repo.getOwner().getUsername() + "/" + repo.getTitle());
        Iterable<GHCommit> gHCommits =  gHrepo.queryCommits().since(repo.getLastUpdated()).list();
        for (GHCommit ghCommit : gHCommits ){
            if(ghCommit.getCommitDate().after(repo.getLastUpdated())){
                Contributor contributor = findOrCreateUser(ghCommit.getAuthor());
                Commit commit = new CommitBuilder()
                    .setContributor(contributor)
                    .setRepo(repo)
                    .setStatus(ghCommit.getCommitShortInfo().getMessage())
                    .setTimestamp(ghCommit.getCommitDate())
                    .setUrl(ghCommit.getHtmlUrl().toString())
                    .createCommit();
                commitRepository.save(commit);
            }

        }
        repo.setLastUpdated(Date.from(Instant.now()));
        repoRepository.save(repo);
    }

    private Contributor findOrCreateUser(String username){
        if (username == null) username = "anonymous";
        Optional<Contributor> optional = contributorRepository.findByUsername(username);
        if (optional.isPresent()){
            return optional.get();
        } else {
            Contributor contributor = new Contributor(username, PLATFORM);
            contributorRepository.save(contributor);
            return contributor;
        }
    }

    private Contributor findOrCreateUser(GHUser user){
        String username;
        if (user == null) username = "anonymous";
        else username =  user.getLogin();
        return findOrCreateUser(username);
    }


}
