package wolox.training.models;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * Represents a Book
 */
@Entity
@ApiModel(description = "Books from the Training API")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String genre;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String image;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String subtitle;
    @Column(nullable = false)
    private String publisher;
    @Column(nullable = false)
    private String year;
    @Column(nullable = false)
    private int pages;
    /**
     * International Standard Book Number
     */
    @Column(nullable = false)
    private String isbn;

    @ManyToMany(mappedBy = "books")
    private List<User> users;

    public Book() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        Preconditions.checkNotNull(author, "Illegal Argument, author is Null.");
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        Preconditions.checkNotNull(image, "Illegal Argument, image is Null.");
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Preconditions.checkNotNull(title, "Illegal Argument, title is Null.");
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        Preconditions.checkNotNull(subtitle, "Illegal Argument, subtitle is Null.");
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        Preconditions.checkNotNull(publisher, "Illegal Argument, publisher is Null.");
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        Preconditions.checkNotNull(year, "Illegal Argument, year is Null.");
        int yearInt = Integer.parseInt(year);
        Preconditions.checkArgument(yearInt <= LocalDate.now().getYear(), "Illegal Argument, year out of range.");
        Preconditions.checkArgument(yearInt > 0, "Illegal Argument, year is negative.");
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        Preconditions.checkNotNull(pages, "Illegal Argument, pages is Null.");
        Preconditions.checkArgument(pages > 0, "Illegal Argument, pages is negative.");
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        Preconditions.checkNotNull(isbn, "Illegal Argument, isbn is Null.");
        this.isbn = isbn;
    }
}
