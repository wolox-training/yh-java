package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    public Optional<Book> findFirstByAuthor(String author);
    public Optional<Book> findFirstByIsbn(String isbn);
}
