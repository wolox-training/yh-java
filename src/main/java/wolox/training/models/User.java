package wolox.training.models;

import com.google.common.base.Preconditions;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import wolox.training.exceptions.BookAlreadyOwnedException;

/**
 * Represents an User
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_user",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List <Book> books = Collections.emptyList();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Preconditions.checkNotNull(username, "Illegal Argument, username is Null.");
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name, "Illegal Argument, name is Null.");
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        Preconditions.checkNotNull(birthday, "Illegal Argument, birthday is Null.");
        this.birthday = birthday;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        Preconditions.checkNotNull(books, "Illegal Argument, books is Null.");
        this.books = books;
    }

    public void addBook(Book book) {
        Optional<Book> bookOptional = books.stream().filter(bookL -> bookL.getId() == book.getId()).findFirst();
        if (bookOptional.isPresent()) {
            throw new BookAlreadyOwnedException();
        }
        books.add(book);
    }

    public void removeBook(long bookId) {
        Optional<Book> bookOptional= books.stream().filter(book -> book.getId() == bookId).findFirst();
        if(bookOptional.isPresent()) {
            books.remove(bookOptional.get());
        }
    }
}
