package wolox.training.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.dto.BookDto;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.service.OpenLibraryService;
import wolox.training.util.BookUtil;

/**
 * This class attends rest requests for book management
 */
@RestController
@RequestMapping("/api/books")
@Api
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     * This method gets all the saved {@link Book}s iltered by query parameters
     * @param allRequestParams: {@link Book} query parameters
     * @return saved {@link Book}s filtered by query parameters
     */
    @GetMapping
    public Iterable findAll(@RequestParam Map<String,String> allRequestParams, Pageable pageable) {
        String genre = allRequestParams.get("genre");
        String author = allRequestParams.get("author");
        String image = allRequestParams.get("image");
        String title = allRequestParams.get("title");
        String subtitle = allRequestParams.get("subtitle");
        String publisher = allRequestParams.get("publisher");
        String year = allRequestParams.get("year");
        String pagesString = allRequestParams.get("pages");
        Integer pages = pagesString == null || pagesString.trim().isEmpty() ? null : Integer.parseInt(pagesString);
        return bookRepository.findByFilters(genre, author, image, title, subtitle, publisher, year, pages, pageable);
    }

    /**
     *This method gets a {@link Book} by its identifier
     * @param id: {@link Book} identifier
     * @return {@link Book} saved
     * @throws {@link BookNotFoundException} if the book with the given identifier does not exist
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Giving an id, return the book", response = Book.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book"),
            @ApiResponse(code = 404, message = "Book Not Found")
    })
    public Book findById(@ApiParam(value = "id to find the book", required = true) @PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    /**
     * This method creates a {@link Book}
     * @param book: {@link Book} to create
     * @return Created {@link Book}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /**
     * This method deletes a {@link Book} by its identifier
     * @param id: {@link Book} identifier
     * @throws {@link BookNotFoundException} if the book with the given identifier does not exist
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    /**
     * This method updates a {@link Book} by its identifier
     * @param book: {@link Book} with new data to update
     * @param id: {@link Book} identifier
     * @return Updated {@link Book}
     * @throws {@link BookNotFoundException} if the book with the given identifier does not exist
     * @throws {@link BookIdMismatchException} if the identifier of the book does not match
     */
    @PutMapping("/{id}")
    public Book update(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

    /**
     * This method find a {@link Book} by its isbn
     * @param isbn: {@link Book} ISBN
     * @return Found {@link Book}
     * @throws JsonProcessingException
     */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> findByIsbn(@PathVariable String isbn) throws JsonProcessingException {
        Optional<Book> bookOptional = bookRepository.findFirstByIsbn(isbn);
        if(bookOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(bookOptional.get());
        }
        BookDto bookDto = openLibraryService.bookInfo(isbn).orElseThrow(BookNotFoundException::new);
        Book bookSaved = bookRepository.save(BookUtil.dtoToEntity(bookDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(bookSaved);
    }
}
