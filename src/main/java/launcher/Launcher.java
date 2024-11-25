package launcher;

import database.JDBConnectionWrapper;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;

public class Launcher extends Application {

    public static void main(String[] args){
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
       //LoginComponentFactory loginComponentFactory=LoginComponentFactory.getInstance(false, primaryStage);
        EmployeeComponentFactory.getInstance(false, primaryStage);
       }
}
