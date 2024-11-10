import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.BookRepository;
import repository.BookRepositoryMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRepositoryMySQLTest {

    private static BookRepository bookRepository;

    @BeforeAll
    public static void setup() throws SQLException {
        Connection connection= DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        bookRepository=new BookRepositoryMySQL(connection);
       // bookRepository.removeAll();

    }

    @Test
    public void findAll(){
        List<Book> books= bookRepository.findAll();
        assertEquals(0, books.size());
    }

    @Test
    public void findById(){
        final Optional<Book> book = bookRepository.findById(1L);
        assertTrue(book.isEmpty());
    }

    @Test
    public void save(){
        assertTrue(bookRepository.save(new BookBuilder().setTitle("Ion").setAuthor("Liviu Rebreanu").setPublishedDate(LocalDate.of(1980,10,2)).build()));
    }

    @Test
    public void delete(){
        Book bookMoaraCuNoroc= new BookBuilder().setAuthor("Ioan Slavici").setTitle("Moara cu noroc").setPublishedDate(LocalDate.of(1950,2,10)).build();
        bookRepository.save(bookMoaraCuNoroc);
        assertTrue(bookRepository.delete(bookMoaraCuNoroc));
    }

}
