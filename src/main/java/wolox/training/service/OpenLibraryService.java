package wolox.training.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import wolox.training.dto.BookDto;

@Service
public class OpenLibraryService {

    private static final String FIND_BOOK_BY_ISBN_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:%s&format=json&jscmd=data";

    private WebClient webClient;

    public OpenLibraryService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Optional<BookDto> bookInfo(String isbn) throws JsonProcessingException {
        String responseBody = getBookInfo(isbn);
        JsonNode bookOpenLibraryJson = buildBookOpenLibraryJson(responseBody);
        if (bookOpenLibraryJson.isEmpty()) {
            return Optional.empty();
        }
        String bookJson = bookOpenLibraryJson.get("ISBN:".concat(isbn)).toString();
        BookDto bookDto = buildBookDto(isbn, bookJson);
        return Optional.of(bookDto);
    }

    private JsonNode buildBookOpenLibraryJson(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode bookOpenLibraryJson;
        bookOpenLibraryJson = objectMapper.readTree(responseBody);
        return bookOpenLibraryJson;
    }

    private BookDto buildBookDto(String isbn, String bookJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        BookDto bookDto;
        bookDto = objectMapper.readValue(bookJson, BookDto.class);
        bookDto.setISBN(isbn);
        return bookDto;
    }

    private String getBookInfo(String isbn) {
        return webClient
                .get()
                .uri("https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(error -> {
                    return Mono.empty();
                })
                .block();
    }
}
