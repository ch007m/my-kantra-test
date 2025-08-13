package dev.snowdrop.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookRestController {
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String home() {
        return "Welcome to the Book API!";
    }

    @GetMapping("/findbyid/{id}")
    public Optional<Book> findBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    @GetMapping("/findall")
    public List<Book> findAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/book/{id}")
    public Book saveBook(@PathVariable int id) {
        Book book = new Book(id, "Test" + Integer.toString(id), "Test" + Integer.toString(id) );
        return book;
    }

    @DeleteMapping("/book/{id}")
    public String deleteAllBooks(@PathVariable int id) {
        bookService.deleteBook(id);
        return "Books has been deleted.";
    }
}

