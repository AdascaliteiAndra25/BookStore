package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import model.Book;
import service.book.BookService;
import view.CustomerView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class CustomerController {

    private final CustomerView customerView;
    private final BookService bookService;

    public CustomerController(CustomerView customerView, BookService bookService) {
        this.customerView = customerView;
        this.bookService = bookService;

        this.customerView.addBuyButtonListener(new BuyButtonListener());

    }

    private class BuyButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = (BookDTO) customerView.getBookTableView().getSelectionModel().getSelectedItem();

            if (bookDTO != null) {


                Book bookToBuy = BookMapper.convertBookDTOToBook(bookDTO);

                boolean isBought = bookService.buyBook(bookToBuy);

                if (isBought) {
                    bookDTO.setStock(bookDTO.getStock() - 1);

                    if (bookDTO.getStock() == 0) {
                        customerView.removeBookFromObservableList(bookDTO);
                    } else {
                        customerView.getBookTableView().refresh();
                    }
                    customerView.addDisplayAlertMessage("Success", "Book Bought", "The book was successfully bought!");
                } else {
                    customerView.addDisplayAlertMessage("Error", "Cannot Buy Book", "The book could not be bought. Ensure it has stock.");
                }

            } else {
                customerView.addDisplayAlertMessage("Error", "No Book Selected", "Please select a book to buy.");
            }
        }
    }

}



