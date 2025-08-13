package dev.snowdrop.demo;

import java.util.List;
import java.util.Optional;

public interface BookService {
    /**
     * Retrieves all books.
     * @return a list of all books.
     */
    List<Book> getAllBooks();

    /**
     * Finds a book by its ID.
     *
     * @param id The ID of the book to find.
     * @return an Optional containing the book if found, or an empty Optional.
     */
    Optional<Book> getBookById(int id);

    /**
     * Updates an existing book.
     * @param id The ID of the book to update.
     * @param bookDetails The new details for the book.
     * @return an Optional containing the updated book if it exists, or an empty Optional.
     */
    Optional<Book> updateBook(int id, Book bookDetails);

    /**
     * Delete a Book
     * @param id The ID of the book to update.
     */
    void deleteBook(int id);
}
