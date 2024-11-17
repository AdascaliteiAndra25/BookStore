package repository.book;

import repository.book.BookRepository;

public abstract class BookRepositoryDecorator implements BookRepository {

    protected BookRepository decoratedbookRepository;

    public BookRepositoryDecorator (BookRepository bookRepository){
        decoratedbookRepository=bookRepository;
    }
}
