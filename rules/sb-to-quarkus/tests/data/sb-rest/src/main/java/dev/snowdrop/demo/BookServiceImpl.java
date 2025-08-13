package dev.snowdrop.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BookServiceImpl implements BookService {
    private final List<Book> books = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        books.add(new Book(1, "The Hobbit", "J.R.R. Tolkien"));
    }

    public BookServiceImpl() {
        // Sample data for books
        books.add(new Book(1, "The Great Gatsby", "F. Scott Fitzgerald"));
        books.add(new Book(2, "1984", "George Orwell"));
        books.add(new Book(3, "To Kill a Mockingbird", "Harper Lee"));
    }

    @Override
    public List<Book> getAllBooks() {
        return books;
    }

    @Override
    public Optional<Book> getBookById(int id) {
        return books.stream()
            .filter(book -> book.getId() == id)
            .findFirst();
    }

    @Override
    public Optional<Book> updateBook(int id, Book bookDetails) {
        Optional<Book> existingBookOptional = getBookById(id);
        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get();
            existingBook.setTitle(bookDetails.getTitle());
            existingBook.setAuthor(bookDetails.getAuthor());
            return Optional.of(existingBook);
        }
        return Optional.empty(); // Return empty if the book to update doesn't exist
    }

    @Override
    public void deleteBook(int id) {
        books.removeIf(book -> book.getId() == id);
    }

}