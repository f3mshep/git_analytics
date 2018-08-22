package app.controllers;

import app.exceptions.RepoNotFoundException;
import app.models.Commit;
import app.models.Repo;
import app.models.helpers.GithubWrapper;
import app.models.helpers.Wrapper;
import app.models.repositories.CommitRepository;
import app.models.repositories.ContributorRepository;
import app.models.repositories.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/repos")
public class RepositoriesController {

    private final ContributorRepository contributorRepository;
    private final RepoRepository repoRepository;
    private final CommitRepository commitRepository;



    @Autowired
    public RepositoriesController(ContributorRepository contributorRepository, RepoRepository repoRepository, CommitRepository commitRepository) {
        this.contributorRepository = contributorRepository;
        this.repoRepository = repoRepository;
        this.commitRepository = commitRepository;
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
        GithubWrapper wrapper = new GithubWrapper(myRepo.getOwner() + "/" + myRepo.getTitle());
        List<Commit> commits = wrapper.updateCommits(myRepo);
        this.updateCommits(commits, myRepo);
        return commitRepository.findByRepoId(myRepo.getId());
    }

    @PostMapping
    public @ResponseBody Repo createRepo(@RequestParam String url) throws IOException {
        Wrapper wrapper = new GithubWrapper(url);
        //TODO: casting is a code smell. fix this
        Repo repo = ((GithubWrapper) wrapper).buildRepo();
        //TODO: Consider moving this to updateCommits
        repoRepository.save(repo);
        List<Commit> commits = wrapper.buildCommits(repo);
        this.updateCommits(commits, repo);
        return repo;
    }

    @GetMapping(path="/{id}")
    public @ResponseBody Repo returnRepo(@PathVariable Long id){
        return this.repoRepository.findById(id).orElseThrow(() -> new RepoNotFoundException(id));
    }

    private void updateCommits(List<Commit> commits, Repo myRepo){
        for (Commit commit : commits){
            commitRepository.save(commit);
        }
        myRepo.setLastUpdated(Instant.now().getEpochSecond());
        this.repoRepository.save(myRepo);
    }

    private void validateRepo(long id){
        this.repoRepository.findById(id).orElseThrow(() -> new RepoNotFoundException(id));
    }

}
