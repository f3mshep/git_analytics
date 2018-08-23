package app.controllers;

import app.exceptions.CommitNotFoundException;
import app.models.Commit;
import app.models.repositories.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commits")
public class CommitsController {

    private CommitRepository commitRepository;

    @Autowired
    public CommitsController(CommitRepository commitRepository) {
        this.commitRepository = commitRepository;
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody Commit getCommit(@PathVariable long id){
        return this.commitRepository.findById(id).orElseThrow(()-> new CommitNotFoundException(id));
    }
}
