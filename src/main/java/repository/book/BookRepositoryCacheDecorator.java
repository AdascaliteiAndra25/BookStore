package repository.book;

import model.Book;
import model.Order;

import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator {

    private Cache<Book> cache;
    private Cache<Order> soldBooksCache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache, Cache<Order> soldBooksCache){
        super(bookRepository);
        this.cache=cache;
        this.soldBooksCache=soldBooksCache;

    }
    @Override
    public List<Book> findAll() {

        if(cache.hasResult()){
            return cache.load();
        }

        List<Book> books=decoratedbookRepository.findAll(); //mergem pana in BAZA DE DATE
        cache.save(books); //incarc cache

        return books;
    }

    @Override
    public List<Book> findSoldBooks() {

        List<Book> soldBooks = decoratedbookRepository.findSoldBooks();

        return soldBooks;
    }

    @Override
    public List<Order> findAllOrders() {
        if (soldBooksCache.hasResult()) {
            return soldBooksCache.load();
        }
        List<Order> orders = decoratedbookRepository.findAllOrders();
        soldBooksCache.save(orders);

        return orders;
    }

    @Override
    public List<Order> findOrdersForLastMonth() {
        if (soldBooksCache.hasResult()) {
            return soldBooksCache.load();
        }

        List<Order> orders = decoratedbookRepository.findOrdersForLastMonth();
        soldBooksCache.save(orders);

        return orders;
    }

    @Override
    public Optional<Book> findById(Long id) {
        if(cache.hasResult()){
            return cache.load().stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst();
        }
        return decoratedbookRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedbookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        cache.invalidateCache();
        return decoratedbookRepository.delete(book);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedbookRepository.removeAll();

    }




    @Override
    public boolean sellBook(Book book, Long id) {
        cache.invalidateCache();
        soldBooksCache.invalidateCache();
        return decoratedbookRepository.sellBook(book,id);
    }

    @Override
    public boolean update(Book book) {
        cache.invalidateCache();
        return decoratedbookRepository.update(book);
    }


}
