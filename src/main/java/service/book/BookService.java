package service.book;

import model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();
    List<Book> findSoldBooks();
    Book findById(Long id);
    boolean save(Book book);
    boolean delete(Book book);
    int getAgeOgBook(Long id);
    boolean bookExists(String title, String author);

    boolean sellBook(Long id);

}
