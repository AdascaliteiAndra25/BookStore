package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.model.BookDTO;
import view.model.UserDTO;


import java.util.List;

public class AdminView {

    private TableView<UserDTO> userTableView;
    private final ObservableList<UserDTO> usersObservableList;
    private TextField usernameTextField;
    private PasswordField passwordTextField;
    private Label usernameLabel;
    private Label passwordLabel;
    private Button addUserButton;
    private Button generateReportButton;
    private Button deleteUserButton;


    public AdminView(Stage primaryStage, List<UserDTO> users) {
        primaryStage.setTitle("Admin Panel");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 900, 500);
        primaryStage.setScene(scene);

        usersObservableList = FXCollections.observableArrayList(users);

        initUserTableView(gridPane);
        initUserAddOptions(gridPane);


        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initUserTableView(GridPane gridPane) {
        userTableView = new TableView<UserDTO>();
        userTableView.setPlaceholder(new Label("No users to display"));

        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        userTableView.getColumns().add(usernameColumn);

        userTableView.setItems(usersObservableList);

        gridPane.add(userTableView, 0, 0, 4, 1);
    }

    private void initUserAddOptions(GridPane gridPane) {
        usernameLabel = new Label("Username");
        gridPane.add(usernameLabel, 0, 1);

        usernameTextField = new TextField();
        gridPane.add(usernameTextField, 1, 1);

        passwordLabel = new Label("Password");
        gridPane.add(passwordLabel, 2, 1);

        passwordTextField = new PasswordField();
        gridPane.add(passwordTextField, 3, 1);

        addUserButton = new Button("Add User");
        gridPane.add(addUserButton, 1, 2);

        deleteUserButton = new Button("Delete");
        gridPane.add(deleteUserButton, 3, 2);

        generateReportButton = new Button("Generate Report");
        gridPane.add(generateReportButton, 5, 2);

    }

    public void addAddUserButtonListener(EventHandler<ActionEvent> addUserButtonListener) {
        addUserButton.setOnAction(addUserButtonListener);
    }

    public void addGenerateReportButtonListener(EventHandler<ActionEvent> generateReportButtonListener) {
        generateReportButton.setOnAction(generateReportButtonListener);
    }

    public void addDeleteUserButtonListener(EventHandler<ActionEvent> deleteUserButtonListener) {
        deleteUserButton.setOnAction(deleteUserButtonListener);
    }


    public void addDisplayAlertMessage(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);

        alert.showAndWait();
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getPassword() {
        return passwordTextField.getText();
    }

    public void addUserToObservableList(UserDTO userDTO) {
        this.usersObservableList.add(userDTO);
    }
    public void removeUserFromObservableList(UserDTO userDTO){
        this.usersObservableList.remove(userDTO);
    }

    public TableView<UserDTO> getUserTableView() {
        return userTableView;
    }
}
