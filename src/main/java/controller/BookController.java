package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.CustomerComponentFactory;
import mapper.BookMapper;
import model.Book;
import model.builder.BookBuilder;
import service.book.BookService;
import view.BookView;
import view.LoginView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;

    public BookController(BookView bookView, BookService bookService){
        this.bookView=bookView;
        this.bookService=bookService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addSellButtonListener(new SellButtonListener());
        //this.bookView.addBackButtonListener(new BackButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            double price = bookView.getPrice();
            int stock = bookView.getStock();

            System.out.println("Title: " + title);
            System.out.println("Author: " + author);
            System.out.println("Price: " + price);
            System.out.println("Stock: " + stock);

            if( title.isEmpty() || author.isEmpty()){
                bookView.addDiasplayAlertMessage("Save Error", "Problem at Author or Title fields", "Can not have an empty Title or Author field.");
            }
                else {
                BookDTO bookDTO = new BookDTOBuilder().setTitle(title).setAuthor(author).setPrice(price).setStock(stock).build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if (savedBook){
                    bookView.getBookTableView().refresh();
                    System.out.println("LAICI");
                    System.out.println(bookDTO.getId());

                    bookView.addDiasplayAlertMessage("Save Successful", "Book Added", "Book was successfully added to the database.");

                    bookView.addBookToObservableList(bookDTO);
                }else {
                    bookView.addDiasplayAlertMessage("Save Error", "Problem at adding Book", "There was a problem at adding the book to the database. Please try again.");

                }
            }



    }}




    private class DeleteButtonListener implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();

            if (bookDTO != null){
                boolean deleteSuccessful= bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));

                if(deleteSuccessful){
                    bookView.addDiasplayAlertMessage("Delete Successful", "Book Deleted", "Book was successfully deleted from the database.");
                    bookView.removeBookFromObservableList(bookDTO);
                } else {
                    bookView.addDiasplayAlertMessage("Delete Error", "Problem at deleting Book", "There was a problem with the database. Please try again.");
                }

            } else {
                bookView.addDiasplayAlertMessage("Delete Error", "Problem at deleting book", "You must select a book before pressing the delete button.");
            }
        }
    }

//    private class SellButtonListener implements EventHandler<ActionEvent>{
//
//        @Override
//        public void handle(ActionEvent actionEvent) {
//            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
//
//            if (bookDTO != null){
//
//                boolean isSold = bookService.sellBook(BookMapper.convertBookDTOToBook(bookDTO).getId());
//
//                if(isSold) {
//                    bookDTO.setStock(bookDTO.getStock()-1);
//                    bookView.getBookTableView().refresh();
//                    bookView.addDiasplayAlertMessage("Success", "Book Sold", "The book was successfully sold!");
//                    bookView.addSoldBookToObservableList(bookDTO);
//
//                }else{
//                    bookView.addDiasplayAlertMessage("Error", "Cannot Sell Book", "The book could not be sold. Ensure it has stock.");
//
//                }
//            }else {
//                bookView.addDiasplayAlertMessage("Error", "No Book Selected", "Please select a book to sell.");
//
//            }
//        }
//    }

    private class SellButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();

            if (bookDTO != null) {
                System.out.println("IDdto:");
                System.out.println(bookDTO.getId());

                boolean isSold = bookService.sellBook(bookDTO.getId());

                if (isSold) {
                    bookDTO.setStock(bookDTO.getStock() - 1);

                    if (bookDTO.getStock() == 0) {

                        bookView.removeBookFromObservableList(bookDTO);
                    } else {

                        bookView.getBookTableView().refresh();
                    }

                    bookView.getBookTableView().refresh();
                    BookDTO soldBook = new BookDTOBuilder()
                            .setId(bookDTO.getId())
                            .setTitle(bookDTO.getTitle())
                            .setAuthor(bookDTO.getAuthor())
                            .setPrice(bookDTO.getPrice())
                            .setStock(1) // Stocul vândut
                            .build();
                    bookView.addDiasplayAlertMessage("Success", "Book Sold", "The book was successfully sold!");
                    bookView.addSoldBookToObservableList(soldBook);
                } else {
                    bookView.addDiasplayAlertMessage("Error", "Cannot Sell Book", "The book could not be sold. Ensure it has stock.");
                }

            } else {
                bookView.addDiasplayAlertMessage("Error", "No Book Selected", "Please select a book to sell.");
            }
        }
    }



//    private class BackButtonListener implements EventHandler<ActionEvent>{
//
//        @Override
//        public void handle(ActionEvent actionEvent) {
//            Stage current =(Stage) bookView.getBookTableView().getScene().getWindow();
//            current.close();
//            Stage loginView =new Stage();
//            new LoginView(loginView);
//            loginView.show();
//
//        }
//    }

}
