package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RepoNotFoundException extends RuntimeException {

    public RepoNotFoundException(long id){
        super("could not find user '" + id + "'.");
    }

}
