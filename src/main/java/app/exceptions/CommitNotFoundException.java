package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommitNotFoundException extends RuntimeException {

    public CommitNotFoundException(long id){
        super("could not find commit " + id + ".");
    }

}
