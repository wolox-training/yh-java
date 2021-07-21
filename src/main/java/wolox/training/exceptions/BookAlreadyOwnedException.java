package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Book already owned")
public class BookAlreadyOwnedException extends RuntimeException {

    public BookAlreadyOwnedException() {
        super();
    }
}
