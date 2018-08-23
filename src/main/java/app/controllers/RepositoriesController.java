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
        wrapper.updateCommits(repoRepository, commitRepository, contributorRepository);
        return commitRepository.findByRepoId(myRepo.getId());
    }

    @PostMapping
    public @ResponseBody Repo createRepo(@RequestParam String url) throws IOException {
        // one day this will be APIWrapper class which chooses appropriate wrapper
        GithubWrapper wrapper = new GithubWrapper(url);
        Repo repo = wrapper.buildRepo(repoRepository);
        wrapper.updateCommits(repoRepository, commitRepository, contributorRepository);
        return repo;
    }

    @GetMapping(path="/{id}")
    public @ResponseBody Repo returnRepo(@PathVariable Long id){
        return this.repoRepository.findById(id).orElseThrow(() -> new RepoNotFoundException(id));
    }

    private void validateRepo(long id){
        this.repoRepository.findById(id).orElseThrow(() -> new RepoNotFoundException(id));
    }

}
