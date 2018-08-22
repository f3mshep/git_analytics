package app.controllers;

import app.exceptions.RepoNotFoundException;
import app.models.Repo;
import app.models.helpers.GithubWrapper;
import app.models.helpers.Wrapper;
import app.models.repositories.CommitRepository;
import app.models.repositories.ContributorRepository;
import app.models.repositories.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path="/repos")
public class RepositoriesController {

   // private final ContributorRepository contributorRepository;
    private final RepoRepository repoRepository;
    // private final CommitRepository commitRepository;

    @Autowired
    public RepositoriesController(RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }


    @GetMapping
    public @ResponseBody String returnHi(){
        return "Hello";
    }

    @PostMapping
    public @ResponseBody Repo createRepo(@RequestParam String url) throws IOException {
        Wrapper wrapper = new GithubWrapper(url);
        System.out.println(url);
        Repo repo = ((GithubWrapper) wrapper).buildRepo();
        repoRepository.save(repo);
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
