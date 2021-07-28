package wolox.training.util;

import java.util.stream.Collectors;
import wolox.training.dto.Author;
import wolox.training.dto.BookDto;
import wolox.training.dto.Publisher;
import wolox.training.models.Book;

public class BookUtil {

    public static Book dtoToEntity(BookDto bookDto) {
        Book book = new Book();
        String authors = bookDto.getAuthors().stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));
        book.setAuthor(authors);
        book.setTitle(bookDto.getTitle());
        book.setGenre("NA");
        book.setImage("NA");
        book.setIsbn(bookDto.getISBN());
        book.setPages(bookDto.getPagesNumber());
        String publishers = bookDto.getPublishers().stream()
                .map(Publisher::getName)
                .collect(Collectors.joining(", "));
        book.setPublisher(publishers);
        book.setSubtitle(bookDto.getSubtitle() == null ? "NA" : bookDto.getSubtitle());
        String year = getYear(bookDto);
        book.setYear(year);
        return book;
    }

    private static String getYear(BookDto bookDto) {
        String date[] = bookDto.getPublishDate().split(",");
        String year;
        if(date.length == 2) {
            year = date[1].trim();
        } else {
            year = date[0].trim();
        }
        return year;
    }
}
