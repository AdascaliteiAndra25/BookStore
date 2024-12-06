package launcher;
import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import model.validator.UserValidator;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.report.PDFGenerateReport;
import service.report.PDFGenerateReportService;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;

import service.user.UserService;
import service.user.UserServiceImpl;
import view.LoginView;

import java.sql.Connection;

public class ComponentFactory {
    private final LoginView loginView;
    private final LoginController loginController;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    private final UserService userService;
    private final RightsRolesRepository rightsRolesRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    private final PDFGenerateReportService pdfGenerateReportService;
    private static volatile ComponentFactory instance;
    private static Boolean componentsForTests;
    private static Stage stage;

    public static ComponentFactory getInstance(Boolean aComponentsForTests, Stage aStage) {
        if (instance == null) {
            synchronized (ComponentFactory.class){
            if (instance == null) {
                componentsForTests = aComponentsForTests;
                stage = aStage;
                instance = new ComponentFactory(componentsForTests, stage);
            }
            }
        }

        return instance;
    }

    public ComponentFactory(Boolean componentsForTests, Stage stage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.userService = new UserServiceImpl(userRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        this.loginView = new LoginView(stage);
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService=new BookServiceImpl(bookRepository);
        this.pdfGenerateReportService=new PDFGenerateReport();
        this.loginController = new LoginController(loginView, authenticationService,userService,bookService, stage, pdfGenerateReportService);

    }


    public static Stage getStage(){
        return stage;
    }

    public static Boolean getComponentsForTests(){
        return componentsForTests;
    }

    public AuthenticationService getAuthenticationService(){
        return authenticationService;
    }

    public UserRepository getUserRepository(){
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository(){
        return rightsRolesRepository;
    }

    public LoginView getLoginView(){
        return loginView;
    }

    public BookRepository getBookRepository(){
        return bookRepository;
    }

    public LoginController getLoginController(){
        return loginController;
    }

}