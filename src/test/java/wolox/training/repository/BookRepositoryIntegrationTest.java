package wolox.training.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@DataJpaTest
class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    private Book oneTestBook;

    @BeforeEach
    public void setUp() {
        oneTestBook = new Book();
        oneTestBook.setAuthor("Fernando Soto Aparicio");
        oneTestBook.setTitle("The Rebellion of the Rats");
        oneTestBook.setGenre("Fiction");
        oneTestBook.setImage(
                "https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG");
        oneTestBook.setIsbn("9789583061738");
        oneTestBook.setPages(266);
        oneTestBook.setPublisher("PANAMERICANA");
        oneTestBook.setSubtitle("NA");
        oneTestBook.setYear("1962");
    }

    @Test
    void whenSaveBookThenBookIsPersisted() {
        Book bookSaved = bookRepository.save(oneTestBook);
        Assertions.assertEquals(oneTestBook.getAuthor(), bookSaved.getAuthor());
        Assertions.assertEquals(oneTestBook.getTitle(), bookSaved.getTitle());
        Assertions.assertEquals(oneTestBook.getGenre(), bookSaved.getGenre());
        Assertions.assertEquals(oneTestBook.getImage(), bookSaved.getImage());
        Assertions.assertEquals(oneTestBook.getIsbn(), bookSaved.getIsbn());
        Assertions.assertEquals(oneTestBook.getPages(), bookSaved.getPages());
        Assertions.assertEquals(oneTestBook.getPublisher(), bookSaved.getPublisher());
        Assertions.assertEquals(oneTestBook.getSubtitle(), bookSaved.getSubtitle());
        Assertions.assertEquals(oneTestBook.getYear(), bookSaved.getYear());
    }

    @Test
    void whenSaveBookWithoutTitleThenThrowException() {
        oneTestBook = new Book();
        oneTestBook.setAuthor("Fernando Soto Aparicio");
        oneTestBook.setGenre("Fiction");
        oneTestBook.setImage(
                "https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG");
        oneTestBook.setIsbn("9789583061738");
        oneTestBook.setPages(266);
        oneTestBook.setPublisher("PANAMERICANA");
        oneTestBook.setSubtitle("NA");
        oneTestBook.setYear("1962");
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> bookRepository.save(oneTestBook));
    }
}
