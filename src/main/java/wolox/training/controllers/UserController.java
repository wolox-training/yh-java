package wolox.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

/**
 * This class attends rest requests for user management
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * This method gets all the saved {@link User}s
     * @return saved {@link User}s
     */
    @GetMapping
    public Iterable findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     *This method gets an {@link User} by its identifier
     * @param id: {@link User} identifier
     * @return {@link User} saved
     * @throws {@link UserNotFoundException} if the {@link User} with the given identifier does not exist
     */
    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    /**
     * This method creates an {@link User}
     * @param user: {@link User} to create
     * @return Created {@link User}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * This method deletes an {@link User} by its identifier
     * @param id: {@link User} identifier
     * @throws {@link UserNotFoundException} if the {@link User} with the given identifier does not exist
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    /**
     * This method updates an {@link User} by its identifier
     * @param user: {@link User} with new data to update
     * @param id: {@link User} identifier
     * @return Updated {@link User}
     * @throws {@link UserNotFoundException} if the {@link User} with the given identifier does not exist
     * @throws {@link UserIdMismatchException} if the identifier of the {@link User} does not match
     */
    @PutMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable Long id) {
        if (user.getId() != id) {
            throw new UserIdMismatchException();
        }
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }

    /**
     * This method adds a {@link Book} to the {@link User}
     * @param userId: {@link User} identifier
     * @param book: {@link Book} to add
     * @return Updated {@link User}
     */
    @PostMapping("/{userId}/books")
    public User addBook(@PathVariable Long userId, @RequestBody Book book) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Book savedBook = bookRepository.findById(book.getId()).orElseThrow(BookNotFoundException::new);
        user.addBook(savedBook);
        return userRepository.save(user);
    }

    /**
     * This method removes a {@link Book} from the {@link User}
     * @param userId: {@link User} identifier
     * @param bookId: {@link Book} identifier to remove
     * @return Updated {@link User}
     */
    @DeleteMapping("/{userId}/books/{bookId}")
    public User removeBook(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.removeBook(bookId);
        return userRepository.save(user);
    }
}
