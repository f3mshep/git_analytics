package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContributorNotFoundException extends RuntimeException {

    public ContributorNotFoundException(String username) {
        super("could not find username " + username + ".");
    }

}
