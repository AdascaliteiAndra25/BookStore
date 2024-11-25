package repository.book;

import java.util.List;
import java.util.Optional;

import model.Book;
//se ocupa doar cu citirea de BD
public interface BookRepository {

    List<Book> findAll();
    List<Book> findSoldBooks();

    Optional<Book> findById(Long id);
    boolean save(Book book);
    boolean delete(Book book);
    void removeAll();

    Optional<Book> findByTitleAndAuthor(String title, String author);
    boolean sellBook(Book book);



}
