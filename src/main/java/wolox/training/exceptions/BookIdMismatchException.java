package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Provide correct Book Id")
public class BookIdMismatchException extends RuntimeException {

    public BookIdMismatchException() {
        super();
    }
}
