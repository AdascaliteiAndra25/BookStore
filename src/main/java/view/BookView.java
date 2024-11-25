package view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import model.Book;
import view.model.BookDTO;

import javax.swing.plaf.PanelUI;
import java.util.List;
public class BookView {

    private TableView<BookDTO> bookTableView;
    private final ObservableList<BookDTO> booksObservableList;
    private TextField authorTextField;
    private TextField titleTextField;

    private TextField priceTextField;
    private TextField stockTextField;
    private Label authorLabel;
    private Label titleLabel;

    private Label priceLabel;
    private Label stockLabel;
    private Button saveButton;
    private Button deleteButton;
    private Button sellButton;

    private TableView soldBooksTableView;
    private final ObservableList<BookDTO> soldBooksObservableList;

    //private Button backButton;

    public BookView(Stage primaryStage, List<BookDTO> books, List<BookDTO> soldBooks){
        primaryStage.setTitle("Library");

        GridPane gridPane=new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 1000, 500);
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(books);
        soldBooksObservableList=FXCollections.observableArrayList(soldBooks);

        initTableView(gridPane);
        initSoldBooksTableView(gridPane);

        initSaveOptions(gridPane);
        primaryStage.show();

    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));

    }

    private void initTableView(GridPane gridPane){
        bookTableView=new TableView<BookDTO>();

        bookTableView.setPlaceholder(new Label("No books to display"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<BookDTO, String>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<BookDTO, String> authorColumn = new TableColumn<BookDTO, String>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<BookDTO, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<BookDTO, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        bookTableView.getColumns().addAll(titleColumn,authorColumn,priceColumn,stockColumn);

        bookTableView.setItems(booksObservableList);

        gridPane.add(bookTableView,0,0,4,1);

        }

        private void initSoldBooksTableView(GridPane gridPane){

            soldBooksTableView = new TableView<BookDTO>();
            soldBooksTableView.setPlaceholder(new Label("No sold books"));

            TableColumn<BookDTO, String> soldTitleColumn = new TableColumn<>("Title");
            soldTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            TableColumn<BookDTO, String> soldAuthorColumn = new TableColumn<>("Author");
            soldAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            TableColumn<BookDTO, Double> soldPriceColumn = new TableColumn<>("Price");
            soldPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            TableColumn<BookDTO, Integer> soldStockColumn = new TableColumn<>("Stock Sold");
            soldStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

            soldBooksTableView.getColumns().addAll(soldTitleColumn, soldAuthorColumn, soldPriceColumn, soldStockColumn);
            soldBooksTableView.setItems(soldBooksObservableList);

            gridPane.add(soldBooksTableView, 4, 0, 4, 1);
        }

        private void initSaveOptions(GridPane gridPane){
        titleLabel=new Label("Title");
        gridPane.add(titleLabel,0,1);

        titleTextField=new TextField();
        gridPane.add(titleTextField,1,1);

        authorLabel=new Label("Author");
        gridPane.add(authorLabel,2,1);

        authorTextField= new TextField();
        gridPane.add(authorTextField,3,1);

        priceLabel = new Label("Price");
        gridPane.add(priceLabel, 0, 2);

        priceTextField = new TextField();
        gridPane.add(priceTextField, 1, 2);

        stockLabel = new Label("Stock");
        gridPane.add(stockLabel, 2, 2);

        stockTextField = new TextField();
        gridPane.add(stockTextField, 3, 2);

        saveButton=new Button("Save");
        gridPane.add(saveButton, 1,3);

        deleteButton=new Button("Delete");
        gridPane.add(deleteButton,2,3);

        sellButton = new Button("Sell");
        gridPane.add(sellButton, 3, 3);

       // backButton=new Button("Back");
        //gridPane.add(backButton,0,4);

        }

        public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener){
        saveButton.setOnAction(saveButtonListener);
        }

        public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener){
        deleteButton.setOnAction(deleteButtonListener);
        }

        public void addSellButtonListener(EventHandler<ActionEvent> sellButtonListener){
        sellButton.setOnAction(sellButtonListener);
        }

       // public void addBackButtonListener(EventHandler<ActionEvent> backButtonListener){
        //backButton.setOnAction(backButtonListener);
        // }



        public void addDiasplayAlertMessage(String title, String header, String context){
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);

        alert.showAndWait();
        }

        public String getTitle(){
            return titleTextField.getText();
        }

        public String getAuthor(){
            return authorTextField.getText();
        }

        public double getPrice(){
            try{
                //System.out.println(Integer.parseInt(priceTextField.getText()));
                return Double.parseDouble(priceTextField.getText());
            }catch (NumberFormatException e){
                return 0.0;
            }
           // return Double.parseDouble(priceTextField.getText());
        }

        public int getStock(){
            try{
                return Integer.parseInt(stockTextField.getText());

            }catch (NumberFormatException e){
                 return 0;
             }
        }



        public void addBookToObservableList(BookDTO bookDTO){
            this.booksObservableList.add(bookDTO);
        }
        public void addSoldBookToObservableList(BookDTO bookDTO) { this.soldBooksObservableList.add(bookDTO);}

        public void removeBookFromObservableList(BookDTO bookDTO){
        this.booksObservableList.remove(bookDTO);
        }

        public TableView getBookTableView(){
            return bookTableView;
        }
        public TableView getSoldBooksTableView(){ return soldBooksTableView; }


}
