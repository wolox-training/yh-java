package wolox.training.repository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@DataJpaTest
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private User oneTestUser;

    @BeforeEach
    public void setUp() {
        oneTestUser = new User();
        oneTestUser.setUsername("Ferho");
        oneTestUser.setName("Yesid Hoyos");
        oneTestUser.setBirthday(LocalDate.of(1995, 06, 06));
        List<Book> books = new LinkedList<>();
        oneTestUser.setBooks(books);
    }

    @Test
    void whenSaveUserThenUserIsPersisted() {
        User userSaved = userRepository.save(oneTestUser);
        Assertions.assertEquals(oneTestUser.getUsername(), userSaved.getUsername());
        Assertions.assertEquals(oneTestUser.getName(), userSaved.getName());
        Assertions.assertEquals(oneTestUser.getBirthday(), userSaved.getBirthday());
    }

    @Test
    void whenSaveUserWithoutUsernameThenThrowException() {
        oneTestUser = new User();
        oneTestUser.setName("Yesid Hoyos");
        oneTestUser.setBirthday(LocalDate.of(1995, 06, 06));
        List<Book> books = new LinkedList<>();
        oneTestUser.setBooks(books);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(oneTestUser));
    }
}
