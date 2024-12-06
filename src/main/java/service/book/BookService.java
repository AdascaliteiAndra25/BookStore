package service.book;

import controller.BookController;
import model.Book;
import model.Order;

import java.util.List;

public interface BookService {

    List<Book> findAll();
    List<Book> findSoldBooks();

    List<Order> findAllOrders();

    List<Order> findOrdersForLastMonth();
    Book findById(Long id);
    boolean save(Book book);
    boolean delete(Book book);
    int getAgeOgBook(Long id);

    boolean sellBook( Book book, Long id);
    boolean buyBook(Book book);

}
