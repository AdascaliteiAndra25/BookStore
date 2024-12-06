package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.model.BookDTO;

import java.util.List;

public class CustomerView {

    private TableView<BookDTO> bookTableView;
    private final ObservableList<BookDTO> booksObservableList;
    private Button buyButton;


    public CustomerView(Stage primaryStage, List<BookDTO> books) {
        primaryStage.setTitle("Customer Panel");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 900, 500);
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(books);

        initTableView(gridPane);
        initCustomerOptions(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane) {
        bookTableView = new TableView<>();
        bookTableView.setPlaceholder(new Label("No books to display"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookDTO, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<BookDTO, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        bookTableView.getColumns().addAll(titleColumn, authorColumn, priceColumn, stockColumn);

        bookTableView.setItems(booksObservableList);
        gridPane.add(bookTableView, 0, 0, 4, 1);
    }

    private void initCustomerOptions(GridPane gridPane) {
        buyButton = new Button("Buy");
        gridPane.add(buyButton, 3, 2);

        GridPane.setHalignment(buyButton, HPos.CENTER);
        GridPane.setValignment(buyButton, VPos.CENTER);


    }

    public void addBuyButtonListener(EventHandler<ActionEvent> buyButtonListener) {
        buyButton.setOnAction(buyButtonListener);
    }



    public void addDisplayAlertMessage(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);

        alert.showAndWait();
    }

    public void addBookToObservableList(BookDTO bookDTO){
        this.booksObservableList.add(bookDTO);
    }

    public void removeBookFromObservableList(BookDTO bookDTO){
        this.booksObservableList.remove(bookDTO);
    }


    public TableView<BookDTO> getBookTableView() {
        return bookTableView;
    }

    public ObservableList<BookDTO> getBooksObservableList() {
        return booksObservableList;
    }
}
