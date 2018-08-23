package app.controllers;

import app.exceptions.ContributorNotFoundException;
import app.models.Contributor;
import app.models.repositories.ContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/contributors")
public class ContributorsController {

    private ContributorRepository contributorRepository;

    @Autowired
    public ContributorsController(ContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
    }

    @GetMapping
    public @ResponseBody Iterable<Contributor> getAllContributors(){
        return contributorRepository.findAll();
    }

    @GetMapping(path = "/{username}")
    public @ResponseBody Contributor getContributor(@PathVariable String username){
        return this.contributorRepository.findByUsername(username).orElseThrow(() -> new ContributorNotFoundException(username));
    }
}
