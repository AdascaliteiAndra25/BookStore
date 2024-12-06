package repository.book;

import model.Book;
import model.Order;
import repository.book.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository {
    private final List<Book> books;
    private final List<Book> soldBooks;

    public BookRepositoryMock(){
        books=new ArrayList<>();
        soldBooks=new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public List<Book> findSoldBooks() {
        return soldBooks;
    }

    @Override
    public List<Order> findAllOrders() {
        return null;
    }

    @Override
    public List<Order> findOrdersForLastMonth() {
        return null;
    }


    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean delete(Book book) {
        return books.remove(book);
    }

    @Override
    public void removeAll() {
        books.clear();

    }

    @Override
    public boolean sellBook(Book book, Long id) {
        return false;
    }



    @Override
    public boolean update(Book book) {
        return false;
    }


}
