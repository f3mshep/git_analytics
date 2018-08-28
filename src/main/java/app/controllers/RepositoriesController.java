package app.controllers;

import app.models.helpers.wrappers.*;
import app.exceptions.RepoNotFoundException;
import app.models.Commit;
import app.models.Repo;
import app.models.helpers.wrappers.GithubWrapper;
import app.models.repositories.CommitRepository;
import app.models.repositories.ContributorRepository;
import app.models.repositories.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping(path="/repos")
public class RepositoriesController {

    private final int REFRESH_LIMIT_MINUTES = 30;
    private final ContributorRepository contributorRepository;
    private final RepoRepository repoRepository;
    private final CommitRepository commitRepository;
    protected APIWrapper apiWrapper;

    @Autowired
    public RepositoriesController(ContributorRepository contributorRepository, RepoRepository repoRepository, CommitRepository commitRepository) throws Exception {
        this.contributorRepository = contributorRepository;
        this.repoRepository = repoRepository;
        this.commitRepository = commitRepository;
        this.apiWrapper = new GithubWrapper(repoRepository, commitRepository, contributorRepository);
    }


    @GetMapping
    public @ResponseBody Iterable<Repo> getAllRepos(){
        return repoRepository.findAll();
    }

    @GetMapping(path="/{id}/commits")
    public @ResponseBody Iterable<Commit> getCommits(@PathVariable Long id) throws IOException{
        validateRepo(id);
        Repo myRepo  = repoRepository.findById(id).get();
        //TODO: refactor error handling
        if(shouldRepoUpdate(myRepo)) apiWrapper.updateCommits(myRepo);
        return commitRepository.findByRepoId(myRepo.getId());
    }

    @PostMapping
    public @ResponseBody Repo createRepo(@RequestParam String url) throws IOException {
        // one day this will be APIWrapper class which chooses appropriate wrapper
        Repo repo = apiWrapper.buildRepo(url);
        apiWrapper.updateCommits(repo);
        return repo;
    }

    @GetMapping(path="/{id}")
    public @ResponseBody Repo returnRepo(@PathVariable Long id){
        System.out.println(id);
        return this.repoRepository.findById(id).orElseThrow(() -> new RepoNotFoundException(id));
    }

    private void validateRepo(long id){
        this.repoRepository.findById(id).orElseThrow(() -> new RepoNotFoundException(id));
    }


    private boolean shouldRepoUpdate(Repo repo){
        Instant now = Instant.now();
        Instant lastUpdated = repo.getLastUpdated().toInstant();
        return lastUpdated.isBefore(now.minus(REFRESH_LIMIT_MINUTES, ChronoUnit.MINUTES));
    }

    public void setWrapper(APIWrapper wrapper){
        this.apiWrapper = wrapper;
    }
}
