package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    public Optional<Book> findFirstByAuthor(String author);

    public Optional<Book> findFirstByIsbn(String isbn);

    public List<Book> findByPublisherAndGenreAndYear(String publisher, String genre, String year);

    @Query("SELECT b FROM Book b WHERE (:publisher is null or b.publisher = :publisher) and "
            + "(:genre is null or b.genre = :genre) and (:year is null or b.year = :year)")
    public List<Book> findByPublisherAndGenreAndYearNameParams(@Param("publisher") String publisher,
            @Param("genre") String genre, @Param("year") String year);
}
