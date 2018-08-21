package controllers;

import exceptions.RepoNotFoundException;
import models.Repo;
import models.helpers.GithubWrapper;
import models.helpers.Wrapper;
import models.repositories.CommitRepository;
import models.repositories.ContributorRepository;
import models.repositories.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PostMapping
    Repo createRepo(@RequestParam String url) throws IOException {
        //TODO: implement error handling/status code
        Wrapper wrapper = new GithubWrapper(url);
        Repo repo = ((GithubWrapper) wrapper).buildRepo();
        return repo;
    }

    @GetMapping("/{id}")
    Repo returnRepo(@PathVariable long id){
        this.validateRepo(id);

        return this.repoRepository.findById(id).orElseThrow(() -> new RepoNotFoundException(id));
    }

    private void validateRepo(long id){
        this.repoRepository.findById(id).orElseThrow(() -> new RepoNotFoundException(id));
    }

}
