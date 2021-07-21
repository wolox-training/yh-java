package wolox.training.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@WebMvcTest(BookController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
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
    void whenFindAllThenAllBooksAreReturned() throws Exception {
        List<Book> allBooks = Arrays.asList(oneTestBook);
        when(bookRepository.findAll()).thenReturn(allBooks);
        String url = "/api/books";
        mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "[{\n"
                                + "        \"id\": 0,\n"
                                + "        \"genre\": \"Fiction\",\n"
                                + "        \"author\": \"Fernando Soto Aparicio\",\n"
                                + "        \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "        \"title\": \"The Rebellion of the Rats\",\n"
                                + "        \"subtitle\": \"NA\",\n"
                                + "        \"publisher\": \"PANAMERICANA\",\n"
                                + "        \"year\": \"1962\",\n"
                                + "        \"pages\": 266,\n"
                                + "        \"isbn\": \"9789583061738\"\n"
                                + "}]"
                ));
    }

    @Test
    void whenFindByIdThenBookIsReturned() throws Exception {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(oneTestBook));
        String url = "/api/books/0";
        mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{\n"
                                + "        \"id\": 0,\n"
                                + "        \"genre\": \"Fiction\",\n"
                                + "        \"author\": \"Fernando Soto Aparicio\",\n"
                                + "        \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "        \"title\": \"The Rebellion of the Rats\",\n"
                                + "        \"subtitle\": \"NA\",\n"
                                + "        \"publisher\": \"PANAMERICANA\",\n"
                                + "        \"year\": \"1962\",\n"
                                + "        \"pages\": 266,\n"
                                + "        \"isbn\": \"9789583061738\"\n"
                                + "}"
                ));
    }

    @Test
    void whenFindByIdThenThrowBookNotFoundException() throws Exception {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        String url = "/api/books/10";
        mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenCreateBookThenBookIsCreated() throws Exception {
        String json = "{\n"
                + "        \"genre\": \"Fiction\",\n"
                + "        \"author\": \"Fernando Soto Aparicio\",\n"
                + "        \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                + "        \"title\": \"The Rebellion of the Rats\",\n"
                + "        \"subtitle\": \"NA\",\n"
                + "        \"publisher\": \"PANAMERICANA\",\n"
                + "        \"year\": \"1962\",\n"
                + "        \"pages\": 266,\n"
                + "        \"isbn\": \"9789583061738\"\n"
                + "}";
        when(bookRepository.save(any())).thenReturn(oneTestBook);
        String url = "/api/books";
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{\n"
                                + "        \"id\": 0,\n"
                                + "        \"genre\": \"Fiction\",\n"
                                + "        \"author\": \"Fernando Soto Aparicio\",\n"
                                + "        \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "        \"title\": \"The Rebellion of the Rats\",\n"
                                + "        \"subtitle\": \"NA\",\n"
                                + "        \"publisher\": \"PANAMERICANA\",\n"
                                + "        \"year\": \"1962\",\n"
                                + "        \"pages\": 266,\n"
                                + "        \"isbn\": \"9789583061738\"\n"
                                + "}"
                ));
    }

    @Test
    void whenDeleteBookThenBookIsDeleted() throws Exception {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(oneTestBook));
        Mockito.doNothing().when(bookRepository).deleteById(anyLong());

        String url = "/api/books/0";
        mvc.perform(MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void whenDeleteBookThenThrowBookNotFoundException() throws Exception {
        when(bookRepository.findById(anyLong())).thenThrow(new BookNotFoundException());
        Mockito.doNothing().when(bookRepository).deleteById(anyLong());

        String url = "/api/books/0";
        mvc.perform(MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenUpdateBookThenBookIsUpdated() throws Exception {
        String json = "{\n"
                + "        \"id\": 0,\n"
                + "        \"genre\": \"Fiction\",\n"
                + "        \"author\": \"Fernando Soto Aparicio\",\n"
                + "        \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                + "        \"title\": \"The Rebellion of the Rats\",\n"
                + "        \"subtitle\": \"NA\",\n"
                + "        \"publisher\": \"PANAMERICANA\",\n"
                + "        \"year\": \"1962\",\n"
                + "        \"pages\": 266,\n"
                + "        \"isbn\": \"9789583061738\"\n"
                + "}";
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(oneTestBook));
        when(bookRepository.save(any())).thenReturn(oneTestBook);
        String url = "/api/books/0";
        mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{\n"
                                + "        \"id\": 0,\n"
                                + "        \"genre\": \"Fiction\",\n"
                                + "        \"author\": \"Fernando Soto Aparicio\",\n"
                                + "        \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "        \"title\": \"The Rebellion of the Rats\",\n"
                                + "        \"subtitle\": \"NA\",\n"
                                + "        \"publisher\": \"PANAMERICANA\",\n"
                                + "        \"year\": \"1962\",\n"
                                + "        \"pages\": 266,\n"
                                + "        \"isbn\": \"9789583061738\"\n"
                                + "}"
                ));
    }

    @Test
    void whenUpdateBookThenThrowBookIdMismatchException() throws Exception {
        String json = "{\n"
                + "        \"id\": 4,\n"
                + "        \"genre\": \"Fiction\",\n"
                + "        \"author\": \"Fernando Soto Aparicio\",\n"
                + "        \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                + "        \"title\": \"The Rebellion of the Rats\",\n"
                + "        \"subtitle\": \"NA\",\n"
                + "        \"publisher\": \"PANAMERICANA\",\n"
                + "        \"year\": \"1962\",\n"
                + "        \"pages\": 266,\n"
                + "        \"isbn\": \"9789583061738\"\n"
                + "}";
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(oneTestBook));
        when(bookRepository.save(any())).thenReturn(oneTestBook);
        String url = "/api/books/0";
        mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
