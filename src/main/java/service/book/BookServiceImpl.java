package service.book;

import model.Book;
import model.Order;
import repository.book.BookRepository;
import service.book.BookService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findSoldBooks() {
        return bookRepository.findSoldBooks();
    }

    @Override
    public List<Order> findAllOrders() {
        return bookRepository.findAllOrders();
    }

    @Override
    public List<Order> findOrdersForLastMonth() {
        return bookRepository.findOrdersForLastMonth();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id: %d was not found.".formatted(id)));
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        return bookRepository.delete(book);
    }

    @Override
    public int getAgeOgBook(Long id) {
        Book book = this.findById(id);
        LocalDate now = LocalDate.now();

        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }


    public boolean sellBook(Book book, Long id) {
        if (book == null || book.getStock() == 0) {
            System.out.println("Cannot sell book. Book out of stock!");
            return false;
        }
        return bookRepository.sellBook(book, id);
    }

    @Override
    public boolean buyBook(Book book) {
        System.out.println("Buna ziua");
        if (book == null || book.getStock() == 0) {
            System.out.println("Cannot sell book. Book out of stock!");
            return false;
        }
        System.out.println("Buna seara");
        return bookRepository.update(book);
    }

}

