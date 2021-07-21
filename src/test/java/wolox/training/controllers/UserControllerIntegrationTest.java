package wolox.training.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
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
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@WebMvcTest(UserController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookRepository bookRepository;

    private User oneTestUser;
    private Book oneTestBook;
    private Book twoTestBook;

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
        oneTestUser = new User();
        oneTestUser.setUsername("Ferho");
        oneTestUser.setName("Yesid Hoyos");
        oneTestUser.setBirthday(LocalDate.of(1995, 06, 06));
        List<Book> books = new LinkedList<>(Arrays.asList(oneTestBook));
        oneTestUser.setBooks(books);
        twoTestBook = new Book();
        twoTestBook.setId(5);
        twoTestBook.setAuthor("Fernando Soto Aparicio");
        twoTestBook.setTitle("The Rebellion of the Rats");
        twoTestBook.setGenre("Fiction");
        twoTestBook.setImage(
                "https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG");
        twoTestBook.setIsbn("111111111");
        twoTestBook.setPages(266);
        twoTestBook.setPublisher("PANAMERICANA");
        twoTestBook.setSubtitle("NA");
        twoTestBook.setYear("1962");
    }

    @Test
    void whenFindAllThenAllUsersAreReturned() throws Exception {
        List<User> allUsers = Arrays.asList(oneTestUser);
        when(userRepository.findAll()).thenReturn(allUsers);
        String url = "/api/users";
        mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "[{\n"
                                + "    \"id\": 0,\n"
                                + "    \"username\": \"Ferho\",\n"
                                + "    \"name\": \"Yesid Hoyos\",\n"
                                + "    \"birthday\": \"1995-06-06\",\n"
                                + "    \"books\": [\n"
                                + "        {\n"
                                + "            \"id\": 0,\n"
                                + "            \"genre\": \"Fiction\",\n"
                                + "            \"author\": \"Fernando Soto Aparicio\",\n"
                                + "            \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "            \"title\": \"The Rebellion of the Rats\",\n"
                                + "            \"subtitle\": \"NA\",\n"
                                + "            \"publisher\": \"PANAMERICANA\",\n"
                                + "            \"year\": \"1962\",\n"
                                + "            \"pages\": 266,\n"
                                + "            \"isbn\": \"9789583061738\"\n"
                                + "        }\n"
                                + "    ]\n"
                                + "}]"
                ));
    }

    @Test
    void whenFindByIdThenUserIsReturned() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oneTestUser));
        String url = "/api/users/0";
        mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{\n"
                                + "    \"id\": 0,\n"
                                + "    \"username\": \"Ferho\",\n"
                                + "    \"name\": \"Yesid Hoyos\",\n"
                                + "    \"birthday\": \"1995-06-06\",\n"
                                + "    \"books\": [\n"
                                + "        {\n"
                                + "            \"id\": 0,\n"
                                + "            \"genre\": \"Fiction\",\n"
                                + "            \"author\": \"Fernando Soto Aparicio\",\n"
                                + "            \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "            \"title\": \"The Rebellion of the Rats\",\n"
                                + "            \"subtitle\": \"NA\",\n"
                                + "            \"publisher\": \"PANAMERICANA\",\n"
                                + "            \"year\": \"1962\",\n"
                                + "            \"pages\": 266,\n"
                                + "            \"isbn\": \"9789583061738\"\n"
                                + "        }\n"
                                + "    ]\n"
                                + "}"
                ));
    }

    @Test
    void whenFindByIdThenThrowUserNotFoundException() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        String url = "/api/users/10";
        mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenCreateUserThenUserIsCreated() throws Exception {
        String json = "{\n"
                                + "    \"username\": \"Ferho\",\n"
                                + "    \"name\": \"Yesid Hoyos\",\n"
                                + "    \"birthday\": \"1995-06-06\",\n"
                                + "    \"books\": [\n"
                                + "        {\n"
                                + "            \"genre\": \"Fiction\",\n"
                                + "            \"author\": \"Fernando Soto Aparicio\",\n"
                                + "            \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "            \"title\": \"The Rebellion of the Rats\",\n"
                                + "            \"subtitle\": \"NA\",\n"
                                + "            \"publisher\": \"PANAMERICANA\",\n"
                                + "            \"year\": \"1962\",\n"
                                + "            \"pages\": 266,\n"
                                + "            \"isbn\": \"9789583061738\"\n"
                                + "        }\n"
                                + "    ]\n"
                                + "}";
        when(userRepository.save(any())).thenReturn(oneTestUser);
        String url = "/api/users";
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{\n"
                                + "    \"id\": 0,\n"
                                + "    \"username\": \"Ferho\",\n"
                                + "    \"name\": \"Yesid Hoyos\",\n"
                                + "    \"birthday\": \"1995-06-06\",\n"
                                + "    \"books\": [\n"
                                + "        {\n"
                                + "            \"id\": 0,\n"
                                + "            \"genre\": \"Fiction\",\n"
                                + "            \"author\": \"Fernando Soto Aparicio\",\n"
                                + "            \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "            \"title\": \"The Rebellion of the Rats\",\n"
                                + "            \"subtitle\": \"NA\",\n"
                                + "            \"publisher\": \"PANAMERICANA\",\n"
                                + "            \"year\": \"1962\",\n"
                                + "            \"pages\": 266,\n"
                                + "            \"isbn\": \"9789583061738\"\n"
                                + "        }\n"
                                + "    ]\n"
                                + "}"
                ));
    }

    @Test
    void whenDeleteUserThenUserIsDeleted() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oneTestUser));
        Mockito.doNothing().when(userRepository).deleteById(anyLong());

        String url = "/api/users/0";
        mvc.perform(MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void whenDeleteUserThenThrowUserNotFoundException() throws Exception {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException());
        Mockito.doNothing().when(userRepository).deleteById(anyLong());

        String url = "/api/users/0";
        mvc.perform(MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenUpdateUserThenUserIsUpdated() throws Exception {
        String json = "{\n"
                + "    \"username\": \"Ferho\",\n"
                + "    \"name\": \"Yesid Hoyos\",\n"
                + "    \"birthday\": \"1995-06-06\",\n"
                + "    \"books\": [\n"
                + "        {\n"
                + "            \"genre\": \"Fiction\",\n"
                + "            \"author\": \"Fernando Soto Aparicio\",\n"
                + "            \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                + "            \"title\": \"The Rebellion of the Rats\",\n"
                + "            \"subtitle\": \"NA\",\n"
                + "            \"publisher\": \"PANAMERICANA\",\n"
                + "            \"year\": \"1962\",\n"
                + "            \"pages\": 266,\n"
                + "            \"isbn\": \"9789583061738\"\n"
                + "        }\n"
                + "    ]\n"
                + "}";
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oneTestUser));
        when(userRepository.save(any())).thenReturn(oneTestUser);
        String url = "/api/users/0";
        mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{\n"
                                + "    \"id\": 0,\n"
                                + "    \"username\": \"Ferho\",\n"
                                + "    \"name\": \"Yesid Hoyos\",\n"
                                + "    \"birthday\": \"1995-06-06\",\n"
                                + "    \"books\": [\n"
                                + "        {\n"
                                + "            \"id\": 0,\n"
                                + "            \"genre\": \"Fiction\",\n"
                                + "            \"author\": \"Fernando Soto Aparicio\",\n"
                                + "            \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "            \"title\": \"The Rebellion of the Rats\",\n"
                                + "            \"subtitle\": \"NA\",\n"
                                + "            \"publisher\": \"PANAMERICANA\",\n"
                                + "            \"year\": \"1962\",\n"
                                + "            \"pages\": 266,\n"
                                + "            \"isbn\": \"9789583061738\"\n"
                                + "        }\n"
                                + "    ]\n"
                                + "}"
                ));
    }

    @Test
    void whenUpdateUserThenThrowUserIdMismatchException() throws Exception {
        String json = "{\n"
                + "    \"id\": 5,\n"
                + "    \"username\": \"Ferho\",\n"
                + "    \"name\": \"Yesid Hoyos\",\n"
                + "    \"birthday\": \"1995-06-06\",\n"
                + "    \"books\": [\n"
                + "        {\n"
                + "            \"genre\": \"Fiction\",\n"
                + "            \"author\": \"Fernando Soto Aparicio\",\n"
                + "            \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                + "            \"title\": \"The Rebellion of the Rats\",\n"
                + "            \"subtitle\": \"NA\",\n"
                + "            \"publisher\": \"PANAMERICANA\",\n"
                + "            \"year\": \"1962\",\n"
                + "            \"pages\": 266,\n"
                + "            \"isbn\": \"9789583061738\"\n"
                + "        }\n"
                + "    ]\n"
                + "}";
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oneTestUser));
        when(userRepository.save(any())).thenReturn(oneTestUser);
        String url = "/api/users/0";
        mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void whenAddBookToUserThenBookIsAdded() throws Exception {
        String json = "{\n"
                + "         \"id\": 5,\n"
                + "         \"genre\": \"Fiction\",\n"
                + "         \"author\": \"Fernando Soto Aparicio\",\n"
                + "         \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                + "         \"title\": \"The Rebellion of the Rats\",\n"
                + "         \"subtitle\": \"NA\",\n"
                + "         \"publisher\": \"PANAMERICANA\",\n"
                + "         \"year\": \"1962\",\n"
                + "         \"pages\": 266,\n"
                + "         \"isbn\": \"111111111\"\n"
                + "}";
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oneTestUser));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(twoTestBook));
        when(userRepository.save(any())).thenReturn(oneTestUser);
        String url = "/api/users/0/books";
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{\n"
                                + "    \"id\": 0,\n"
                                + "    \"username\": \"Ferho\",\n"
                                + "    \"name\": \"Yesid Hoyos\",\n"
                                + "    \"birthday\": \"1995-06-06\",\n"
                                + "    \"books\": [\n"
                                + "        {\n"
                                + "            \"id\": 0,\n"
                                + "            \"genre\": \"Fiction\",\n"
                                + "            \"author\": \"Fernando Soto Aparicio\",\n"
                                + "            \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "            \"title\": \"The Rebellion of the Rats\",\n"
                                + "            \"subtitle\": \"NA\",\n"
                                + "            \"publisher\": \"PANAMERICANA\",\n"
                                + "            \"year\": \"1962\",\n"
                                + "            \"pages\": 266,\n"
                                + "            \"isbn\": \"9789583061738\"\n"
                                + "        },\n"
                                + "        {\n"
                                + "            \"id\": 5,\n"
                                + "            \"genre\": \"Fiction\",\n"
                                + "            \"author\": \"Fernando Soto Aparicio\",\n"
                                + "            \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                                + "            \"title\": \"The Rebellion of the Rats\",\n"
                                + "            \"subtitle\": \"NA\",\n"
                                + "            \"publisher\": \"PANAMERICANA\",\n"
                                + "            \"year\": \"1962\",\n"
                                + "            \"pages\": 266,\n"
                                + "            \"isbn\": \"111111111\"\n"
                                + "        }\n"
                                + "    ]\n"
                                + "}"
                ));
    }

    @Test
    void whenAddBookToUserThenThrowBookAlreadyOwnedException() throws Exception {
        String json = "{\n"
                + "         \"id\": 0,\n"
                + "         \"genre\": \"Fiction\",\n"
                + "         \"author\": \"Fernando Soto Aparicio\",\n"
                + "         \"image\": \"https://storage.googleapis.com/catalogo-libros/extralarge/c8654639-3a01-85e2-5bf9-5fc94393c20c_imagen.PNG\",\n"
                + "         \"title\": \"The Rebellion of the Rats\",\n"
                + "         \"subtitle\": \"NA\",\n"
                + "         \"publisher\": \"PANAMERICANA\",\n"
                + "         \"year\": \"1962\",\n"
                + "         \"pages\": 266,\n"
                + "         \"isbn\": \"9789583061738\"\n"
                + "}";
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oneTestUser));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(oneTestBook));
        when(userRepository.save(any())).thenReturn(oneTestUser);
        String url = "/api/users/0/books";
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void whenDeleteBookFromUserThenBookIsDeleted() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oneTestUser));
        when(userRepository.save(any())).thenReturn(oneTestUser);
        String url = "/api/users/0/books/0";
        mvc.perform(MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "{\n"
                                + "    \"id\": 0,\n"
                                + "    \"username\": \"Ferho\",\n"
                                + "    \"name\": \"Yesid Hoyos\",\n"
                                + "    \"birthday\": \"1995-06-06\",\n"
                                + "    \"books\": [\n"
                                + "    ]\n"
                                + "}"
                ));
    }
}
