package repository.book;

import java.util.List;
import java.util.Optional;

import model.Book;
import model.Order;

//se ocupa doar cu citirea de BD
public interface BookRepository {

    List<Book> findAll();
    List<Book> findSoldBooks();

    List<Order> findAllOrders();

    List<Order> findOrdersForLastMonth();

    Optional<Book> findById(Long id);
    boolean save(Book book);
    boolean delete(Book book);
    void removeAll();

    boolean sellBook(Book book, Long id);

    boolean update(Book book);



}
