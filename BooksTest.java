import org.example.Books.Book;
import org.example.Books.Service.BooksDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class BooksTest extends Assertions {
    BooksDao booksDao = new BooksDao();

    public BooksTest() throws SQLException {
    }

    @Test
    public void testGetAll_Add() throws SQLException {
        Book book = new Book(100, "a", "b");
        booksDao.addBook(book);
        ArrayList<Book> array = booksDao.getBooks();

        assertNotNull(array);
    }

    @Test
    public void delete() throws SQLException {
        Book book = new Book(101, "a", "b");
        booksDao.addBook(book);
        booksDao.deleteBook(101);
        Book newBook = booksDao.getBook(101);

        assertNull(newBook);
    }

    @Test
    public void update() throws SQLException {
        Book book = new Book(100, "a", "x");

        Book oldbook = booksDao.getBook(100);

        booksDao.updateBook(book);

        Book newBook = booksDao.getBook(100);

        assertNotEquals(newBook.getAuthor(), oldbook.getAuthor());
    }
}
