package service.book;

import model.Book;
import repository.book.BookRepository;
import service.book.BookService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository=bookRepository;
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
        Book book=this.findById(id);
        LocalDate now =LocalDate.now();

        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }

    @Override
    public boolean bookExists(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title,author).isPresent();
    }

    @Override
    public boolean sellBook(Long id) {

        Book book = bookRepository.findById(id).orElse(null);


        if (book != null) {
            System.out.println("Found book: " + book.getTitle() + " with stock: " + book.getStock());


            if (book.getStock() > 0) {

                return bookRepository.sellBook(book);
            } else {

                System.out.println("Book is out of stock.");
                return false;
            }
        } else {

            System.out.println("Book not found with id: ");
            return false;
        }
    }


//    public boolean sellBook(String title, String author) {
//        Book book = bookRepository.findByTitleAndAuthor(title, author).orElse(null);
//
//        if(book != null && book.getStock()>0){
//            return bookRepository.sellBook(book);
//        }else {
//            System.out.println("Book not found or out of stock.");
//            return false;
//        }
//        //return false;
//    }

//    @Override
//    public boolean sellBook(Long id) {
//        Book book = bookRepository.findById(id).orElse(null);
//        if(book != null && book.getStock()>0){
//            return bookRepository.sellBook(book);
//        }else {
//            System.out.println("Book not found or out of stock.");
//            return false;
//        }
//        //return false;
//    }


}
