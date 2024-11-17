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
       ComponentFactory.getInstance(false, primaryStage);
       // final Connection connection =new JDBConnectionWrapper()
    }
}
